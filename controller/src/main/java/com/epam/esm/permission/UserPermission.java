package com.epam.esm.permission;

import org.springframework.http.HttpMethod;
import java.util.Map;

/**
 * The enum User permission.
 */
public enum UserPermission {

    /**
     * The Guest.
     */
    GUEST(Map.of(HttpMethod.GET, new String[]{"/certificates/tag/**", "/certificates/certificate/**", "/certificates/all"},
            HttpMethod.POST, new String[]{"/certificates/login", "/certificates/signup"})),

    /**
     * The Client.
     */
    CLIENT(Map.of(
            HttpMethod.GET, new String[]{"/certificates/orders/**", "/certificates/orders/user/**" },
            HttpMethod.POST, new String[]{"/certificates/order"})),
    /**
     * The Admin.
     */
    ADMIN(Map.of(
            HttpMethod.PATCH, new String[]{"/certificates/certificate/**"},
            HttpMethod.PUT, new String[]{"/certificates/certificate/**"},
            HttpMethod.POST, new String[]{"/certificates/create"},
            HttpMethod.DELETE, new String[]{"/certificates/tag/**", "/certificates/certificate/**"}));

    private final Map<HttpMethod, String[]> permissions;

    UserPermission(Map<HttpMethod, String[]> permissions){
        this.permissions = permissions;
    }

    /**
     * Gets permissions.
     *
     * @return the permissions
     */
    public Map<HttpMethod, String[]> getPermissions() {
        return permissions;
    }
}
