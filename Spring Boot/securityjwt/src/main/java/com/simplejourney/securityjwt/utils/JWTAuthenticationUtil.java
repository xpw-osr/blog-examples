package com.simplejourney.securityjwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JWTAuthenticationUtil {
    @Value("${jwt.secret}")
    private String secret;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;


    public String getUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        if (null == claims) {
            claims = new HashMap<>();
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username =getClaimFromToken(token, Claims::getSubject);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String generateIdent(HttpServletRequest request) {
        String identSource = String.format("%s;%s", getIPAddress(request), getUserAgent(request));
        return new String(Base64.getEncoder().encode(identSource.getBytes()));
    }

    public String getIdent(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return String.valueOf(claims.get("ident"));
    }


    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        /*
        iss: issuer
        exp: expiration time
        sub: subject
        aud: audience
        nbf: Not Before
        iat: Issued At
        jti: JWT ID
         */
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)    // sub
                .setIssuedAt(new Date(System.currentTimeMillis()))  // iat
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))    // exp
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private String getIPAddress(HttpServletRequest request) {
        String ip = "";
        if (null != request) {
            ip = request.getHeader("X-FORWARDED-FOR");
            if (null == ip || ip.isEmpty()) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    private String getUserAgent(HttpServletRequest request) {
        String userAgent = "";
        if (null != request) {
            userAgent = request.getHeader("User-Agent");
        }
        return userAgent;
    }
}
