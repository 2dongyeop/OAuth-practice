package io.wisoft.oauthpractice.web.dto;

import io.wisoft.oauthpractice.persistence.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private Role role;
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(
            final Long id,
            final String name,
            final String email,
            final String imageUrl,
            final Role role,
            final String tokenType,
            final String accessToken,
            final String refreshToken) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}