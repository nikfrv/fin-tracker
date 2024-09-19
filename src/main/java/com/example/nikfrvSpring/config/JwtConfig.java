package com.example.nikfrvSpring.config;

import com.example.nikfrvSpring.util.JwtUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecretKey jwtSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public JwtUtil jwtUtil(SecretKey secretKey){
        return new JwtUtil(secretKey);
    }
}
