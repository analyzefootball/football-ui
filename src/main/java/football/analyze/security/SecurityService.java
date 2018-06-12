package football.analyze.security;

import football.analyze.provision.Invitation;

/**
 * @author Hassan Mushtaq
 * @since 5/28/18
 */
public interface SecurityService {

    String authenticate(String username, String password);

    Invitation fetchInviteEmail(String invitationId);

    boolean registerUser(String invitationId, User user);
}
