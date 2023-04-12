package io.wisoft.oauthpractice.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "oauth2")
public class OauthProperties {
    /**
     * 프로퍼티 파일에 적어준 정보들을 객체로 바인딩
     * 프로퍼티 파일의 구조대로 존재하는 값들을 아래와 같이
     * static class의 필드로 두면 값을 바인딩 받을 수 있는 상태가 된다.
     * ex) oauth2 하위에 크게 user와 provider가 존재
     *
     * 여기까지는 값을 바인딩할 수 있는 상태로 만들었을 뿐, 사용하려면 등록해야 한다.
     * -> OauthConfig.java로 ㄱㄱ
     */
    private final Map<String, User> user = new HashMap<>();

    private final Map<String, Provider> provider = new HashMap<>();

    @Getter @Setter
    public static class User {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
    }

    @Getter @Setter
    public static class Provider {
        private String tokenUri;
        private String userInfoUri;
        private String userNameAttribute;
    }
}