package ru.t1.java.demo.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import ru.t1.java.demo.dto.CheckResponse;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CheckWebClientIntegrationTest {
    @Value("${integration-test.resource-check}")
    private String resourceCheck;
    @Value("${integration-test.resource-login}")
    private String resourceLogin;
    @Value("${integration-test.url}")
    private String url;
    @Value("${integration-test.user}")
    private String user;
    @Value("${integration-test.password}")
    private String password;
    @Value("${integration-test.retry-count}")
    private Integer retryCount;
    @Value("${integration-test.retry-backoff}")
    private Integer retryBackoff;

    @Autowired
    CheckClientConfig.ClientHttp clientHttp;

    private CheckWebClient checkWebClient;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        WebClient.Builder webClient = WebClient.builder();
        webClient
                .baseUrl(url)
                .clientConnector(clientHttp.getClientHttp(CheckWebClient.class.getName()));

        checkWebClient = new CheckWebClient(webClient.build());

        Field fieldResourceCheck = CheckWebClient.class.getDeclaredField("resourceCheck");
        fieldResourceCheck.setAccessible(true);
        fieldResourceCheck.set(checkWebClient, resourceCheck);

        Field fieldResourceLogin = CheckWebClient.class.getDeclaredField("resourceLogin");
        fieldResourceLogin.setAccessible(true);
        fieldResourceLogin.set(checkWebClient, resourceLogin);

        Field fieldUser = CheckWebClient.class.getDeclaredField("user");
        fieldUser.setAccessible(true);
        fieldUser.set(checkWebClient, user);

        Field fieldPassword = CheckWebClient.class.getDeclaredField("password");
        fieldPassword.setAccessible(true);
        fieldPassword.set(checkWebClient, password);

        Field fieldRetryCount = CheckWebClient.class.getSuperclass().getDeclaredField("retryCount");
        fieldRetryCount.setAccessible(true);
        fieldRetryCount.set(checkWebClient, retryCount);

        Field fieldRetryBackoff = CheckWebClient.class.getSuperclass().getDeclaredField("retryBackoff");
        fieldRetryBackoff.setAccessible(true);
        fieldRetryBackoff.set(checkWebClient, retryBackoff);
    }

    @Test
    public void testCheckSuccessfulResponse() {
        Optional<CheckResponse> response = checkWebClient.check(1L);
        assertThat(response).isPresent();
        assertThat(response.get().getBlocked()).isEqualTo(false);
    }
}
