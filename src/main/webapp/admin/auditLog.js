angular.module('appLog',[]).controller('viewLog',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    $scope.pr = '-logId';
    let logId = true;
    let correspondingObject = true;
    let id = true;
    let username = true;
    let eventType = true;
    let timestamp = true;

    $scope.sortByUsername = function(){
        if(username){
            $scope.pr = '-username';
            document.getElementById("sort_username").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'username';
            document.getElementById("sort_username").innerHTML = '^';
        }
        username = !username;
    }

    $scope.sortById = function(){
        if(id){
            $scope.pr = '-id';
            document.getElementById("sort_id").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'id';
            document.getElementById("sort_id").innerHTML = '^';
        }
        id = !id;
    }

    $scope.sortByCorrespondingObject = function(){
        if(correspondingObject){
            $scope.pr = '-correspondingObject';
            document.getElementById("sort_correspondingObject").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'correspondingObject';
            document.getElementById("sort_correspondingObject").innerHTML = '^';
        }
        correspondingObject = !correspondingObject;
    }

    $scope.sortByEventType = function(){
        if(eventType){
            $scope.pr = '-eventType';
            document.getElementById("sort_eventType").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'eventType';
            document.getElementById("sort_eventType").innerHTML = '^';
        }
        eventType = !eventType;
    }

    $scope.sortByLogId = function(){
        if(logId){
            $scope.pr = '-logId';
            document.getElementById("sort_logId").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'logId';
            document.getElementById("sort_logId").innerHTML = '^';
        }
        logId = !logId;
    }

    $scope.sortByTimestamp = function(){
        if(timestamp){
            $scope.pr = '-timestamp';
            document.getElementById("sort_timestamp").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'timestamp';
            document.getElementById("sort_timestamp").innerHTML = '^';
        }
        timestamp = !timestamp;
    }


    $scope.loadLog = function(){
        $http.get(contextPath + '/admin/getLog')
            .then(function (response) {
                $scope.logList = response.data;
                console.log($scope.logList);
            });
        setInterval($scope.loadLog,1000 * 60 * 60);
    }

    $scope.loadLog();
});