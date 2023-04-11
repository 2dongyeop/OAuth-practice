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

    private final OauthProperties properties;

    @Bean
    public InMemoryProviderRepository inMemoryProviderRepository() {
        Map<String, OauthProvider> providers = OauthAdapter.getOauthProviders(properties);
        return new InMemoryProviderRepository(providers);
    }
}