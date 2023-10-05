angular.module('appHouse',[]).controller('viewHouse',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id')

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

    $scope.loadConcreteHouse= function (){
        $http.get(contextPath + '/loadHouse/' + id)
            .then(function (response) {
                $scope.ss = response.data;
                console.log($scope.ss);
                if($scope.ss.accesspoint) {
                    $scope.loadSensorDataInitial();
                    $scope.getLimits();
                    $scope.getTransmissionInterval();
                }
                $scope.accessible = $scope.ss.house.accessible;
            });
    }

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
                setInterval(getCurrentStationValues,$scope.transmissionInterval * 1000);
            });
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




    $scope.loadConcreteHouse();
    $scope.loadPics();
});
