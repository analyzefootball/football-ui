package football.analyze.main.data.points;

import football.analyze.main.data.play.PointsTable;
import football.analyze.main.data.play.UserPoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static football.analyze.security.SecurityConstants.HEADER_STRING;

/**
 * @author Hassan Mushtaq
 * @since 6/3/18
 */
@Service
@Slf4j
public class PointsLoader {

    private final RestTemplate restTemplate;

    private final String pointsUrl;

    public PointsLoader(RestTemplate restTemplate,
                        @Value("${football.service.endpoints.points}") String pointsUrl1) {
        this.restTemplate = restTemplate;
        this.pointsUrl = pointsUrl1;
    }

    public List<UserPoints> getPoints(String tokenWithBearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_STRING, tokenWithBearer);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<PointsTable> responseEntity = restTemplate.exchange(pointsUrl,
                HttpMethod.GET, entity, PointsTable.class);
        return responseEntity.getBody().getUserPoints();
    }
}
