package io.wisoft.oauthpractice.application;

import io.wisoft.oauthpractice.persistence.OauthAttributes;
import io.wisoft.oauthpractice.jwt.JwtTokenProvider;
import io.wisoft.oauthpractice.persistence.InMemoryProviderRepository;
import io.wisoft.oauthpractice.persistence.Member;
import io.wisoft.oauthpractice.persistence.MemberRepository;
import io.wisoft.oauthpractice.persistence.UserProfile;
import io.wisoft.oauthpractice.web.dto.LoginResponse;
import io.wisoft.oauthpractice.web.dto.OauthTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final InMemoryProviderRepository inMemoryProviderRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * OAuth 로그인 시 할 일은 크게 2가지이다.
     * 1. 프론트에서 받은 authorizatoin code를 통해 OAuth 서버의 access token을 얻어오는 것
     * 2. access token을 통해 실제 유저 정보를 얻어오는 것
     */

    public LoginResponse login(final String providerName, final String code) {
        // 프론트에서 넘어온 provider 이름을 통해 InMemoryProviderRepository에서 OauthProvider 가져오기
        // access token을 가져오기 위해서는 OauthProvider가 필요!
        OauthProvider provider = inMemoryProviderRepository.findByProviderName(providerName);

        //access token을 가져오거나, 유저 정보를 가져올 때는 실제로 OAuth 서버와 통신을 해야한다.
        // WebClient를 사용하여 통신을 하려고한다. 아래의 의존성을 추가해주자.
        // build.gradle -> `implementation 'org.springframework.boot:spring-boot-starter-webflux'`

        // TODO access token 가져오기
        // 아래의 OauthTokenResponse는 OAuth 서버와의 통신을 통해 access token을 받아올 dto
        OauthTokenResponse tokenResponse = getToken(code, provider);

        // TODO 유저 정보 가져오기
        // OAuth 서버에 WebClient를 통해 유저 정보를 요청하고 map으로 받아온다. -> getUserAttributes
        // Bearer 타입으로 Auth 헤더에 access token 값을 담아주면 된다. -> getUserProfile
        UserProfile userProfile = getUserProfile(providerName, tokenResponse, provider);

        // TODO 유저 DB에 저장
        Member member = saveOrUpdate(userProfile);

        // 우리 애플리케이션의 JWT 토큰 만들기
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // TODO 레디스에 refresh token 저장
        // redisUtil.setData(String.valueOf(member.getId()), refreshToken);

        return LoginResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .role(member.getRole())
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    private Member saveOrUpdate(final UserProfile userProfile) {
        Member member = memberRepository.findByOauthId(userProfile.getOauthId())
                .map(entity -> entity.update(
                        userProfile.getEmail(), userProfile.getName(), userProfile.getImageUrl()))
                .orElseGet(userProfile::toMember);
        return memberRepository.save(member);
    }

    private UserProfile getUserProfile(final String providerName, final OauthTokenResponse tokenResponse, final OauthProvider provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        // TODO 유저 정보(map)를 통해 UserProfile 만들기
        return OauthAttributes.extract(providerName, userAttributes);
    }

    // OAuth 서버에서 유저 정보 map으로 가져오기
    private Map<String, Object> getUserAttributes(final OauthProvider provider, final OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(provider.getUserInfoUrl())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    private OauthTokenResponse getToken(final String code, final OauthProvider provider) {
        /**
         * 이제 WebClient를 사용해 OAuth 서버에 access token 요청을 하면 된다.
         * 프로퍼티 파일에 적어줬던 access token을 요청할 수 있는 uri에 요청을 보내면 된다.
         * 이 때 헤더에 client-id와 client-secret값으로 Basic Auth를 추가해주고,
         *       컨텐츠 타입을 APPLICATION_FORM_URLENCODED로 설정해준다.
         * 요청 바디에는 authorization code, redirect_uri 등을 넘겨주면 된다.
         */

        return WebClient.create()
                .post()
                .uri(provider.getTokenUrl())
                .headers(header -> {
                    header.setBasicAuth(provider.getClientId(), provider.getClientSecret());
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(final String code, final OauthProvider provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUrl());
        return formData;
    }
}
