package io.wisoft.oauthpractice.web;

import io.wisoft.oauthpractice.web.dto.LoginResponse;
import io.wisoft.oauthpractice.application.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OauthRestController {

    private final OauthService oauthService;

    /**
     * provider 이름과, authorization code를 받아서 실제로 로그인을 실행할 것이다.
     * 실제 로직은 service 단에서 처리하도록 하자.
     */

    @GetMapping("/login/oauth/{provider}")
    public ResponseEntity<LoginResponse> login(
            final @PathVariable String provider, final @RequestParam String code) {

        LoginResponse loginResponse = oauthService.login(provider, code);
        return ResponseEntity.ok().body(loginResponse);
    }
}