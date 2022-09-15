package com.example.otp.service;

import com.example.otp.model.Result;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * com.server.uber_clone.twilio;
 * Created by Phuoc -19127520
 * Date 10/08/2022 - 04:42 CH
 * Description: ...
 */
@Service
public class TwilioService {

    @Autowired
    public OTPService otpService=new OTPService();


    public TwilioService(){
        Twilio.init("AC3e71e1e1e78b8ac7a11ba55522f2d313", "668bdad0fffe247f99ff4a8e02e30f30");
    }

    public Result sendSMS(String username){
       try {
           int otp=otpService.generateOTP(username);

           Message.creator(new PhoneNumber("+84"+username.substring(1)),
                   new PhoneNumber("+15625394497"), "Hello from UberClone, this is your OTP: "+ otp+". Please keep it private").create();
           ;
           return new Result(true,null,"Send otp to your phone");
       }catch (Exception e){
           e.printStackTrace();
           return new Result(false,null,e.getMessage());
       }
    }

    public Result validateOTP(int otpnum, String username){
        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Validate the Otp
        if(otpnum >= 0){

            Integer serverOtp = otpService.getOtp(username);
            if(serverOtp > 0){
                if(otpnum == serverOtp){
                    otpService.clearOTP(username);

                    return new Result(true,null,SUCCESS);
                }
                else {
                    return new Result(false,null,FAIL);
                }
            }else {
                return new Result(false,null,FAIL);
            }
        }else {
            return new Result(false,null,FAIL);
        }
    }
}
