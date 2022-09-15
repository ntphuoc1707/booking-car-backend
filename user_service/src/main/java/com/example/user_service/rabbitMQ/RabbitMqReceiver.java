package com.example.user_service.rabbitMQ;

import com.example.user_service.model.*;
import com.example.user_service.repository.AddressRepository;
import com.example.user_service.repository.TripRepository;
import com.example.user_service.security.JwtTokenProvider;
import com.example.user_service.service.UserDAO;
import com.example.user_service.service.UserDAOImpl;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    UserDAO userDAO=new UserDAOImpl();

    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public Result consumeJsonMessage(Pack4RBMQ pack4RBMQ) {
        Result result = null;
        switch (pack4RBMQ.getFunction()) {
            case "LOGIN" -> {
                User user = new Gson().fromJson(pack4RBMQ.getContent(), User.class);
                result = userDAO.login(user);
            }
            case "SIGNUP" -> {
                User user = new Gson().fromJson(pack4RBMQ.getContent(), User.class);
                result = userDAO.signup(user);
            }
            case "CHECK_PHONE_NUM" -> {
                result=userDAO.checkPhoneNumber(pack4RBMQ.getContent());
            }
            case "CHECK_EMAIL" ->{
                result=userDAO.checkEmail(pack4RBMQ.getContent());
            }
            case "BOOKING" ->{
                Request request=new Gson().fromJson(pack4RBMQ.getContent(),Request.class);
                result=booking(request, pack4RBMQ.getToken());
            }
            case "GET_VEHICLE_AND_PRICE" ->{
                DistanceAndTime distanceAndTime=new Gson().fromJson(pack4RBMQ.getContent(),DistanceAndTime.class);
                result=userDAO.getVehicleAndPrice(distanceAndTime);
            }
            case "CANCEL_BOOKING" ->{
                int requestId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=cancelBooking(requestId);
            }
            case "GET_INFOR" ->{
                result=userDAO.getUserInforByToken(pack4RBMQ.getToken());
            }
            case "GET_LIST_DIS" ->{
                result=userDAO.getListDiscount();
            }
            case "GET_WALLET" ->{
                result=userDAO.getWallet(pack4RBMQ.getToken());
            }
            case "RECHARGE" ->{
                result=userDAO.recharge(pack4RBMQ.getToken(), Double.parseDouble(pack4RBMQ.getContent()));
            }
            case "WITHDRAW" ->{
                result=userDAO.withdraw(pack4RBMQ.getToken(), Double.parseDouble(pack4RBMQ.getContent()));
            }
            case "AFTER_BOOKING" ->{
                int requestId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                result=userDAO.getLinkAfterBooking(requestId);
            }
            case "FEEDBACK_TRIP" -> {
                Feedback feedback=new Gson().fromJson(pack4RBMQ.getContent(),Feedback.class);
                result=setFeedback(feedback);
            }
            default -> result=new Result(false,null,"Something wrong");
        }
        return result;
    }

    private Result setFeedback(Feedback feedback){
        try {
            Trip trip=tripRepository.findByTripId(feedback.getTripId());
            if(trip==null) return new Result(false,null,"Trip doesn't existed");
            trip.setFeedback(feedback);
            feedback.setTrip(trip);
            tripRepository.save(trip);
            return new Result(true,null,"Feedback successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    public Result booking(Request request, String token){
        try {
            int userId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            Result result = userDAO.booking(userId, request);

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
                if(responseFromFirebase!=null){
                    Position destination=request.getDestination();
                    Address address=addressRepository.findByAddressAndLatitudeAndLongitudeAndUserId(
                            destination.getAddress(),
                            destination.getLatitude(),
                            destination.getLongitude(),
                            request.getUserId()
                    );
                    if(address==null) {
                        address=new Address();
                        address.setUserId(request.getUserId());
                        address.setAddress(request.getDestination());
                        address.setUserInfor(request.getUserInfor());
                        address.setPickNum(1);
                    }
                    else address.setPickNum(address.getPickNum()+1);
                    addressRepository.save(address);
                }
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

    public Result cancelBooking(int requestId){
        try {
            Result result=userDAO.cancelBooking(requestId);
            if(!result.getStatus()) return result;
            Pack4RBMQ pack4RBMQ=new Pack4RBMQ("CANCEL_BOOKING",new Gson().toJson(requestId),null);

            String responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(pack4RBMQ).getData();
            return new Result(
                    responseFromFirebase == null,
                    null,
                    responseFromFirebase==null?"Cancel successfully":responseFromFirebase
            );
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }
}
