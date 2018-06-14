package football.analyze.main.data.prediction;

import football.analyze.main.data.play.Match;
import football.analyze.main.data.play.Prediction;
import football.analyze.main.data.play.Tournament;
import football.analyze.security.User;
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
import java.util.function.Function;

import static football.analyze.security.SecurityConstants.HEADER_STRING;

/**
 * @author Hassan Mushtaq
 * @since 6/3/18
 */
@Service
@Slf4j
public class PredictionLoader {

    private final RestTemplate restTemplate;

    private final String userUrl;

    public PredictionLoader(RestTemplate restTemplate,
                            @Value("${football.service.endpoints.user}") String userUrl) {
        this.restTemplate = restTemplate;
        this.userUrl = userUrl;
    }

    public List<Prediction> getPredictions(String tokenWithBearer, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_STRING, tokenWithBearer);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange(userUrl + "/" + username,
                HttpMethod.GET, entity, User.class);
        List<Prediction> predictions = responseEntity.getBody().getPredictions();
        predictions.sort(Comparator.comparing(prediction -> prediction.getMatch().getMatchNumber()));
        return predictions;
    }

    public List<Prediction> getAllPredictions(String tokenWithBearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_STRING, tokenWithBearer);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User> responseEntity = restTemplate.exchange(userUrl,
                HttpMethod.GET, entity, User.class);
        List<Prediction> predictions = responseEntity.getBody().getPredictions();
        predictions.sort(Comparator.comparing(prediction -> prediction.getMatch().getMatchNumber()));
        return predictions;
    }

    public boolean savePrediction(String tokenWithBearer, String username, Prediction prediction)   {
        return false;
    }
}
