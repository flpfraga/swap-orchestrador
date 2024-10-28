package swap.orchestrador.client;


import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GithubClient {

    private static final String API_KEY_HEADER = "Bearer ";
    private static final Logger LOG = LoggerFactory.getLogger(GithubClient.class);

    @Value("${app.client.github.token}")
    private String apiKey;


    private final RestTemplate gitHubRestTemplate;

    public GithubClient(final RestTemplate gitHubRestTemplate) {
        this.gitHubRestTemplate = gitHubRestTemplate;
    }


    @CircuitBreaker(name = "InfoGithub", fallbackMethod = "callClientFallback")
    public <T> Optional<T> callGithubInfo(String url, Class<T> responseType) {
        try {
            final ResponseEntity<T> response = call(url, responseType);
            return Optional.ofNullable(response.getBody());
        } catch (final Exception ex) {
            LOG.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private <T> ResponseEntity<T> call(String url, Class<T> responseType) {
        gitHubRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        final ResponseEntity<T> response = gitHubRestTemplate.exchange(
                buildRepositoryInfoRequest(url),
                responseType
                );
        if (isResponseFail(response)) {
            throw new RuntimeException("Github Client not sucesso." + url);
        }
        return response;
    }

    @CircuitBreaker(name = "InfoGithubPageable", fallbackMethod = "callClientFallback")
    public <T> Optional<List<T>> callGithubInfoPageable(String url, Class<T[]> responseType) {
        List<T> githubInfos = new ArrayList<>();
        boolean hasBody = true;
        int page = 1;
        while (Boolean.TRUE.equals(hasBody)) {
            try {
                String pageableUrl = url + "?page=" + page++;
                final ResponseEntity<T[]> response = call(pageableUrl, responseType);
                if (Boolean.TRUE.equals(ObjectUtils.isEmpty(response.getBody()))) {
                    hasBody = false;
                }
                else{
                    githubInfos.addAll(List.of(Objects.requireNonNull(response.getBody())));
                }
            } catch (final Exception ex) {
                LOG.error(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
        return Optional.of(githubInfos);

    }

    public List<?> callClientFallback(String user, String repository, final CallNotPermittedException ex) {
        LOG.error("Fall back client Github. " + ex.getMessage());
        throw new RestClientException("Github Client Call Fallback. " + user + " - " + repository + " - " + ex);
    }

    private RequestEntity<Void> buildRepositoryInfoRequest(String url) {
        return RequestEntity
                .get(url)
                .header("Authorization", API_KEY_HEADER + apiKey)
                .build();
    }

    private RequestEntity<Void> buildRepositoryInfoRequest(String url, Integer page) {
        return RequestEntity
                .get(url + "?page=" + page)
                .header("Authorization", API_KEY_HEADER + apiKey)
                .build();
    }

    private boolean isResponseFail(final ResponseEntity<?> response) {
        return response == null || !response.getStatusCode().is2xxSuccessful();
    }

}
