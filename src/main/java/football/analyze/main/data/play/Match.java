package football.analyze.main.data.play;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Hassan Mushtaq
 * @since 5/25/18
 */
@Getter
public class Match {

    @Setter
    private Team homeTeam;

    @Setter
    private Team awayTeam;

    private LocalDateTime dateTime;

    private Integer matchNumber;

    private MatchType matchType;

    @Setter
    private Integer homeTeamScore;

    @Setter
    private Integer awayTeamScore;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' HH:mm z");

    private Match() {
    }

    public Match(Team homeTeam, Team awayTeam, LocalDateTime dateTime, Integer matchNumber, MatchType matchType) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.dateTime = dateTime;
        this.matchNumber = matchNumber;
        this.matchType = matchType;
    }

    public String getDateTimeFormatted(ZoneId zoneId) {
        ZonedDateTime actualMatchTime = getDateTime().atZone(TimeZone.getDefault().toZoneId());
        ZonedDateTime userLocaleMatchTime = actualMatchTime.withZoneSameInstant(zoneId);
        return dateTimeFormatter.format(userLocaleMatchTime);
    }

    public boolean isMatchStarted() {
        return LocalDateTime.now().isAfter(dateTime);
    }
}
