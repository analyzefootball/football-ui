package football.analyze.security;

import football.analyze.common.Entity;
import football.analyze.main.data.play.Prediction;
import football.analyze.main.data.play.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Hassan Mushtaq
 * @since 5/18/18
 */

@Getter
@Setter
public class User extends Entity {

    private String displayName;

    private Role role;

    private String username;

    private String password;

    private Team favoriteTeam;

    private List<Prediction> predictions;

    //For jackson mapper
    private User() {
        //super();
    }

    public User(String displayName, Role role, String username, String password, Team favoriteTeam, List<Prediction> predictions) {
        this.displayName = displayName;
        this.role = role;
        this.username = username;
        this.password = password;
        this.favoriteTeam = favoriteTeam;
        this.predictions = predictions;
    }

    public User(String id, String displayName, Role role, String username, String password, Team favoriteTeam, List<Prediction> predictions) {
        super(id);
        this.displayName = displayName;
        this.role = role;
        this.username = username;
        this.password = password;
        this.favoriteTeam = favoriteTeam;
        this.predictions = predictions;
    }
}
