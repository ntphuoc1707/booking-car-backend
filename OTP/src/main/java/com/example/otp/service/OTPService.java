package com.example.otp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * com.server.uber_clone.twilio;
 * Created by Phuoc -19127520
 * Date 12/08/2022 - 03:38 CH
 * Description: ...
 */
@Service
public class OTPService {

    Jedis jedis=new Jedis();

    public int generateOTP(String key){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        jedis.set(key, String.valueOf(otp));
        jedis.expire(key,60);
        return otp;
    }

    public int getOtp(String key){
        try{
            return Integer.parseInt(jedis.get(key));
        }catch (Exception e){
            return 0;
        }
    }

    public void clearOTP(String key){
        jedis.del(key);
    }
}
