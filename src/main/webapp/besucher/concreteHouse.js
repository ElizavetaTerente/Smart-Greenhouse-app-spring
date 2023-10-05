angular.module('appBesucher',[]).controller('viewBesucher',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id')
    $scope.id = id;

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

    $scope.loadPics = function(){
        $http.get(contextPath + '/besucher/loadAllPicsOfSensorStation/'+ id)
            .then(function (response) {
                $scope.images = response.data;
                console.log($scope.images);
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
        $http.post(contextPath + '/besucher/uploadPic/' + id, formData, {
            headers: { 'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function (response) {
            $scope.loadPics();
        });

    };
    $scope.loadPics();
});
