package com.example.staff_service.controller;

import com.example.staff_service.model.*;
import com.example.staff_service.rabbitMQ.RabbitMQSender;
import com.example.staff_service.service.StaffDAO;
import com.example.staff_service.service.StaffDAOImpl;
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
 * com.example.staff_service.controller;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 05:57 CH
 * Description: ...
 */
@Slf4j
@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    StaffDAO staffDAO=new StaffDAOImpl();
    
    @Autowired
    RabbitMQSender rabbitMQSender;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Result> login(@RequestBody Staff staffLogin){
//       Result result=staffDAO.login(staffLogin);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("LOGIN",new Gson().toJson(staffLogin),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Result> signUp(@RequestBody Staff staffSignup){
//        Result result=staffDAO.signup(staffSignup);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("SIGNUP",new Gson().toJson(staffSignup),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/infor", method = RequestMethod.GET)
    public ResponseEntity<Result> getInfor(@RequestBody JsonNode username){
//        Result result=staffDAO.getStaffByUsername(username.get("username").textValue());
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_INFOR",username.get("username").textValue(),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllUser(){
//        Result result=staffDAO.getAllUser();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_USER",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllDriver", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllDriver(){
//        Result result=staffDAO.getAllDriver();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_DRIVER",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllVehicle", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllVehicle(){
//        Result result=staffDAO.getAllVehicle();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_VEHICLE",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @RequestMapping(value = "/getAllStaff", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllStaff(){

//            Result result=staffDAO.getAllStaff();
//            return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_STAFF",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/addDiscount", method = RequestMethod.POST)
    public ResponseEntity<Result> addDiscount(@RequestBody Discount discount){
//        Result result=staffDAO.addDiscount(discount);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("ADD_DIS",new Gson().toJson(discount),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/editDiscount", method = RequestMethod.POST)
    public ResponseEntity<Result> editDiscount(@RequestBody Discount discount){
//        Result result=staffDAO.editDiscount(discount);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("EDIT_DIS",new Gson().toJson(discount),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllDiscount", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllDiscount(){
//        Result result=staffDAO.getAllDiscount();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_DIS",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/deleteDiscount/{discountId}", method = RequestMethod.DELETE)
    public ResponseEntity<Result> deleteDiscount(@PathVariable("discountId") int discountId){
//        Result result=staffDAO.deleteDiscountById(discountId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("DELETE_DIS",new Gson().toJson(discountId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/booking", method = RequestMethod.PUT)
    public ResponseEntity<Result> booking(@RequestBody Request request){
//        Result result=staffDAO.booking(request);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("BOOKING",new Gson().toJson(request),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getVehicleAndPrice", method = RequestMethod.PUT)
    public ResponseEntity<Result> getVehicleAndPrice(@RequestBody DistanceAndTime distanceAndTime){
//        Result result= staffDAO.getVehicleAndPrice(distanceAndTime);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_VEHICLE_AND_PRICE",new Gson().toJson(distanceAndTime),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllRequest", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllRequest(){
//        Result result=staffDAO.getAllRequest();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_REQUEST",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getRequestAddingVehicle", method = RequestMethod.GET)
    public ResponseEntity<Result> getRequestAddingVehicle(){
//        Result result=staffDAO.getRequestVehicle();
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_REQUEST_ADDING_VEHICLE",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/acceptVehicle/{licensePlateNum}", method = RequestMethod.PUT)
    public ResponseEntity<Result> acceptAddingVehicle(@PathVariable("licensePlateNum") String licensePlateNum){
//        Result result=staffDAO.acceptVehicle(licensePlateNum);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("ACCEPT_ADDING_VEHICLE",licensePlateNum,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @RequestMapping(value = "/confirmBooking", method = RequestMethod.PUT)
//    public ResponseEntity<Result> confirmBooking(@RequestBody ConfirmBooking confirmBooking){
//        Result result=staffDAO.confirmBooking(confirmBooking);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

    @RequestMapping(value = "/getVehicleOfDriver/{driverId}", method = RequestMethod.GET)
    public ResponseEntity<Result> getVehicleOfDriver(@PathVariable("driverId") int driverId){
//        Result result=staffDAO.getVehicleOfDriver(driverId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_VEHICLE_OF_DRIVER",new Gson().toJson(driverId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/cancelBooking/{requestId}", method = RequestMethod.PUT)
    public ResponseEntity<Result> cancelBooking(@PathVariable("requestId") int requestId){
//        Result result=staffDAO.cancelBooking(requestId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CANCEL_BOOKING",new Gson().toJson(requestId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Result> changePassword(@RequestBody JsonNode changePasswordInfo){

//            String username = changePasswordInfo.get("username").textValue();
//            String password = changePasswordInfo.get("password").textValue();
//            String newPassword = changePasswordInfo.get("newPassword").textValue();
//            Result result=staffDAO.changePassword(username,password,newPassword);
//            return ResponseEntity.status(HttpStatus.OK).body(result);
        Map<String, String> map=new HashMap<>();
        map.put("username",changePasswordInfo.get("username").textValue());
        map.put("password",changePasswordInfo.get("password").textValue());
        map.put("newPassword",changePasswordInfo.get("newPassword").textValue());
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CHANGE_PASS",new Gson().toJson(map),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/editStaffInfo", method = RequestMethod.POST)
    public ResponseEntity<Result> editInformation(@RequestHeader("token") String token, @RequestBody StaffInfor editInfo){
//        Result result=staffDAO.editStaffInfo(token, editInfo);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("EDIT_INFOR",new Gson().toJson(editInfo),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @RequestMapping(value = "/deleteStaff", method = RequestMethod.POST)
    public ResponseEntity<Result> deleteStaff(@RequestHeader("token") String token, @RequestBody JsonNode deleteInfo){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("DELETE_STAFF",deleteInfo.get("staffId").textValue(),token);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllTrip", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllTrip(){
//        Result result=staffDAO.getVehicleOfDriver(driverId);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_ALL_TRIP",null,null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/get5latestTripForUser/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Result> get5latestTrip4User(@PathVariable("userId") int userId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET5T_USER",new Gson().toJson(userId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/getAllTripByDriver/{driverId}", method = RequestMethod.GET)
    public ResponseEntity<Result> getAllTripByDriver(@PathVariable("driverId") int driverId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_TRIP_DRIVER",new Gson().toJson(driverId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/trackingDriverOnTrip/{tripId}", method = RequestMethod.GET)
    public ResponseEntity<Result> trackingDriver(@PathVariable("tripId") int tripId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("TRACK_DRIVER",new Gson().toJson(tripId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/get5mostDestination/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Result> get5mostDestination(@PathVariable("userId") int userId){
        Pack4RBMQ pack4RBMQ=new Pack4RBMQ("GET_5MOST_DES",new Gson().toJson(userId),null);
        Result result=rabbitMQSender.sendMessage2DriverService(pack4RBMQ);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}