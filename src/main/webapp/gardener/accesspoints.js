angular.module('appAdminAccesspoints',[]).controller('adminAccesspoint',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)

    $scope.pr = '';
    let id = true;
    let location = true;

    $scope.sortId = function() {
        if(id){
            $scope.pr = id;
            document.getElementById("sort_id").innerHTML = '&#8964;';
        }else{
            $scope.pr = -id;
            document.getElementById("sort_id").innerHTML = '^';
        }
        id = !id;
    }

    $scope.sortLocation = function() {
        if(location){
            $scope.pr = '-location';
            document.getElementById("sort_location").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'location';
            document.getElementById("sort_location").innerHTML = '^';
        }
        location = !location;
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
            buttons[offset].setAttribute("id", "save_button_" + index);
            buttons[offset+1].setAttribute("id", "edit_button_" + index);
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

    $scope.loadSensorStations = function(id){
        $http.get(contextPath + '/admin/loadSensorStations')
            .then(function (response) {
                $scope.sensorStationsList = response.data;
               // $scope.currentAccesspointId = id;
            });
    }


    $scope.goToConcreteHouseSeite = function(sensorStationId){
        window.location.href = contextPath + "/gardener/concreteHouse.html?id="+sensorStationId;
    }


    $scope.loadAccesspoints();//show users in table
});