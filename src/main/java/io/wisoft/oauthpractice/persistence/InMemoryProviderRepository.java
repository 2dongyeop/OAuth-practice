package io.wisoft.oauthpractice.persistence;

import io.wisoft.oauthpractice.application.OauthProvider;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProviderRepository {

    /**
     *  OauthProvider를 저장해 줄 인메모리 저장소.
     *
     *  다시 OauthConfig로 가자.
     */

    private final Map<String, OauthProvider> providers;

    public InMemoryProviderRepository(final Map<String, OauthProvider> providers) {
        this.providers = new HashMap<>(providers);
    }

    public OauthProvider findByProviderName(final String name) {
        return providers.get(name);
    }
}
