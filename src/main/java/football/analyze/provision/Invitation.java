package football.analyze.provision;

import football.analyze.common.Entity;
import football.analyze.security.Role;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hassan Mushtaq
 * @since 6/6/18
 */
@Getter
@Setter
public class Invitation extends Entity {

    private String email;

    private Role role;

    //For jackson mapper
    private Invitation() {
    }

    public Invitation(String email, Role role) {
        this.email = email;
        this.role = role;
    }
}
