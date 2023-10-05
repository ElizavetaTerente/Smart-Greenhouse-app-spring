package at.qe.skeleton.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.services.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@Transactional
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        userService.createUser("test_user", "test_password");
        Userx testUser = userService.loadUser("test_user");
        Assertions.assertNotNull(testUser, "User \"" + testUser.getUsername() + "\" could not be loaded from test data source");
        Assertions.assertTrue(testUser.isEnabled());
        userService.deleteUser(testUser);
        Assertions.assertFalse(testUser.isEnabled());
        Assertions.assertNotNull(testUser, "User \"" + testUser.getUsername() + "\" was deleted from database, but should only be set to 'enabled' = false");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser() {
        userService.createUser("test_user", "test_password");
        Userx testUser = userService.loadUser("test_user");
        Userx toBeSavedUser = userService.loadUser(testUser.getUsername());
        Assertions.assertNotNull(toBeSavedUser, "User \"" + testUser.getUsername() + "\" could not be loaded from test data source");

        Assertions.assertNull(toBeSavedUser.getUpdateUser(), "User \"" + testUser.getUsername() + "\" has a updateUser defined");
        Assertions.assertNull(toBeSavedUser.getUpdateDate(), "User \"" + testUser.getUsername() + "\" has a updateDate defined");

        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUser);

        Userx freshlyLoadedUser = userService.loadUser("test_user");
        Assertions.assertNotNull(freshlyLoadedUser, "User \"" + testUser.getUsername() + "\" could not be loaded from test data source after being saved");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateUser(), "User \"" + testUser.getUsername() + "\" does not have a updateUser defined after being saved");
        Assertions.assertNotNull(freshlyLoadedUser.getUpdateDate(), "User \"" + testUser.getUsername() + "\" does not have a updateDate defined after being saved");
        Assertions.assertEquals("changed-email@whatever.wherever", freshlyLoadedUser.getEmail(), "User \"" + testUser.getUsername() + "\" does not have a the correct email attribute stored being saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser() {
        String username = "newuser";
        String password = "passwd";
        String fName = "New";
        String lName = "User";
        String email = "new-email@whatever.wherever";
        String phone = "+12 345 67890";
        Userx toBeCreatedUser = new Userx();
        toBeCreatedUser.setUsername(username);
        toBeCreatedUser.setPassword(password);
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName(fName);
        toBeCreatedUser.setLastName(lName);
        toBeCreatedUser.setEmail(email);
        toBeCreatedUser.setPhone(phone);
        toBeCreatedUser.setRoles(Sets.newSet(UserRole.USER, UserRole.GARDENER));
        userService.saveUser(toBeCreatedUser);

        Userx freshlyCreatedUser = userService.loadUser(username);
        Assertions.assertNotNull(freshlyCreatedUser, "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(username, freshlyCreatedUser.getUsername(), "New user could not be loaded from test data source after being saved");
        Assertions.assertEquals(password, freshlyCreatedUser.getPassword(), "User \"" + username + "\" does not have a the correct password attribute stored being saved");
        Assertions.assertEquals(fName, freshlyCreatedUser.getFirstName(), "User \"" + username + "\" does not have a the correct firstName attribute stored being saved");
        Assertions.assertEquals(lName, freshlyCreatedUser.getLastName(), "User \"" + username + "\" does not have a the correct lastName attribute stored being saved");
        Assertions.assertEquals(email, freshlyCreatedUser.getEmail(), "User \"" + username + "\" does not have a the correct email attribute stored being saved");
        Assertions.assertEquals(phone, freshlyCreatedUser.getPhone(), "User \"" + username + "\" does not have a the correct phone attribute stored being saved");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserRole.GARDENER), "User \"" + username + "\" does not have role GARDENER");
        Assertions.assertTrue(freshlyCreatedUser.getRoles().contains(UserRole.USER), "User \"" + username + "\" does not have role USER");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateUser(), "User \"" + username + "\" does not have a createUser defined after being saved");
        Assertions.assertNotNull(freshlyCreatedUser.getCreateDate(), "User \"" + username + "\" does not have a createDate defined after being saved");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername1() {
        Assertions.assertThrows(org.springframework.orm.jpa.JpaSystemException.class, () -> {
            userService.createUser(null, "test_password");
        });
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername2() {
        Assertions.assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            Userx user = new Userx();
            user.setUsername("");
            userService.createUser(user);
        });
    }

    @Test
    public void testUnauthenticateddLoadUsers() {
        Assertions.assertThrows(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class, () -> {
            for (Userx user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user", authorities = {"GARDENER"})
    public void testUnauthorizedLoadUsers() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            for (Userx user : userService.getAllUsers()) {
                Assertions.fail("Call to userService.getAllUsers should not work without proper authorization");
            }
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"GARDENER"})
    public void testUnauthorizedLoadUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            Userx user = userService.loadUser("admin");
            Assertions.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"ADMIN"})
    public void testAuthorizedLoadUser() {
        userService.createUser("test_user", "test_password");
        Userx testUser = userService.loadUser("test_user");
        Assertions.assertEquals("test_user", testUser.getUsername(), "Call to userService.loadUser returned wrong user");
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"GARDENER"})
    public void testUnauthorizedCreateUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            userService.createUser("test_user", "test_password");
        });
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"GARDENER"})
    public void testUnauthorizedDeleteUser() {
        Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            userService.deleteUser(null);
        });
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetActiveUsers() {
        userService.createUser("user1", "user1pwd");
        userService.createUser("user2", "user2pwd");
        userService.createUser("deletedUser", "deletedUserpwd");
        Userx activeUser1 = userService.loadUser("user1");
        Userx activeUser2 = userService.loadUser("user2");
        Userx deletedUser = userService.loadUser("deletedUser");
        Collection<Userx> allActiveUsers = userService.getAllActiveUsers();
        Assertions.assertTrue(allActiveUsers.contains(activeUser1));
        Assertions.assertTrue(allActiveUsers.contains(activeUser2));
        Assertions.assertTrue(allActiveUsers.contains(deletedUser));
        userService.deleteUser(deletedUser);
        Collection<Userx> allActiveUsers2 = userService.getAllActiveUsers();
        Assertions.assertFalse(allActiveUsers2.contains(deletedUser));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testGetActiveGardeners() {
        userService.createUser("user1", "user1pwd");
        userService.createUser("user2", "user2pwd");
        userService.createUser("deletedUser", "deletedUserpwd");
        Userx activeUser1 = userService.loadUser("user1");
        Userx activeUser2 = userService.loadUser("user2");
        Userx deletedUser = userService.loadUser("deletedUser");
        Collection<Userx> allActiveGardeners = userService.getAllActiveGardeners();
        Assertions.assertFalse(allActiveGardeners.contains(activeUser1));
        Assertions.assertFalse(allActiveGardeners.contains(activeUser2));
        Assertions.assertFalse(allActiveGardeners.contains(deletedUser));
        Set<UserRole> gardenerRoleSet = new HashSet<>();
        gardenerRoleSet.add(UserRole.GARDENER);
        deletedUser.setRoles(gardenerRoleSet);
        userService.saveUser(deletedUser);
        allActiveGardeners = userService.getAllActiveGardeners();
        Assertions.assertTrue(allActiveGardeners.contains(deletedUser));
        userService.deleteUser(deletedUser);
        allActiveGardeners = userService.getAllActiveGardeners();
        Assertions.assertFalse(allActiveGardeners.contains(deletedUser));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAssignRole() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        user1.setPassword("user1pwd");
        userService.createUser(user1);
        userService.assignRole(user1, UserRole.ADMIN);
        Set<UserRole> roles = user1.getRoles();
        Assertions.assertTrue(roles.contains(UserRole.ADMIN));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getAllUsers() {
        Collection<Userx> allUsers = userService.getAllUsers();
        Assertions.assertEquals(6, allUsers.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void updateUserInfo() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        user1.setPassword("user1pwd");
        user1.setFirstName("first");
        user1.setLastName("last");
        user1.setPhone("phone");
        user1.setEmail("email");
        user1 = userService.createUser(user1);
        Assertions.assertEquals("phone", user1.getPhone());
        Assertions.assertEquals("last", user1.getLastName());
        userService.updateUserInfo(user1, null, "firstNew", "lastNew", "emailNew", "phoneNew");
        Assertions.assertEquals("phoneNew", user1.getPhone());
        Assertions.assertEquals("lastNew", user1.getLastName());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void getActiveGardeners() {
        Collection<Userx> activeGardeners = userService.getActiveGardeners();
        Assertions.assertEquals(2, activeGardeners.size());
    }


}