package com.example.driver_service.security;

import com.example.driver_service.model.Result;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * com.server.uber_clone.security.authentication.tokenProvider;
 * Created by Phuoc -19127520
 * Date 29/07/2022 - 01:30 CH
 * Description: ...
 */
@Component
public class JwtTokenProvider {
    private static final String JWT_SECRET = "aloalo";

    private static final long JWT_EXPIRATION = 604800000L;

    public String generateToken(int id) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
            // Tạo chuỗi json web token từ id của user.
            return Jwts.builder()
                    .setSubject(Long.toString(id))
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        }
        catch (Exception e){
            return e.getMessage();
        }

    }

    public String generateToken(int id,long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(Long.toString(id))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public Long getIdFromToken(String token) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        }catch (ExpiredJwtException e){
            e.printStackTrace();
            return -1L;
        }
    }

    public Result validateToken(String authToken) {
        try {
            Jws<Claims> jwsR=Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return new Result(true,Integer.parseInt(jwsR.getBody().getSubject()),"Valid");
        } catch (MalformedJwtException ex) {
            return new Result(false,null,"Invalid");
            //log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            return new Result(true,Integer.parseInt(ex.getClaims().getSubject()),"Expired");
            //log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            return new Result(false,null,"Unsupported");
            //log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            return new Result(false,null,"Empty");
            //log.error("JWT claims string is empty.");
        }

    }

}
