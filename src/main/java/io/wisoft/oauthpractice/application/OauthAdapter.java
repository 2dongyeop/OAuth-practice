package io.wisoft.oauthpractice.application;

import io.wisoft.oauthpractice.persistence.OauthProperties;

import java.util.HashMap;
import java.util.Map;

public class OauthAdapter {
    private OauthAdapter() {}

    /**
     * OauthProperties를 OauthProvider로 변환해준다.
     *
     * 스프링 시큐리티에선 OAuth2ClientPropertiesRegistrationAdapter를 통해
     * OAuth2ClientProperties를 ClientRegistration로 바꿔주는 것에 해당!
     *
     * InMemoryProviderRepository.java로 이동
     */
    public static Map<String, OauthProvider> getOauthProviders(OauthProperties properties) {
        Map<String, OauthProvider> oauthProvider = new HashMap<>();

        properties.getUser().forEach((key, value) -> oauthProvider.put(key, new OauthProvider(value, properties.getProvider().get(key))));
        return oauthProvider;
    }
}
