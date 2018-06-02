package football.analyze.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hassan Mushtaq
 * @since 5/19/18
 */
@Getter
public class Credentials {

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Setter
    private String jwtToken;

    //For Jackson Mapper
    private Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
