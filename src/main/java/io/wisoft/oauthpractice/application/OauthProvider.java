package io.wisoft.oauthpractice.application;

import io.wisoft.oauthpractice.persistence.OauthProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OauthProvider {

    /**
     * 저장소에 저장하기에 앞서 OauthProperties를 분해해야 한다.
     * 스프링 시큐리티로 보면 ClientRegistration 객체를 만들어 주는 것에 해당!
     *
     * OauthAdapter.java로 가보자.
     */
    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;
    private final String tokenUrl;
    private final String userInfoUrl;

    public OauthProvider(OauthProperties.User user, OauthProperties.Provider provider) {
        this(user.getClientId(), user.getClientSecret(), user.getRedirectUri(), provider.getTokenUri(), provider.getUserInfoUri());
    }

    @Builder
    public OauthProvider(String clientId, String clientSecret, String redirectUrl, String tokenUrl, String userInfoUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
    }
}