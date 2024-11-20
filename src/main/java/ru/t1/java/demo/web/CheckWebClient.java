package ru.t1.java.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.t1.java.demo.dto.CheckRequest;
import ru.t1.java.demo.dto.CheckResponse;
import ru.t1.java.demo.dto.JwtResponse;
import ru.t1.java.demo.dto.LoginRequest;

import java.util.Optional;

@Slf4j
public class CheckWebClient extends BaseWebClient {

    @Value("${integration_service_2.resource-check}")
    private String resourceCheck;
    @Value("${integration_service_2.resource-login}")
    private String resourceLogin;
    @Value("${integration_service_2.user}")
    private String user;
    @Value("${integration_service_2.password}")
    private String password;


    public CheckWebClient(WebClient webClient) {
        super(webClient);
    }

    public Optional<CheckResponse> check(Long id) {
        log.debug("Старт запроса с id {}", id);
        ResponseEntity<JwtResponse> postLogin = null;
        ResponseEntity<CheckResponse> post = null;
        try {
            LoginRequest loginRequest = LoginRequest.builder()
                    .username(user)
                    .password(password)
                    .build();

            postLogin = this.post(
                    uriBuilder -> uriBuilder.path(resourceLogin).build(),
                    loginRequest,
                    JwtResponse.class
            );

            if (postLogin.getStatusCode().is2xxSuccessful()) {
                CheckRequest request = CheckRequest.builder()
                        .clientId(id)
                        .build();

                post = this.post(
                        uriBuilder -> uriBuilder.path(resourceCheck).build(),
                        request,
                        CheckResponse.class);
            }

        } catch (Exception httpStatusException) {
            throw httpStatusException;
        }

        log.debug("Финиш запроса с id {}", id);
        return Optional.ofNullable(post.getBody());
    }
}
