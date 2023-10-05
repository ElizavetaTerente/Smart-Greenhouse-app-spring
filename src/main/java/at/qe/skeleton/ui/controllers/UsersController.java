package at.qe.skeleton.ui.controllers;

import at.qe.skeleton.configs.WebSecurityConfig;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.SensorStationService;
import at.qe.skeleton.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class UsersController {

    @Autowired
    UserService userService;

    @Autowired
    SensorStationService sensorStationService;

    @Autowired
    WebSecurityConfig webSecurityConfig;

    @GetMapping("/admin/loadUsers")
    public Collection<Userx> loadUsersBesidesCurrentOne(){
        return userService.getAllActiveUsersBesidesCurrentOne();
    }

    @PostMapping("/admin/editUser/{username}/{roles}/{firstName}/{lastName}/{email}/{phone}")
    public void editUser(@PathVariable String username, @PathVariable List<UserRole> roles, @PathVariable String firstName, @PathVariable String lastName,@PathVariable String email,@PathVariable String phone){
        Set<UserRole> rolesSet = new HashSet<>(roles);
        Userx user = userService.getUserById(username);
        userService.updateUserInfo(user,rolesSet,firstName,lastName,email,phone);

    }

    @DeleteMapping("/admin/deleteUser/{username}")
    public void deleteUser(@PathVariable String username){
        userService.deleteUser(userService.getUserById(username));
    }

    @PostMapping("/admin/newUser/{username}/{password}/{roles}/{firstName}/{lastName}/{email}/{phone}")
    public void addNewUser(@PathVariable String username,@PathVariable String password,@PathVariable List<UserRole> roles,@PathVariable String firstName,@PathVariable String lastName,@PathVariable String email,@PathVariable String phone){
        Set<UserRole> rolesSet = new HashSet<>(roles);
        password = webSecurityConfig.passwordEncoder().encode(password);
        Userx user = new Userx();
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(rolesSet);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        if (userService.loadUser(user.getUsername()) == null) {
            userService.createUser(user);
        }
    }

    @GetMapping("/admin/loadGardeners/{id}")
    public Collection<Userx> loadActiveGardeners(@PathVariable Long id){
        Set<Userx> currentGardeners = sensorStationService.loadSensorstation(id).getManagingUsers();
        Set<Userx> allGardeners = new HashSet<Userx>(userService.getActiveGardeners());
        allGardeners.removeAll(currentGardeners);
        return allGardeners;
    }




}
