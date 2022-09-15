package com.example.staff_service.rabbitMQ;

import com.example.staff_service.model.*;
import com.example.staff_service.repository.RequestRepository;
import com.example.staff_service.repository.TripRepository;
import com.example.staff_service.repository.WalletRepository;
import com.example.staff_service.security.JwtTokenProvider;
import com.example.staff_service.service.StaffDAO;
import com.example.staff_service.service.StaffDAOImpl;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * com.example.user_service.rabbitMQ;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:09 CH
 * Description: ...
 */
@Service
public class RabbitMqReceiver {

    @Autowired
    StaffDAO staffDAO=new StaffDAOImpl();

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public Result consumeJsonMessage(Pack4RBMQ pack4RBMQ) {
        Result result=null;
        switch (pack4RBMQ.getFunction()){
            case "LOGIN" -> {
                Staff staffLogin=new Gson().fromJson(pack4RBMQ.getContent(),Staff.class);
                result=staffDAO.login(staffLogin);
            }
            case "SIGNUP" -> {
                Staff staffSignup=new Gson().fromJson(pack4RBMQ.getContent(),Staff.class);
                result=staffDAO.signup(staffSignup);
            }
            case "GET_INFOR" -> {
                result=staffDAO.getStaffByUsername(pack4RBMQ.getContent());
            }
            case "GET_ALL_USER" -> {
                result=staffDAO.getAllUser();
            }
            case "GET_ALL_DRIVER" -> {
                result=staffDAO.getAllDriver();
            }
            case "GET_ALL_VEHICLE" -> {
                result=staffDAO.getAllVehicle();
            }
            case "GET_ALL_STAFF" -> {
                result=staffDAO.getAllStaff();
            }
            case "ADD_DIS" -> {
                Discount discount=new Gson().fromJson(pack4RBMQ.getContent(),Discount.class);
                result=staffDAO.addDiscount(discount);
            }
            case "EDIT_DIS" -> {
                Discount discount=new Gson().fromJson(pack4RBMQ.getContent(),Discount.class);
                result=staffDAO.editDiscount(discount);
            }
            case "GET_ALL_DIS" -> {
                result=staffDAO.getAllDiscount();
            }
            case "DELETE_DIS" -> {
                int discountId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=staffDAO.deleteDiscountById(discountId);
            }
            case "BOOKING" -> {
                Request request=new Gson().fromJson(pack4RBMQ.getContent(),Request.class);
                result=booking(request);
            }
            case "GET_VEHICLE_AND_PRICE" -> {
                DistanceAndTime distanceAndTime=new Gson().fromJson(pack4RBMQ.getContent(),DistanceAndTime.class);
                result= staffDAO.getVehicleAndPrice(distanceAndTime);
            }
            case "GET_ALL_REQUEST" -> {
                result=staffDAO.getAllRequest();
            }
            case "GET_REQUEST_ADDING_VEHICLE" -> {
                result=staffDAO.getRequestVehicle();
            }
            case "ACCEPT_ADDING_VEHICLE" -> {
                String licensePlateNum=pack4RBMQ.getContent();
                result=staffDAO.acceptVehicle(licensePlateNum);
            }
            case "GET_VEHICLE_OF_DRIVER" -> {
                int driverId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=staffDAO.getVehicleOfDriver(driverId);
            }
            case "CANCEL_BOOKING" ->{
                int requestId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=staffDAO.cancelBooking(requestId);
            }
            case "CHANGE_PASS" -> {
                Map<String, String> map=new Gson().fromJson(pack4RBMQ.getContent(),Map.class);
                String username = map.get("username");
                String password = map.get("password");
                String newPassword = map.get("newPassword");
                result=staffDAO.changePassword(username,password,newPassword);
            }
            case "EDIT_INFOR" -> {
                StaffInfor editInfo=new Gson().fromJson(pack4RBMQ.getContent(),StaffInfor.class);
                result=staffDAO.editStaffInfo(pack4RBMQ.getToken(), editInfo);
            }
            case "DELETE_STAFF" -> {
                result=staffDAO.deleteStaffById(Integer.parseInt(pack4RBMQ.getContent()));
            }
            case "GET_ALL_TRIP" -> {
                result=staffDAO.getAllTrip();
            }
            case "GET5T_USER" -> {
                int userId= new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=staffDAO.get5latestTripForUser(userId);
            }
            case "GET_TRIP_DRIVER" -> {
                int driverId= new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=staffDAO.getAllTripByDriver(driverId);
            }
            case "TRACK_DRIVER" -> {
                Position position= (Position) rabbitMQSender.sendMessage2FirebaseService(pack4RBMQ).getData();
                result=new Result(
                        position!=null,
                        position,
                        position!=null?"Tracking successfully":"Trip can be completed or doesn't exist");
            }
            case "GET_5MOST_DES" -> {
                int userId= new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=staffDAO.get5mostDesByUser(userId);
            }
            default -> new Result(false,null,"Something wrong");
        }
        return result;
    }


    public Result booking(Request request){
        try {
            Result result = staffDAO.booking(request);
            if(request.getDiscount()!=null) request.getDiscount().setRequest(null);
            if(request.getUserInfor()!=null){
                request.getUserInfor().setUser(null);
//                request.getUserInfor().setRequest(null);
//                request.getUserInfor().setAddresses(null);
            }
            request.setTrip(null);
            String responseFromFirebase = null;
            if(result.getStatus()){
                request= (Request) result.getData();
                Pack4RBMQ pack4RBMQ=new Pack4RBMQ("Booking",new Gson().toJson(request),null);

                responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(pack4RBMQ).getData();
                return new Result(responseFromFirebase != null,
                        responseFromFirebase,
                        responseFromFirebase!=null?"Booking successfully": null);
            }
            else {
                return new Result(false,
                        null,
                        result.getMessage());

            }
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }
}
