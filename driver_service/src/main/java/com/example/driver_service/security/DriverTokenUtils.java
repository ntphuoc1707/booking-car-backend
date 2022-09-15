package com.example.driver_service.security;

import com.example.driver_service.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverTokenUtils {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Boolean checkExpired(String token){
        Result result=jwtTokenProvider.validateToken(token);
        return result.getStatus() && result.getMessage().equals("Expired");
    }

    public Integer getIdFromToken(String token){
        Result result=jwtTokenProvider.validateToken(token);
        return result.getStatus()? (Integer) result.getData() :-1;
    }
}
