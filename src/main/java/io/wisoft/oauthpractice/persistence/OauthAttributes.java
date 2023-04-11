package io.wisoft.oauthpractice.persistence;

import io.wisoft.oauthpractice.persistence.UserProfile;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {

    GOOGLE("google") {
        @Override
        public UserProfile of(final Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("sub")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("picture"))
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(final String name) {
        this.providerName = name;
    }

    public static UserProfile extract(final String providerName, final Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract UserProfile of(final Map<String, Object> attributes);
}
