package at.qe.skeleton.services;

import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;

import java.io.Serializable;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
@Component
@Scope("application")
public class UserService implements Serializable {

    @Autowired
    private UserxRepository userRepository;

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Returns a collection of all active users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Userx> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public Userx loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Userx#getCreateDate} for new
     * entities or {@link Userx#getUpdateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link Userx#getCreateDate}
     * or {@link Userx#getUpdateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx saveUser(Userx user) {
        if (user.isNew()) {
            user.setCreateDate(LocalDateTime.now());
            user.setCreateUser(getAuthenticatedUser());
            user.setEnabled(true);
        } else {
            user.setUpdateDate(LocalDateTime.now());
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(Userx user) {
        user.setEnabled(false);
        saveUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx updateUserInfo(Userx user, Set<UserRole> rolesSet, String firstName, String lastName, String email, String phone) {
        user.setRoles(rolesSet);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        return saveUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Userx createUser(Userx user) {
        if (user != null) {
            user = saveUser(user);
        }
        return user;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void createUser(String username, String password) {
        Userx newUser = new Userx();
        newUser.setUsername(username);
        newUser.setPassword(password);
        saveUser(newUser);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void assignRole(Userx user, UserRole role) {
        if (user != null && role != null) {
            Set<UserRole> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }
    }

    public Userx getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    public UserRole getRoleOfAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Set<UserRole> roles = userRepository.findFirstByUsername(username).getRoles();
        return roles.iterator().next();
    }

    public Userx getUserById(String username) {
        Optional<Userx> user = userRepository.findById(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalArgumentException("User with such id is not found");
        }
    }

    public Collection<Userx> getAllActiveUsersBesidesCurrentOne() {
        Collection<Userx> users = userRepository.findAllActiveUsers();
        users.remove(getAuthenticatedUser());
        return users;
    }

    public Collection<Userx> getActiveGardeners() {
        Collection<Userx> gardeners = getAllActiveUsers()
                .stream()
                .filter(g -> g.getRoles().contains(UserRole.GARDENER))
                .collect(Collectors.toList());
        return gardeners;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Userx> getAllActiveGardeners() {
        return userRepository.findAllActiveGardeners(UserRole.GARDENER);
    }

    public boolean loggedInUserIsGardener(Userx user) {
        for (UserRole role : user.getRoles()) {
            if (role.equals(UserRole.GARDENER) || role.equals(UserRole.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public boolean loggedInUserIsGardener() {
        for (UserRole role : getAuthenticatedUser().getRoles()) {
            if (role.equals(UserRole.GARDENER) || role.equals(UserRole.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public boolean loggedInUserIsAdmin() {
        for (UserRole role : getAuthenticatedUser().getRoles()) {
            if (role.equals(UserRole.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a collection of all active gardeners.
     *
     * @return
     */


}
