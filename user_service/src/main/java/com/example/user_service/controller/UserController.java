package com.example.user_service.controller;

import com.example.user_service.model.*;
import com.example.user_service.rabbitMQ.RabbitMQSender;
import com.example.user_service.service.UserDAO;
import com.example.user_service.service.UserDAOImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.example.user_service.controller;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:57 SA
 * Description: ...
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDAO userDAO=new UserDAOImpl();

    @Autowired
    RabbitMQSender rabbitMQSender;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestBody User userLogin){
        log.info("staying user");
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("LOGIN", new Gson().toJson(userLogin),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Result> signUp(@RequestBody User userSignup){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("SIGNUP", new Gson().toJson(userSignup),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        //rabbitMqSender.send(result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/checkPhonenumber", method = RequestMethod.POST)
    public ResponseEntity<Result> checkPhonenumber(@RequestBody JsonNode phoneNumber){
//        Result result=userDAO.checkPhoneNumber(phoneNumber.get("phoneNumber").textValue());
//        return ResponseEntity.status(HttpStatus.OK).body(result);

        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CHECK_PHONE_NUM",phoneNumber.get("phoneNumber").textValue(),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        //rabbitMqSender.send(result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    public ResponseEntity<Result> checkEmail(@RequestBody JsonNode email){
//        Result result=userDAO.checkEmail(email.get("email").textValue());
//        return ResponseEntity.status(HttpStatus.OK).body(result);

        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CHECK_EMAIL",email.get("email").textValue(),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        //rabbitMqSender.send(result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @RequestMapping(value = "/infor", method = RequestMethod.GET)
//    public ResponseEntity<Result> getUserInfor(@RequestBody JsonNode username){
//        Result result= userDAO.getUserByUsername(username.get("username").textValue());
//       // rabbitMqSender.send(result);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//
//
//    }

    @RequestMapping(value = "/booking", method = RequestMethod.PUT)
    public ResponseEntity<Result> booking(@RequestBody Request request, @RequestHeader("token") String token){

        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("BOOKING",new Gson().toJson(request),token);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getVehicleAndPrice", method = RequestMethod.PUT)
    public ResponseEntity<Result> getVehicleAndPrice(@RequestBody DistanceAndTime distanceAndTime){

        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_VEHICLE_AND_PRICE",new Gson().toJson(distanceAndTime),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/cancelBooking/{requestId}", method = RequestMethod.PUT)
    public ResponseEntity<Result> cancelBooking(@PathVariable("requestId") int requestId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CANCEL_BOOKING",new Gson().toJson(requestId),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        //rabbitMqSender.send(result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getInforByToken", method = RequestMethod.GET)
    public ResponseEntity<Result> getInforByToken(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_INFOR",null,token);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getListDiscount", method = RequestMethod.GET)
    public ResponseEntity<Result> getListDiscount(){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_LIST_DIS",null,null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getWallet", method = RequestMethod.GET)
    public ResponseEntity<Result> getWallet(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_WALLET",null,token);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.PUT)
    public ResponseEntity<Result> recharge(@RequestHeader("token") String token, @RequestBody JsonNode money){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("RECHARGE",money.get("money").textValue(),token);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.PUT)
    public ResponseEntity<Result> withdraw(@RequestHeader("token") String token, @RequestBody JsonNode money){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("WITHDRAW",money.get("money").textValue(),token);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getLinkAfterBooking/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<Result> getLinkAfterBooking(@PathVariable("requestId") int requestId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("AFTER_BOOKING",new Gson().toJson(requestId),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/feedback/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<Result> feedback(@RequestBody Feedback feedback, @PathVariable("requestId") int requestId){
        feedback.setTripId(requestId);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("FEEDBACK_TRIP",new Gson().toJson(feedback),null);
        Result result=rabbitMQSender.sendMessage2UserService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
