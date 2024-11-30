package com.example.demo.utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.dto.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import com.auth0.jwt.JWT;

@Component
public class JWTUtils {

    @Value("jwt.secret")
    private String SECRET;
    private final long EXPIRES = 1000 * 60 * 60 * 24 * 7; //1 week

    public String generateToken(String username, RoleEnum role) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES))
                .withClaim("role", role.name())
                .sign(Algorithm.HMAC256(SECRET));
    }

    public boolean validateToken(String token, String username) {
        var decodedToken = JWT.decode(token);
        return decodedToken.getSubject().equals(username) && decodedToken.getExpiresAt().after(new Date(System.currentTimeMillis()));
    }

    public DecodedJWT getDecodedToken(String token) {
        return JWT.decode(token);
    }
}
