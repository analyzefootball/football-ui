package football.analyze.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static football.analyze.security.SecurityConstants.HEADER_STRING;

/**
 * @author Hassan Mushtaq
 * @since 5/28/18
 */
@Slf4j
@Service
public class DefaultSecurityService implements SecurityService {

    private final RestTemplate restTemplate;

    private final String loginURL;

    public DefaultSecurityService(RestTemplate restTemplate,
                                  @Value("${football.service.endpoints.login}") String loginURL) {
        this.restTemplate = restTemplate;
        this.loginURL = loginURL;
    }

    @Override
    public String authenticate(String username, String password) {
        try {
            String input = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(input);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(loginURL, entity, String.class);
            HttpHeaders headers = responseEntity.getHeaders();
            return headers.getFirst(HEADER_STRING);
        } catch (Exception e) {
            log.error("Login error", e);
            return null;
        }
    }
}
