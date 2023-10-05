package at.qe.skeleton.tests;

import at.qe.skeleton.ui.converters.UserRoleConverter;
import at.qe.skeleton.model.UserRole;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
@WebAppConfiguration
class UserRoleConverterTest {

    private final FacesContext context = mock(FacesContext.class);
    private final UIComponent component = mock(UIComponent.class);
    private final UserRoleConverter converter = new UserRoleConverter();

    @Test
    void getAsObject() {
        assertEquals(UserRole.ADMIN, converter.getAsObject(context, component, "Admin"));
        assertEquals(UserRole.GARDENER, converter.getAsObject(context, component, "Gardener"));
        assertEquals(UserRole.USER, converter.getAsObject(context, component, "User"));
        assertEquals(UserRole.USER, converter.getAsObject(context, component, "UndefinedRole"));
        assertNull(converter.getAsObject(context, component, null));
        assertNull(converter.getAsObject(context, component, ""));
    }

    @Test
    void getAsString() {
        assertEquals("Admin", converter.getAsString(context, component, UserRole.ADMIN));
        assertEquals("Gardener", converter.getAsString(context, component, UserRole.GARDENER));
        assertEquals("User", converter.getAsString(context, component, UserRole.USER));
        assertNull(converter.getAsString(context, component, null));
    }
}
