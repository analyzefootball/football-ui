package football.analyze.main.security;

import org.springframework.stereotype.Service;

/**
 * @author Hassan Mushtaq
 * @since 5/28/18
 */
@Service
public class DefaultSecurityService implements SecurityService {
    @Override
    public User authenticate(String username, String password) {
        return null;
    }
}
