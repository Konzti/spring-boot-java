package demo.com.jwtdemo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;



@Slf4j
public class JWTUtils {

    private String JWTSecret;

    public JWTUtils(String jwtSecret) {
        this.JWTSecret = jwtSecret;
        log.info(JWTSecret);
    }


    public Algorithm getAlgorithm () {
        Algorithm algorithm = Algorithm.HMAC256(JWTSecret);
        return algorithm;
    }
    public JWTVerifier getVerifier () {
        JWTVerifier verifier;
        try {
            verifier = JWT.require(getAlgorithm()).build();
        }
        catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token could not be verified");
        }
        return verifier;

    }
    public DecodedJWT getDecodedJWT (String token) {
        DecodedJWT decodedJWT = getVerifier().verify(token);
        return decodedJWT;
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String[] getClaimsFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getClaim("roles").asArray(String.class);
    }

    public String generateJWT (User user) {
        String[] claims = getClaimsFromUser(user);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+15*60*1000))
                .withArrayClaim("roles", claims)
                .sign(getAlgorithm());
    }

    public String[] getClaimsFromUser(User user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority: user.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

}


