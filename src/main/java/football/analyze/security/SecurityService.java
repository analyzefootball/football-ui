package football.analyze.security;

/**
 * @author Hassan Mushtaq
 * @since 5/28/18
 */
public interface SecurityService {

    String authenticate(String username, String password);

}
