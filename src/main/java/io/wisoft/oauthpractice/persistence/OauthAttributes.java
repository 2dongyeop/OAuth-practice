package io.wisoft.oauthpractice.persistence;

import io.wisoft.oauthpractice.persistence.UserProfile;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {
    /**
     *  각 OAuth 서버 별로 데이터의 key값이 다르다.
     *  예를 들어 github의 프로필 이미지는 avatar_url이지만 google의 경우는 picture이다.
     *  따라서 OAuth 서버가 어떤 형식으로 데이터를 리턴하는지 확인해보고 추가해줘야한다.
     */

    GITHUB("github") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            return UserProfile.builder()
                    .oauthId(String.valueOf(attributes.get("id")))
                    .email((String) attributes.get("email"))
                    .name((String) attributes.get("name"))
                    .imageUrl((String) attributes.get("avatar_url"))
                    .build();
        }
    },
    NAVER("naver") {
        @Override
        public UserProfile of(Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return UserProfile.builder()
                    .oauthId((String) response.get("id"))
                    .email((String) response.get("email"))
                    .name((String) response.get("name"))
                    .imageUrl((String) response.get("profile_image"))
                    .build();
        }
    },
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
