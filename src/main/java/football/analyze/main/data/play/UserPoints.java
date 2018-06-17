package football.analyze.main.data.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hassan Mushtaq
 * @since 6/14/18
 */
@Setter
@Getter
@NoArgsConstructor
public class UserPoints {

    private String user;

    private Integer roundOneCorrectResult = 0;

    private Integer roundOneBonus = 0;

    private Integer roundSecondCorrectResult = 0;

    private Integer roundSecondBonus = 0;

    private Integer quarterCorrectResult = 0;

    private Integer quarterBonus = 0;

    private Integer semiCorrectResult = 0;

    private Integer semiBonus = 0;

    private Integer thirdCorrectResult = 0;

    private Integer thirdBonus = 0;

    private Integer finalCorrectResult = 0;

    private Integer finalBonus = 0;

    private Integer total;
}
