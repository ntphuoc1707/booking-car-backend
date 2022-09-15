package com.example.otp.controller;

import com.example.otp.model.Result;
import com.example.otp.service.TwilioService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.example.otp.controller;
 * Created by Phuoc -19127520
 * Date 31/08/2022 - 07:55 SA
 * Description: ...
 */

@RestController
@RequestMapping("/otp")
public class OTPController {
    @Autowired
    private TwilioService twilioService;

    @RequestMapping(value = "/sendOTP", method = RequestMethod.PUT)
    public ResponseEntity<Result> sendOTP(@RequestBody JsonNode username){
//        if(!result.getStatus()) return ResponseEntity.status(HttpStatus.OK).body(result);
//        User user= (User) result.getData();
        Result result=twilioService.sendSMS( username.get("username").textValue());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/validateOTP", method = RequestMethod.PUT)
    public ResponseEntity<Result> validateOTP(@RequestBody JsonNode otp){
        Result result=twilioService.validateOTP(Integer.parseInt(otp.get("otp").textValue()), otp.get("username").textValue());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
