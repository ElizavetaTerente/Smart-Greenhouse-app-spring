angular.module('appAdminUsers',[]).controller('adminUsers',function ($scope,$http) {

    const contextPath = window.location.protocol + "//" + window.location.host;

    //sorting in tables

    //sorted by parameter pr . Initially pr = '' (empty) ,so we see values in the table how they are
    //if the "^" button is clicked then corresponding sort function run (because we can sort several columns)
    //and the sorting parameter in html is set. Button change from "^" to "⌄". and corresponding to column
    //boolean variables is changing its value (to track which sort we need to do next : in desc order or asc)

    $scope.pr = '';
    let username = true;
    let lastName = true;
    let firstName = true;
    let email = true;
    let phone = true;

    $scope.sortUsername = function() {
        if(username){
            $scope.pr = '-username';
            document.getElementById("sort_username").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'username';
            document.getElementById("sort_username").innerHTML = '^';
        }
        username = !username;
    }

    $scope.sortLastName = function() {
        if(lastName){
            $scope.pr = '-lastName';
            document.getElementById("sort_lastName").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'lastName';
            document.getElementById("sort_lastName").innerHTML = '^';
        }
        lastName = !lastName;
    }

    $scope.sortFirstName = function() {
        if(firstName){
            $scope.pr = '-firstName';
            document.getElementById("sort_firstName").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'firstName';
            document.getElementById("sort_firstName").innerHTML = '^';
        }
        firstName = !firstName;
    }

    $scope.sortEmail = function() {
        if(email){
            $scope.pr = '-email';
            document.getElementById("sort_email").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'email';
            document.getElementById("sort_email").innerHTML = '^';
        }
        email = !email;
    }

    $scope.sortPhone = function() {
        if(phone){
            $scope.pr = '-phone';
            document.getElementById("sort_phone").innerHTML = '&#8964;';
        }else{
            $scope.pr = 'phone';
            document.getElementById("sort_phone").innerHTML = '^';
        }
        phone = !phone;
    }



//function to print users in html table
    $scope.loadUsers = function () {;
        $http.get(contextPath + '/admin/loadUsers')
            .then(function (response) {
                $scope.usersList = response.data;
            });
    }
/*
    //checking by adding new user if such a username already exists,
    //because its unique index in out database
    //in case exists - corresponding message is shown
    $scope.checkIfUserWithSuchANameAlreadyExists = function (wantedUsername) {
        $scope.usersList.forEach(function (user) {
            if (user.username === wantedUsername) {
                alert("such Username already exists." +
                    "Change it please to to able to add new user");
                //interrupt adding user process
                bool = true;
            }
        });
    }

    //variables shows if username of the user which admin want to add in invalid
    //defaultly false;
    let bool = false;
    */
    $scope.addNewUser = function () {
        //by adding new user at leat one role need to be specified
        let roleAdmin = document.getElementById("roleAdmin").checked;
        let roleGardener = document.getElementById("roleGardener").checked;
        let roleUser = document.getElementById("roleUser").checked;
        if (roleAdmin === false && roleGardener === false && roleUser === false){
            alert("check at least one role");
            return;
        }

        //adding roles in array to pass to backend
        let roles = [];

        if (roleAdmin === true) {
            roles.push("ADMIN");
        }
        if (roleGardener === true) {
            roles.push("GARDENER");
        }
        if (roleUser === true) {
            roles.push("USER");
        }

        $http.post(contextPath + '/admin/newUser/' + $scope.newUser.username + '/' + $scope.newUser.password + '/' + roles + '/' + $scope.newUser.firstName + '/' + $scope.newUser.lastName + '/' + $scope.newUser.email + '/' + $scope.newUser.phone)
            .then(function (response) {
                $scope.loadUsers();
            });
        //reload info to see changes
        //closing "add new user" form after submit
        document.getElementById("addNewUserForm").reset();
        document.getElementById('addUserButton').click();
    }

    $scope.deleteUser = function (username) {

        var confirmed = confirm("Are you sure you want to delete this user?");

        if(confirmed){
        $http.delete(contextPath + '/admin/deleteUser/' + username)
            .then(function (response){
                $scope.loadUsers();
            });
        alert("User " + username + " deleted!");
        }
    }
    //next 2 functions is edit-save row process (of user table)
    $scope.editUser = function (username, index) {
        //take all the data from user_table with tag "input" in html
        let inputs = document.getElementById('users_table').rows[index].getElementsByTagName('input');

        //to identify row which admin wants to edit to every data(input type in this case because editable)
        //i add index with column name and index of the row which i recieved from html function call
        inputs[1].setAttribute("id", "roleAdmin_" + index);
        inputs[2].setAttribute("id", "roleGardener_" + index);
        inputs[3].setAttribute("id", "roleUser_" + index);
        inputs[4].setAttribute("id", "lastName_" + index);
        inputs[5].setAttribute("id", "firstName_" + index);
        inputs[6].setAttribute("id", "email_" + index);
        inputs[7].setAttribute("id", "phone_" + index);

        //making all data in row editable
        document.getElementById("roleAdmin_" + index).removeAttribute("disabled");
        document.getElementById("roleGardener_" + index).removeAttribute("disabled");
        document.getElementById("roleUser_" + index).removeAttribute("disabled");

        document.getElementById("lastName_" + index).setAttribute("style","width:120px");
        document.getElementById("lastName_" + index).removeAttribute("readonly");

        document.getElementById("firstName_" + index).setAttribute("style","width:120px");
        document.getElementById("firstName_" + index).removeAttribute("readonly");

        document.getElementById("email_" + index).setAttribute("style","width:170px");
        document.getElementById("email_" + index).removeAttribute("readonly");

        document.getElementById("phone_" + index).setAttribute("style","width:140px");
        document.getElementById("phone_" + index).removeAttribute("readonly");

        //load all the buttons from this row in array
        let buttons = document.getElementById('users_table').rows[index].getElementsByTagName('button');
        //set id to buttons in this row to identify them
        buttons[0].setAttribute("id", "save_button_" + index);
        buttons[1].setAttribute("id", "edit_button_" + index);
        //hide "edit" button and show "save" button
        document.getElementById("edit_button_" + index).style.display = "none";
        document.getElementById("save_button_" + index).style.display = "block";
    }

    $scope.saveUser = function (username, index) {
        //saving new values of a row
        let roleAdmin = document.getElementById("roleAdmin_" + index).checked;
        let roleGardener = document.getElementById("roleGardener_" + index).checked;
        let roleUser = document.getElementById("roleUser_" + index).checked;
        let firstName = document.getElementById("firstName_" + index).value;
        let lastName = document.getElementById("lastName_" + index).value;
        let email = document.getElementById("email_" + index).value;
        let phone = document.getElementById("phone_" + index).value;

        //if no roles checked print message and interrupt process
        if (roleAdmin !== true && roleGardener !== true && roleUser !== true) {
            alert("check at least one role!");
            return;
        }

        if(firstName == "" || lastName == "" || email == "" || phone == ""){
            alert("The field cannot be empty!");
            return;
        }

        //making all data not editable again
        document.getElementById("roleAdmin_" + index).disabled = true;
        document.getElementById("roleGardener_" + index).disabled = true;
        document.getElementById("roleUser_" + index).disabled = true;

        document.getElementById("firstName_" + index).setAttribute("style", "border:none;background: transparent;width:120px");
        document.getElementById("firstName_" + index).setAttribute("readonly", "true");

        document.getElementById("lastName_" + index).setAttribute("style", "border:none;background: transparent;width:120px");
        document.getElementById("lastName_" + index).setAttribute("readonly", "true");

        document.getElementById("email_" + index).setAttribute("style", "border:none;background: transparent");
        document.getElementById("email_" + index).setAttribute("readonly", "true");

        document.getElementById("phone_" + index).setAttribute("style", "border:none;background: transparent");
        document.getElementById("phone_" + index).setAttribute("readonly", "true");

        //load all buttons in this row info
        //hide "save" button and show again "edit" button
        document.getElementById("edit_button_" + index).style.display = "inline-block";
        document.getElementById("save_button_" + index).style.display = "none";

        //put roles in array to pass them to backend
        let roles = [];

        if (roleAdmin === true) {
            roles.push("ADMIN");
        }
        if (roleGardener === true) {
            roles.push("GARDENER");
        }
        if (roleUser === true) {
            roles.push("USER");
        }
        //pass edited info to backend
        $http.post(contextPath + '/admin/editUser/' + username +'/'+ roles +'/'+ firstName + '/' + lastName + '/' + email + '/' + phone);
    }

    //to visualise user roles in CheckBoxe for table
    $scope.checkedFunc = function (roles, index) {
        let inputs = document.getElementById('users_table').rows[index].getElementsByTagName('input');
        if (roles.includes("ADMIN")) {
            inputs[1].checked = true;
        }
        if (roles.includes("GARDENER")) {
            inputs[2].checked = true;
        }
        if (roles.includes("USER")) {
            inputs[3].checked = true;
        }
    }
    /*
    $scope.closeDropdown = function() {
        $('#collapseExample').hide();
    }
 */
    $scope.loadUsers();//show users in table
});
