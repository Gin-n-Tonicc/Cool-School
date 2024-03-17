package com.coolSchool.coolSchool.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration representing OAuth2 providers available in the application.
 * This class is used to define and access different OAuth2 providers supported for authentication.
 * Providers - Local and Google
 */
@Getter
public enum Provider {
    LOCAL("local"),
    GOOGLE("google");

    private static final Map<String, Provider> providerMap = new HashMap<>();

    static {
        for (Provider provider : values()) {
            providerMap.put(provider.getRegistrationId(), provider);
        }
    }

    private final String registrationId;

    Provider(String registrationId) {
        this.registrationId = registrationId;
    }

    public static Provider getProvider(String registrationId) {
        return providerMap.getOrDefault(registrationId, Provider.LOCAL);
    }
}
