package io.wisoft.oauthpractice.persistence;


import lombok.Builder;
import lombok.Getter;


@Getter
public class UserProfile {

    /**
     * OAuth 서버 별로 가져올 수 있는 유저 정보가 다르다.
     * oauthId, email, name, imageUrl 정도만 DTO를 이용해 가져와보자!
     */

    private final String oauthId;
    private final String email;
    private final String name;
    private final String imageUrl;

    @Builder
    public UserProfile(final String oauthId, final String email, final String name, final String imageUrl) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Member toMember() {
        return Member.builder()
                .oauthId(oauthId)
                .email(email)
                .name(name)
                .imageUrl(imageUrl)
                .role(Role.GUEST)
                .build();
    }
}
