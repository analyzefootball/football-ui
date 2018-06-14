package football.analyze.security;

import football.analyze.provision.Invitation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final String invitationURL;

    private final String signupURL;

    public DefaultSecurityService(RestTemplate restTemplate,
                                  @Value("${football.service.endpoints.login}") String loginURL,
                                  @Value("${football.service.endpoints.invitation}") String invitationURL,
                                  @Value("${football.service.endpoints.register}") String signupURL) {
        this.restTemplate = restTemplate;
        this.loginURL = loginURL;
        this.invitationURL = invitationURL;
        this.signupURL = signupURL;
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

    @Override
    public Invitation fetchInviteEmail(String invitationId) {
        try {
            ResponseEntity<Invitation> responseEntity = restTemplate.getForEntity(invitationURL + "/" + invitationId, Invitation.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Invitation error", e);
            return null;
        }
    }

    @Override
    public boolean registerUser(String invitationId, User user) {
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<Void> response = restTemplate.postForEntity(signupURL + "/" + invitationId, entity, Void.class);
        return response.getStatusCode().equals(HttpStatus.CREATED);
    }

    @Override
    public boolean sendInvite(Invitation invitation, String tokenWithBearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_STRING, tokenWithBearer);
        HttpEntity<Invitation> entity = new HttpEntity<>(invitation, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(invitationURL, entity, Void.class);
        return response.getStatusCode().equals(HttpStatus.CREATED);
    }
}
