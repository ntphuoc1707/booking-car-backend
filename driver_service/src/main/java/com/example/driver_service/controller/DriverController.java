package com.example.driver_service.controller;

import com.example.driver_service.model.*;
import com.example.driver_service.rabbitMQ.RabbitMQSender;
import com.example.driver_service.service.DriverDAO;
import com.example.driver_service.service.DriverDAOImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * com.example.driver_service.controller;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:59 SA
 * Description: ...
 */
@Slf4j
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    DriverDAO driverDAO=new DriverDAOImpl();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Result> login(@RequestBody Driver driverLogin){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("LOGIN",new Gson().toJson(driverLogin),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Result> signUp(@RequestBody Driver driverSignup){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("SIGNUP",new Gson().toJson(driverSignup),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public ResponseEntity<Result> addVehicle(@RequestBody Vehicle vehicle, @RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("ADD_VEHICLE",new Gson().toJson(vehicle),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/checkPhoneNumber", method = RequestMethod.POST)
    public ResponseEntity<Result> checkPhoneNumber(@RequestBody JsonNode phoneNumber){

        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CHECK_PHONE_NUM",phoneNumber.get("phoneNumber").textValue(),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/checkDriverInfo", method = RequestMethod.POST)
    public ResponseEntity<Result> checkDriverInfo(@RequestBody DriverInfor driverInfor){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CHECK_DRIVER_INFOR",new Gson().toJson(driverInfor),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/checkVehiclePlateNum", method = RequestMethod.POST)
    public ResponseEntity<Result> checkVehiclePlateNum(@RequestBody JsonNode licensePlateNum){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CHECK_VEHICLE_PLATE",licensePlateNum.get("vehiclePlateNum").textValue(),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/getVehicle", method = RequestMethod.GET)
    public ResponseEntity<Result> getVehicle(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_VEHICLE",null,token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getInforByToken", method = RequestMethod.GET)
    public ResponseEntity<Result> getInforByToken(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_INFOR",null,token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @JsonIgnore
    @RequestMapping(value = "/online", method = RequestMethod.POST)
    public ResponseEntity<Result> online(@RequestHeader("token") String token, @RequestBody Position position){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("ONLINE",new Gson().toJson(position),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/offline", method = RequestMethod.GET)
    public ResponseEntity<Result> offline(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("OFFLINE",null,token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getWallet", method = RequestMethod.GET)
    public ResponseEntity<Result> getWallet(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_WALLET",null,token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/recharge", method = RequestMethod.PUT)
    public ResponseEntity<Result> recharge(@RequestHeader("token") String token, @RequestBody JsonNode money){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("RECHARGE",money.get("money").textValue(),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.PUT)
    public ResponseEntity<Result> withdraw(@RequestHeader("token") String token, @RequestBody JsonNode money){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("WITHDRAW",money.get("money").textValue(),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/acceptBooking/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<Result> acceptBooking(@RequestHeader("token") String token, @PathVariable("requestId") int requestId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("ACCEPT_BOOKING",new Gson().toJson(requestId),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/completeTrip", method = RequestMethod.PUT)
    public ResponseEntity<Result> completeTrip( @RequestBody JsonNode data){
        Map<String,Object> map=new HashMap<>();
        map.put("requestId",data.get("requestId").intValue());
        map.put("completeTime",data.get("completeTime").textValue());
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("COMPLETE_TRIP",new Gson().toJson(map),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/updatePosition/{tripId}", method = RequestMethod.PUT)
    public ResponseEntity<Result> updatePosition(@RequestBody Position position, @PathVariable("tripId") int tripId){
        Map<String,Object> map=new HashMap<>();
        map.put("tripId",tripId);
        map.put("position",new Gson().toJson(position));
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("UPDATE_POS",new Gson().toJson(map),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getFeedbacks", method = RequestMethod.GET)
    public ResponseEntity<Result> getFeedBacks(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_FEEDBACKS",null,token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getTrips", method = RequestMethod.GET)
    public ResponseEntity<Result> getTrips(@RequestHeader("token") String token){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_TRIPS",null,token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
