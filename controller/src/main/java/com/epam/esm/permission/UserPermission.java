package com.epam.esm.permission;

import org.springframework.http.HttpMethod;
import java.util.Map;

public enum UserPermission {

    GUEST(Map.of(HttpMethod.GET, new String[]{"/certificates/tag/**", "/certificates/certificate/**"},
            HttpMethod.POST, new String[]{"/certificates/login", "/certificates/signup"})),

    CLIENT(Map.of(
            HttpMethod.GET, new String[]{"/certificates/orders/**", "/certificates/orders/user/**" },
            HttpMethod.POST, new String[]{"/certificates/order"})),
    ADMIN(Map.of(
            HttpMethod.PATCH, new String[]{"/certificates/certificate/**"},
            HttpMethod.PUT, new String[]{"/certificates/certificate/**"},
            HttpMethod.POST, new String[]{"/certificates/create"},
            HttpMethod.DELETE, new String[]{"/certificates/tag/**", "/certificates/certificate/**"}));

    private final Map<HttpMethod, String[]> permissions;

    UserPermission(Map<HttpMethod, String[]> permissions){
        this.permissions = permissions;
    }

    public Map<HttpMethod, String[]> getPermissions() {
        return permissions;
    }
}
