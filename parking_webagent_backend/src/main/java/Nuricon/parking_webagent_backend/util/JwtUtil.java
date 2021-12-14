package Nuricon.parking_webagent_backend.util;

import Nuricon.parking_webagent_backend.domain.User;
import Nuricon.parking_webagent_backend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Autowired
    private UserService userService;

    private String secret = "secret";

    public String extractName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Map<String, String> generateTokens(String userId, int expired) {
        Map<String, Object> claims = new HashMap<>();

        User user = userService.getUser(userId);
        claims.put("role",  user.getRole());
        claims.put("userid", user.getUserid());
        Map<String, String> res = new HashMap<>();
        res.put("access-token", createToken(claims, userId, expired)); // 30 min
        res.put("refresh-token", createToken(claims, userId, 1000 * 60 * 60 * 24 * 7));  // 1 week

        return res;
    }

    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        String res = createToken(claims, userId, 1000 * 60 * 60 * 10);  // 10 hours

        return res;
    }

    private String createToken(Map<String, Object> claims, String subject, int expiredTime) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                //.setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String name = extractName(token);
        return (name.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
