package football.analyze.main.security;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Hassan Mushtaq
 * @since 5/28/18
 */
@Service
public class DefaultSecurityService implements SecurityService {

    private final RestTemplate restTemplate;

    public DefaultSecurityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public User authenticate(String username, String password) {
        return null;
    }
}
