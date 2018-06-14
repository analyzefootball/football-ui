package football.analyze.main.data.prediction;

import football.analyze.main.data.play.Prediction;
import football.analyze.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
public class PredictionLoader {

    private final RestTemplate restTemplate;

    private final String userUrl;

    private final String predictionUrl;

    public PredictionLoader(RestTemplate restTemplate,
                            @Value("${football.service.endpoints.user}") String userUrl,
                            @Value("${football.service.endpoints.prediction}") String predictionUrl) {
        this.restTemplate = restTemplate;
        this.userUrl = userUrl;
        this.predictionUrl = predictionUrl;
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
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_STRING, tokenWithBearer);
        HttpEntity<Prediction> entity = new HttpEntity<>(prediction, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(predictionUrl+"/"+username, entity, Void.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }
}
