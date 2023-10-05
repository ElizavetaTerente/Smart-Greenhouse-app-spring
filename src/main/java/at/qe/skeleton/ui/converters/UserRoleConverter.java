package at.qe.skeleton.ui.converters;

import at.qe.skeleton.model.UserRole;
import org.springframework.context.annotation.Scope;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;

@Named
@Scope("application")
@FacesConverter(value = "userRoleConverter", managed = true)
public class UserRoleConverter implements Converter<UserRole> {

    @Override
    public UserRole getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            switch (value) {
                case "Admin":
                    return UserRole.ADMIN;
                case "Gardener":
                    return UserRole.GARDENER;
                default:
                    return UserRole.USER;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, UserRole value) {
        if (value != null) {
            return switch (value) {
                case ADMIN -> "Admin";
                case GARDENER -> "Gardener";
                default -> "User";
            };
        }
        return null;
    }
}