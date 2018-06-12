package football.analyze.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

import static football.analyze.security.SecurityConstants.TOKEN_PREFIX;

/**
 * @author Hassan Mushtaq
 * @since 6/12/18
 */
@Component
public class JWTService {

    private final String jwtSecret;


    public JWTService(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public boolean isAdmin(String jwtToken)  {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(jwtToken.replace(TOKEN_PREFIX, ""));
            Claim claim = jwt.getClaim("admin");
            return claim.asBoolean();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
