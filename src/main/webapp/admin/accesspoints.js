angular.module('appAdminAccesspoints',[]).controller('adminAccesspoint',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)

    $scope.pr = 'accesspoint.accesspointId';
    let accesspointId = true;
    let location = true;
    let accessible = true;
    let transmissionInterval = true;


    $scope.sortAccesspointId = function(){
        if(accesspointId){
            $scope.pr = '-accesspoint.accesspointId';
            document.getElementById("sort_accesspointId").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'accesspoint.accesspointId';
            document.getElementById("sort_accesspointId").innerHTML = '^';
        }
        accesspointId = !accesspointId;
    }

    $scope.sortLocation = function() {
        if(location){
            $scope.pr = '-accesspoint.locationName';
            document.getElementById("sort_location").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'accesspoint.locationName';
            document.getElementById("sort_location").innerHTML = '^';
        }
        location = !location;
    }

    $scope.sortAccessible = function() {
        if(accessible){
            $scope.pr = '-accesspoint.accessible';
            document.getElementById("sort_accessible").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'accesspoint.accessible';
            document.getElementById("sort_accessible").innerHTML = '^';
        }
        accessible = !accessible;
    }

    $scope.sortAccesspointByTransmissionInterval = function (){

        console.log("sort trans inter");

            if(transmissionInterval){
                $scope.pr = '[time.hours,time.minutes,time.seconds]';
                document.getElementById("sort_transmissionInterval").innerHTML = '&#8964;';
            }else{
                $scope.pr = '-[time.hours,time.minutes,time.seconds]';
                document.getElementById("sort_transmissionInterval").innerHTML = '^';
            }
        transmissionInterval = !transmissionInterval;

    }

    $scope.editAccesspoint = function (id, index) {
        //take all the data from user_table with tag "input" in html
        let inputs = document.getElementById('accesspoints_table').rows[index].getElementsByTagName('input');

        //to identify row which admin wants to edit to every data(input type in this case because editable)
        //i add index with column name and index of the row which i recieved from html function call
        console.log(inputs);
        inputs[1].setAttribute("id", "locationName_" + index);
        inputs[3].setAttribute("id", "intervalHours_" + index);
        inputs[4].setAttribute("id", "intervalMinutes_" + index);
        inputs[5].setAttribute("id", "intervalSeconds_" + index);

        //making all data in row editable
        document.getElementById("locationName_" + index).setAttribute("style", "width:100px");
        document.getElementById("locationName_" + index).removeAttribute("readonly");

        document.getElementById("intervalHours_" + index).setAttribute("style", "width:57px;display: inline-block");
        document.getElementById("intervalHours_" + index).removeAttribute("readonly");
        document.getElementById("intervalMinutes_" + index).setAttribute("style", "width:57px;display: inline-block");
        document.getElementById("intervalMinutes_" + index).removeAttribute("readonly");
        document.getElementById("intervalSeconds_" + index).setAttribute("style", "width:57px;display: inline-block");
        document.getElementById("intervalSeconds_" + index).removeAttribute("readonly");

        //load all the buttons from this row in array
        let buttons = document.getElementById('accesspoints_table').rows[index].getElementsByTagName('button');
        console.log(buttons);
        //set id to buttons in this row to identify them
        let offset = $scope.accesspointsList[index-1].sensorStations.length;
        console.log("offset : " + offset);
        console.log("index : " + index);
            buttons[offset*2+2].setAttribute("id", "save_button_" + index);
            buttons[offset*2+3].setAttribute("id", "edit_button_" + index);
        //hide "edit" button and show "save" button
        document.getElementById("edit_button_" + index).style.display = "none";
        document.getElementById("save_button_" + index).style.display = "block";
    }

    $scope.saveAccesspoint = function (id, index) {
        //saving new values of a row
        let locationName = document.getElementById("locationName_" + index).value;
        let intervalHours = document.getElementById("intervalHours_" + index).value;
        let intervalMinutes = document.getElementById("intervalMinutes_" + index).value;
        let intervalSeconds=document.getElementById("intervalSeconds_" + index).value;


        //making all data not editable again
        document.getElementById("locationName_" + index).setAttribute("style", "border:none;background: transparent;width:100px");
        document.getElementById("locationName_" + index).setAttribute("readonly", "true");

        document.getElementById("intervalHours_" + index).setAttribute("style", "width:57px;display: inline-block;border:none;background: transparent");
        document.getElementById("intervalHours_" + index).setAttribute("readonly", "true");
        document.getElementById("intervalMinutes_" + index).setAttribute("style", "width:57px;display: inline-block;border:none;background: transparent");
        document.getElementById("intervalMinutes_" + index).setAttribute("readonly", "true");
        document.getElementById("intervalSeconds_" + index).setAttribute("style", "width:57px;display: inline-block;border:none;background: transparent");
        document.getElementById("intervalSeconds_" + index).setAttribute("readonly", "true");

        //load all buttons in this row info
        //hide "save" button and show again "edit" button
        document.getElementById("edit_button_" + index).style.display = "inline-block";
        document.getElementById("save_button_" + index).style.display = "none";

        console.log(intervalHours);
        console.log(intervalMinutes);
        console.log(intervalSeconds);

        //pass edited info to backend
        $http.post(contextPath + '/editAccesspoint/' + id +'/'+ locationName +'/'+ intervalHours + '/' + intervalMinutes + '/' + intervalSeconds).then(function (response) {
            $scope.loadAccesspoints();
        });
    }

    $scope.loadAccesspoints = function () {;
        $http.get(contextPath + '/loadAccesspoints')
            .then(function (response) {
                $scope.accesspointsList = response.data;
                console.log($scope.accesspointsList);
            });
    }

    $scope.deleteAccesspoint = function (id) {

        var confirmed = confirm("Are you sure you want to delete this Accesspoint?");

        if(confirmed){
        $http.delete(contextPath + '/admin/deleteAccesspoint/' + id)
            .then(function (response){
                $scope.loadAccesspoints();
            });
        alert("Accesspoint wiht id: " + id + " deleted!");
        $scope.loadAccesspoints();
        }
    }


    $scope.acceptAccesspoint = function (id) {

        $http.post(contextPath + '/admin/acceptAccesspoint/' + id)
            .then(function (response) {
                $scope.loadAccesspoints();
            });
    }

    $scope.searchSensorStations = function (id) {
        console.log("in func");
        $http.get(contextPath + '/admin/searchSensorStations/' + id)
            .then(function (response) {
                $scope.sensorStationsList = response.data;
                console.log(response.data);
                $scope.currentAccesspointId = id;
                console.log("id = " + $scope.currentAccesspointId);
            });
    }

    $scope.loadSensorStations = function(id){
        $http.get(contextPath + '/loadSensorStations')
            .then(function (response) {
                $scope.sensorStationsList = response.data;
               // $scope.currentAccesspointId = id;
            });
    }

    $scope.acceptSensorStation = function (id) {
        $http.post(contextPath + '/admin/acceptSensorStation/' + id)
            .then(function (response) {
                console.log("accepted!");
                $scope.loadSensorStations(id);
            });
    }

    $scope.chooseSensorStation = function (sensorStationId) {
        $http.post(contextPath + '/admin/addSensorStationToAccesspoint/' + sensorStationId + '/' + $scope.currentAccesspointId)
            .then(function (response) {
                console.log("chosen" + sensorStationId);
                $scope.loadSensorStations(sensorStationId);
                $scope.loadAccesspoints();
            });
    }

    $scope.deleteSensorStationFromAccesspoint = function(sensorStationId){
        $http.post(contextPath + '/admin/deleteSensorStationFromAccesspoint/' + sensorStationId)
            .then(function (response) {
                console.log("deleted");
                $scope.loadAccesspoints();
            });
    }

    $scope.goToConcreteHouseSeite = function(sensorStationId){
        window.location.href = contextPath + "/admin/concreteHouse.html?id="+sensorStationId;
    }


    $scope.loadAccesspoints();//show users in table
});