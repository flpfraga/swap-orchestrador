package swap.orchestrador.client;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubClientTest {

    @Mock
    private Logger logMock;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubClient githubClient;

    @Value("${app.client.github.endpoint}")
    private static final String ENDPOINT = "https://api.github.com";

    @Value("${app.client.github.token}")
    private static final String API_KEY = "fake-api-key";

    @Value("${app.client.github.repository-info.path}")
    private static final String REPOSITORY_INFO_PATH = "repos";

    @Value("${app.client.github.issue.path}")
    private static final String ISSUE_PATH = "issues";

    @BeforeEach
    void setUp() {
        this.githubClient = new GithubClient(restTemplate);
    }

//    @Test
//    void testCallGithubInfoSuccessfulResponse() {
//        List<String> issuesPage1 = Collections.singletonList("issue1");
//        ResponseEntity<List> responsePage1 = new ResponseEntity<>(issuesPage1, HttpStatus.OK);
//
//        ResponseEntity<List> emptyResponse = new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
//
//        when(restTemplate.exchange(any(RequestEntity.class), eq(List.class)))
//                .thenReturn(responsePage1)
//                .thenReturn(emptyResponse);
//
//        List<?> result = githubClient.callGithubInfo("user");
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals("issue1", result.get(0));
//
//        verify(restTemplate, times(2)).exchange(any(RequestEntity.class), eq(List.class));
//    }

    @Test
    void testCallGithubInfoResponseFail() {
        ResponseEntity<List> response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(any(RequestEntity.class), eq(List.class)))
                .thenReturn(response);
//
//        List<?> result = githubClient.callGithubInfo("user", "repository");
//
//        assertNull(result);
    }

    @Test
    void testCallGithubInfo_ExceptionHandling() {
        // Configurando mock para simular uma exceção
        when(restTemplate.exchange(any(RequestEntity.class), eq(List.class)))
                .thenThrow(new RuntimeException("Simulated error"));

//        Exception exception = assertThrows(RuntimeException.class, () ->
//                githubClient.callGithubInfo("user", "repository"));
//
//        assertEquals("java.lang.RuntimeException: Simulated error", exception.getMessage());
    }

    @Test
    void testFallbackMethod() {
        // Mock do CircuitBreaker e sua configuração
        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
        CircuitBreakerConfig config = mock(CircuitBreakerConfig.class);

        // Configura o CircuitBreaker para retornar o config mockado
        when(circuitBreaker.getCircuitBreakerConfig()).thenReturn(config);
        when(config.isWritableStackTraceEnabled()).thenReturn(true);  // Configura para evitar o erro de null

        // Cria a exceção usando o CircuitBreaker mockado
        CallNotPermittedException exception = CallNotPermittedException.createCallNotPermittedException(circuitBreaker);

        // Verifica se a exceção RestClientException é lançada
        RestClientException thrownException = assertThrows(RestClientException.class, () -> {
            githubClient.callClientFallback("user", "repository", exception);
        });

        // Verifica se a mensagem de erro contém o texto esperado
        assertTrue(thrownException.getMessage().contains("Github client error"));
    }

//    @Test
//    void testCallClientFallback_LogsErrorMessage() throws Exception {
//        // Mock do CircuitBreaker e sua configuração
//        CircuitBreaker circuitBreaker = mock(CircuitBreaker.class);
//        CircuitBreakerConfig config = mock(CircuitBreakerConfig.class);
//
//        // Configura o CircuitBreaker para retornar o config mockado
//        when(circuitBreaker.getCircuitBreakerConfig()).thenReturn(config);
//        when(config.isWritableStackTraceEnabled()).thenReturn(true);
//
//        // Cria a exceção usando o CircuitBreaker mockado
//        CallNotPermittedException exception = CallNotPermittedException.createCallNotPermittedException(circuitBreaker);
//
//        // Captura a saída do log de erro
//        String logOutput = tapSystemErr(() -> {
//            assertThrows(RestClientException.class, () -> {
//                githubClient.callClientFallback("user", "repository", exception);
//            });
//        });
//
//        // Verifica se a mensagem de erro está presente na saída capturada
//        assertTrue(logOutput.contains("Fall back client Github"), "A mensagem de log esperada não foi encontrada.");
//    }
}

