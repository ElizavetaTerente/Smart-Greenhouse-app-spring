angular.module('appHouse',[]).controller('viewHouse',function ($scope,$http,$compile) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id')
    $scope.id = id;

    let sensorNames = [];
    let sensorType = [];
    sensorType.push('airQuality');
    sensorType.push('light');
    sensorType.push('soilHumidity');
    sensorType.push('temperature');
    sensorType.push('airHumidity');
    sensorType.push('airPressure');

    let limitsMin = [];
    let limitsMax = [];

    let xValues = [];
    let yValues = [];

    $scope.printQr = function() {
        new QRCode(document.getElementById("qrcode"), "10.0.0.200:8080/gallery/gallery.html");
        window.print();
    }

    function generateData() {
        //let canvases = document.getElementsByTagName('canvas');
        //console.log(canvases);

        xValues = [];
        yValues = [];
        for (let i = 0; i < $scope.sensorData.airQuality.length; i++) {
            xValues.push($scope.sensorData.airQuality[i].dataTime);
            yValues.push($scope.sensorData.airQuality[i].dataValue);
        }
        console.log(xValues);

        const ctx1 = document.getElementById("chart1").getContext('2d');
        const myChart1 = new Chart(ctx1, {

            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: 'AIR QUALITY',
                    borderColor: '#36A2EB',
                    backgroundColor: '#9BD0F5',
                    data: yValues,
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'time',
                        ticks: {
                            source: 'auto'
                        }
                    }]
                }
            },
        });

        xValues = [];
        yValues = [];
        for (let i = 0; i < $scope.sensorData.light.length; i++) {
            xValues.push($scope.sensorData.light[i].dataTime);
            yValues.push($scope.sensorData.light[i].dataValue);
        }

        const ctx2 = document.getElementById("chart2").getContext('2d');
        const myChart2 = new Chart(ctx2, {

            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: 'LIGHT QUALITY',
                    borderColor: '#FFFF5C',
                    backgroundColor: '#D1D100',
                    data: yValues,
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'time',
                        ticks: {
                            source: 'auto'
                        }
                    }]
                }
            },
        });

        xValues = [];
        yValues = [];
        for (let i = 0; i < $scope.sensorData.soilHumidity.length; i++) {
            xValues.push($scope.sensorData.soilHumidity[i].dataTime);
            yValues.push($scope.sensorData.soilHumidity[i].dataValue);
        }

        const ctx3 = document.getElementById("chart3").getContext('2d');
        const myChart3 = new Chart(ctx3, {

            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: 'SOIL HUMIDITY ',
                    borderColor: '#A95700',
                    backgroundColor: '#7B3F00',
                    data: yValues,
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'time',
                        ticks: {
                            source: 'auto'
                        }
                    }]
                }
            },
        });

        xValues = [];
        yValues = [];
        for (let i = 0; i < $scope.sensorData.temperature.length; i++) {
            xValues.push($scope.sensorData.temperature[i].dataTime);
            yValues.push($scope.sensorData.temperature[i].dataValue);
        }

        const ctx4 = document.getElementById("chart4").getContext('2d');
        const myChart4 = new Chart(ctx4, {

            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: 'TEMPERATURE',
                    borderColor: '#EE4B2B',
                    backgroundColor: '#880808',
                    data: yValues,
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'time',
                        ticks: {
                            source: 'auto'
                        }
                    }]
                }
            },
        });

        xValues = [];
        yValues = [];
        for (let i = 0; i < $scope.sensorData.airHumidity.length; i++) {
            xValues.push($scope.sensorData.airHumidity[i].dataTime);
            yValues.push($scope.sensorData.airHumidity[i].dataValue);
        }

        const ctx5 = document.getElementById("chart5").getContext('2d');
        const myChart5 = new Chart(ctx5, {

            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: 'AIR HUMIDITY',
                    borderColor: '#00FFFF',
                    backgroundColor: '#F0FFFF',
                    data: yValues,
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'time',
                        ticks: {
                            source: 'auto'
                        }
                    }]
                }
            },
        });

        xValues = [];
        yValues = [];
        for (let i = 0; i < $scope.sensorData.airPressure.length; i++) {
            xValues.push($scope.sensorData.airPressure[i].dataTime);
            yValues.push($scope.sensorData.airPressure[i].dataValue);
        }

        const ctx6 = document.getElementById("chart6").getContext('2d');
        const myChart6 = new Chart(ctx6, {

            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: 'AIR PRESSURE',
                    borderColor: '#3F00FF',
                    backgroundColor: '#5D3FD3',
                    data: yValues,
                }]
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'time',
                        ticks: {
                            source: 'auto'
                        }
                    }]
                }
            },
        });
    }

    $scope.loadConcreteHouse= function (){
        $http.get(contextPath + '/loadHouse/' + id)
            .then(function (response) {
                $scope.ss = response.data;
                console.log($scope.ss);
                if($scope.ss.accesspoint) {
                   // $scope.getLimits();
                    $scope.getTransmissionInterval();
                }
                $scope.loadSensorDataInitial();
                $scope.accessible = $scope.ss.house.accessible;
            });
    }

    $scope.loadGardeners = function(){
        $http.get(contextPath + '/admin/loadGardeners/' + id)
            .then(function (response) {
                $scope.gardeners = response.data;
                console.log($scope.gardeners)
            });
    }

    $scope.addGardenerToHouse = function(sensorStationId){
        var selectBox = document.getElementById("addNewGardenerValue");
        var selectedOption = selectBox.options[selectBox.selectedIndex];
        var gardener = selectedOption.textContent;
        $http.post(contextPath + '/admin/addGardenerToHouse/' + sensorStationId + '/' + gardener)
            .then(function (response) {
                $scope.loadGardeners();
                $scope.loadConcreteHouse();
                console.log("added");
            });
    }

    $scope.deleteGardenerFromHouse = function(gardener){

        $http.post(contextPath + '/admin/deleteGardenerFromHouse/' + id + '/' + gardener)
            .then(function (response) {
                $scope.loadGardeners();
                $scope.loadConcreteHouse();
                console.log("deleted");
            });
    }

    $scope.saveHouseChanges = function(){
        console.log($scope.houseChanges);
        $http.post(contextPath + '/admin/saveHouseChanges/'+ id + '/' + $scope.houseChanges.uuid + '/' + $scope.houseChanges.name)
            .then(function (response) {
                $scope.loadConcreteHouse();
                console.log("changed");
            });
    }

    $(function() {
        document.getElementById("dateAndTime").setAttribute("value",moment().subtract(1,'hours').format('YYYY-MM-DD-HH-mm-ss') + " - " + moment().format('YYYY-MM-DD HH:mm:ss'));
        $('input[name="daterange"]').daterangepicker({
            timePicker: true,
            timePickerIncrement: 5,
            timePicker24Hour : true,
            timePickerSeconds : true,
            locale: {
                format: 'YYYY-MM-DD HH:mm:ss'
            },
            ranges: {
                'Last Hour': [moment().subtract(1,'hours'), moment()],
                'Today': [moment().startOf('day'), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment()],
                'This week': [moment().subtract(7, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            }
        });
    });

    $('input[name="daterange"]').on('apply.daterangepicker', function(ev, picker) {
        console.log(picker.startDate.format('YYYY-MM-DD-HH-mm-ss'));
        console.log(picker.endDate.format('YYYY-MM-DD HH-mm-ss'));
        let startDate = picker.startDate.format('YYYY-MM-DD-HH-mm-ss');
        let endDate = picker.endDate.format('YYYY-MM-DD-HH-mm-ss');
        $scope.loadSensorData(startDate,endDate);
    });

    $scope.loadSensorData = function(startDate,endDate){
        $http.get(contextPath + '/getSensorDataInRange/'+ id + '/' + startDate + '/' + endDate)
            .then(function (response) {
                    $scope.sensorData = response.data;
                    console.log($scope.sensorData);

                generateData();
            });
    }

    $scope.loadSensorDataInitial= function(){
        console.log(moment().startOf('day').format('YYYY-MM-DD-HH-mm-ss'));
        $scope.loadSensorData(moment().subtract(1,'hours').format('YYYY-MM-DD-HH-mm-ss'),moment().format('YYYY-MM-DD-HH-mm-ss'));
    }

/*
    $scope.setIntervalForAFunction = function(functionName,time){
        setInterval(functionName, time);
    }

 */

    function getCurrentStationValues(){
        console.log("hollma data brudi");
        $http.get(contextPath + '/getCurrentStationValues/'+ id)
            .then(function (response) {
                if(response.data) {
                    console.log(response.data);
                    $scope.currentSensorsValues = response.data;
                    $scope.accessible = $scope.currentSensorsValues.accessible;

                    sensorNames.push($scope.currentSensorsValues.airQuality);
                    sensorNames.push($scope.currentSensorsValues.light);
                    sensorNames.push($scope.currentSensorsValues.soilHumidity);
                    sensorNames.push($scope.currentSensorsValues.temperature);
                    sensorNames.push($scope.currentSensorsValues.airHumidity);
                    sensorNames.push($scope.currentSensorsValues.airPressure);

                    for(let i = 0; i < sensorNames.length; i++) {
                        if (sensorNames[i]) {
                            document.getElementById(sensorType[i] + "Value").innerHTML = sensorNames[i].dataValue;
                            console.log("da : " + i);
                            if (sensorNames[i].dataValue > limitsMin[i] && sensorNames[i].dataValue < limitsMax[i]) {
                                document.getElementById(sensorType[i] + "Value").style.color = "green";
                            } else {
                                document.getElementById(sensorType[i] + "Value").style.color = "red";
                            }
                            document.getElementById(sensorType[i] + "Access").innerHTML = "Accessible";
                            document.getElementById(sensorType[i] + "Access").style.color = "greenyellow";
                        } else {
                            document.getElementById(sensorType[i] + "Access").innerHTML = "Not Accessible";
                            document.getElementById(sensorType[i] + "Access").style.color = "red";
                        }
                    }
                    sensorNames = [];
                }
            });
    }

    $scope.getTransmissionInterval = function(){
        getCurrentStationValues();
        $http.get(contextPath + '/getTransmissionInterval/'+ id)
            .then(function (response) {
                $scope.transmissionInterval = response.data;
                console.log("transmissionInterval : " + response.data);
                setInterval(getCurrentStationValues,$scope.transmissionInterval* 1000);
            });
    }

    $scope.editNormalValues = function(sensorName){

        console.log(sensorName);

        document.getElementById(sensorName + "_Min").setAttribute("style","width: 35px");
        document.getElementById(sensorName + "_Min").removeAttribute("readonly");

        document.getElementById(sensorName + "_Max").setAttribute("style","width: 35px");
        document.getElementById(sensorName + "_Max").removeAttribute("readonly");

        document.getElementById(sensorName + "_EditButton").style.display = "none";
        document.getElementById(sensorName + "_SaveButton").style.display = "inline-block";
    }

    $scope.saveNormalValues = function(sensorName){

        let min = document.getElementById(sensorName + "_Min").value;
        let max = document.getElementById(sensorName + "_Max").value;
        if(!(min && max)){
            alert("interval value is missing");
        }else if(isNaN(min) || isNaN(max)) {
            alert("interval should be a number");
        }else {

        document.getElementById(sensorName + "_Min").setAttribute("style", "border:none;background: transparent;width: 35px");
        document.getElementById(sensorName + "_Min").setAttribute("readonly", "true");

        document.getElementById(sensorName + "_Max").setAttribute("style", "border:none;background: transparent;width: 35px");
        document.getElementById(sensorName + "_Max").setAttribute("readonly", "true");

        document.getElementById(sensorName + "_EditButton").style.display = "inline-block";
        document.getElementById(sensorName + "_SaveButton").style.display = "none";


            $http.post(contextPath + '/editLimits/' + id + '/' + sensorName + '/' + min + '/' + max)
                .then(function (response) {
                    $scope.getLimits();
                });
        }
    }

    $scope.deleteSensorStation = function(){

        var confirmed = confirm("Are you sure you want to delete this sensor station?");

        if (confirmed) {
            $http.delete(window.location.origin + '/admin/deleteSensorStation/' + id)
                .then(function (response) {
                    alert("house deleted!");
                    window.location.href = window.location.origin + "/admin/allHouses.html";
                });
        }

    }

    $scope.getLimits = function(){
            $http.get(contextPath + '/getLimitsOfSensorStation/'+ id)
                .then(function (response) {
                    $scope.limits = response.data;
                    limitsMin = [];
                    limitsMax = [];
                    limitsMin.push($scope.limits.airQuality.min);
                    limitsMin.push($scope.limits.light.min);
                    limitsMin.push($scope.limits.soilHumidity.min);
                    limitsMin.push($scope.limits.temperature.min);
                    limitsMin.push($scope.limits.airHumidity.min);
                    limitsMin.push($scope.limits.airPressure.min);

                    limitsMax.push($scope.limits.airQuality.max);
                    limitsMax.push($scope.limits.light.max);
                    limitsMax.push($scope.limits.soilHumidity.max);
                    limitsMax.push($scope.limits.temperature.max);
                    limitsMax.push($scope.limits.airHumidity.max);
                    limitsMax.push($scope.limits.airPressure.max);
                });
    }
//galerie
    const output = document.getElementById("imageOutput");
    const input = document.getElementById("imageInput");
    $scope.imagesArray = [];
    var imagePath;
    var picIndex = -1;
    let name = '';

    input.addEventListener("change", () => {
        $scope.imagesArray = [];
        const files = input.files;
        for (let i=0; i < files.length; i++){
            $scope.imagesArray.push(files[i]);
        }
        $scope.uploadFile();
    })

    $scope.deleteImage = function(id){
        $http.delete(contextPath + '/deletePic/'+id)
            .then(function (response) {
                $scope.loadPics();
            });
    }


    $scope.loadPics = function(){
        $http.get(contextPath + '/loadAllPicsOfSensorStation/'+ id)
            .then(function (response) {
                $scope.images = response.data;
            });
    }
    //end of galerie

    $scope.uploadFile = function() {

        var imageInput = document.getElementById('imageInput');
        var file = imageInput.files[0];

        if (file) {
            var fileSize = file.size; // in bytes
            var maxSize = 128 * 1024; // 128kB
    
            if (fileSize > maxSize) {
                alert('File size exceeds the limit of 128kB. Please choose a smaller file.');
                return; // Abort the function if file size exceeds the limit
            }
        }

        var formData = new FormData();
        formData.append('file', file);

        // Make the request
        $http.post(contextPath + '/uploadPic/' + id, formData, {
            headers: { 'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function (response) {
            $scope.loadPics();
        });

    };

    $scope.getLimits();
    $scope.loadConcreteHouse();
    $scope.loadPics();
});
