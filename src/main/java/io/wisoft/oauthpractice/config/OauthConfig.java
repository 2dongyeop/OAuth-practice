package io.wisoft.oauthpractice.config;

import io.wisoft.oauthpractice.persistence.OauthProperties;
import io.wisoft.oauthpractice.application.OauthProvider;
import io.wisoft.oauthpractice.application.OauthAdapter;
import io.wisoft.oauthpractice.persistence.InMemoryProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OauthProperties.class)
public class OauthConfig {

    /**
     * 이래야 프로퍼티 파일에 적어준 정보가 하나의 OauthProperties 객체로 만들어진다.
     * OauthProvider.java로 ㄱㄱ
     */

    private final OauthProperties properties;


    /**
     * InMemoryProviderRepository 다음에 추가된 부분
     * OauthConfig에서 빈으로 등록된 OauthProperties를 주입받아 OauthAdapter를 사용해
     * 각 OAuth 서버 정보를 가진 OauthProvider로 분해하여 InMemoryProviderRepository에 저장
     */
    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository(providers);
    }
}