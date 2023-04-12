# OAuth-practice
Spring Security를 사용하지 않고 OAuth를 구현하는 프로젝트입니다.

<br/>

### 사전 설명
- secret-key가 담긴 파일은 올리지 않습니다.
  - 하단에 gitignore된 파일 예시를 보여줍니다.
- OAuth에 대한 사전 지식을 필요로 합니다.
  - [참고 블로그](https://velog.io/@max9106/OAuth) 


<br/>

### 동작 방식

1. 애플리케이션 실행 후 localhost:8080으로 접속
2. `Google`, `Github`, `Naver` 중 원하는 방식을 클릭 후 로그인
3. White Label Page를 무시하고, 주소창에 생긴 code 값을 가져온다. 

→ ex) localhost:8080/redirect/oauth?code=***{code}***

4. Postman을 이용해 이 코드를 백엔드 서버로 보내보자.
5. `GET` http://localhost:8080/login/oauth/{provider}?code={위에서받은코드}
6. Response로 받은 정보를 확인

<br/>

#### `application-oauth.yml`
```yaml
oauth2:
  user:
    google:
      client-id: { client-id }
      client-secret: { secret-key }
      redirect-uri: http://localhost:8080/redirect/oauth
    github:
      client-id: { client-id }
      client-secret: { secret-key }
      redirect-uri: http://localhost:8080/redirect/oauth
    naver:
      client-id: { client-id }
      client-secret: { secret-key }
      redirect-uri: http://localhost:8080/redirect/oauth
      
  provider:
    github:
      token-uri: https://github.com/login/oauth/access_token
      user-info-uri: https://api.github.com/user
    google:
      token-uri: https://www.googleapis.com/oauth2/v4/token
      user-info-uri: https://www.googleapis.com/oauth2/v3/userinfo
    naver:
      token-uri: https://nid.naver.com/oauth2.0/token
      user-info-uri: https://openapi.naver.com/v1/nid/me
```

<br/>

#### `application-jwt.yml`
```yaml
jwt:
  token:
    secret-key: { secret }

  refresh-token:
    expire-length: { time }

  access-token:
    expire-length: { time }
```
