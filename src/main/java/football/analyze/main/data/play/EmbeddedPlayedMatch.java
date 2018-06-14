package football.analyze.main.data.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Hassan Mushtaq
 * @since 6/14/18
 */
@Getter
@Setter
@NoArgsConstructor
public class EmbeddedPlayedMatch {

    private List<PlayedMatch> playedMatchList;
}
