angular.module('appIndex',[]).controller('indexView',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    limitsMin = [];
    limitsMax = [];

    let sensorNames = [];
    let sensorType = [];
    sensorType.push('airQuality');
    sensorType.push('light');
    sensorType.push('soilHumidity');
    sensorType.push('temperature');
    sensorType.push('airHumidity');
    sensorType.push('airPressure');

    $scope.loadMyHouses = function () {
        $http.get(contextPath + '/loadMyHouses')
            .then(function (response) {
                $scope.ss = response.data;
            });
    }

    $scope.goToConcreteHouseSeite = function(sensorStationId){
        window.location.href = contextPath + "/admin/concreteHouse.html?id="+sensorStationId;
    }

    $scope.takeSensorsData = function (sensorStationId,accesspoint,accessible,index){
        console.log("sensorStationId : "+sensorStationId + "accesspoint : " + accesspoint + "accessible : " + accessible + "index : " + index);
        if(accesspoint && accesspoint.accessible){
            getCurrentStationValues(sensorStationId,index);
            $scope.getTransmissionInterval(sensorStationId,index);
        }
    }

    $scope.getTransmissionInterval = function(id,index){
        $http.get(contextPath + '/getTransmissionInterval/'+ id)
            .then(function (response) {
                $scope.transmissionInterval = response.data;
                console.log("transmissionInterval : " + response.data);
                setInterval(getCurrentStationValues.bind(id,index),$scope.transmissionInterval * 1000);
                console.log("interval is set for : " + id);
            });
    }

    getCurrentStationValues = function(id,index){

        let inputs = document.getElementById('repeat').getElementsByTagName('h1');

        console.log("hollma data brudi for : " + id);
        $http.get(contextPath + '/getCurrentStationValues/'+ id)
            .then(function (response) {
                if(response.data) {
                    $scope.currentSensorsValues = response.data;



                    sensorNames.push($scope.currentSensorsValues.airQuality);
                    sensorNames.push($scope.currentSensorsValues.light);
                    sensorNames.push($scope.currentSensorsValues.soilHumidity);
                    sensorNames.push($scope.currentSensorsValues.temperature);
                    sensorNames.push($scope.currentSensorsValues.airHumidity);
                    sensorNames.push($scope.currentSensorsValues.airPressure);

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

                            for(let i = 0; i < sensorNames.length; i++) {
                                if (sensorNames[i]) {
                                    document.getElementById(sensorType[i] + "Value").innerHTML = sensorNames[i].dataValue;
                                    if (sensorNames[i].dataValue > limitsMin[i] && sensorNames[i].dataValue < limitsMax[i]) {
                                        document.getElementById(sensorType[i] + "Value").style.color = "green";
                                    } else {
                                        document.getElementById(sensorType[i] + "Value").style.color = "red";
                                    }
                                }
                            }
                            sensorNames = [];
                        });
                }
            });

    }

    $scope.loadMyHouses();

});