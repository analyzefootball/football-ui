package football.analyze.main.data.schedule;

import football.analyze.main.data.play.Match;
import football.analyze.main.data.play.Tournament;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

import static football.analyze.security.SecurityConstants.HEADER_STRING;

/**
 * @author Hassan Mushtaq
 * @since 6/3/18
 */
@Service
@Slf4j
public class ScheduleLoader {

    private final RestTemplate restTemplate;

    private final String scheduleUrl;

    public ScheduleLoader(RestTemplate restTemplate,
                          @Value("${football.service.endpoints.tournament}") String scheduleUrl) {
        this.restTemplate = restTemplate;
        this.scheduleUrl = scheduleUrl;
    }

    public List<Match> getMatches(String tokenWithBearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_STRING, tokenWithBearer);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Tournament> responseEntity = restTemplate.exchange(scheduleUrl + "/Fifa 2018 World Cup",
                HttpMethod.GET, entity, Tournament.class);
        List<Match> matches = responseEntity.getBody().getSchedule().getMatches();
        matches.sort(Comparator.comparing(Match::getMatchNumber));
        return matches;
    }
}
