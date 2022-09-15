package com.example.driver_service.rabbitMQ;

import com.example.driver_service.model.*;
import com.example.driver_service.repository.RequestRepository;
import com.example.driver_service.repository.TripRepository;
import com.example.driver_service.repository.WalletRepository;
import com.example.driver_service.security.JwtTokenProvider;
import com.example.driver_service.service.DriverDAO;
import com.example.driver_service.service.DriverDAOImpl;
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
    DriverDAO driverDAO=new DriverDAOImpl();

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
        Result result = null;
        switch (pack4RBMQ.getFunction()) {
            case "LOGIN" -> {
                Driver driverLogin=new Gson().fromJson(pack4RBMQ.getContent(),Driver.class);
                result=driverDAO.login(driverLogin);
            }
            case "SIGNUP" -> {
                Driver driverSignup=new Gson().fromJson(pack4RBMQ.getContent(),Driver.class);
                result=driverDAO.signup(driverSignup);
            }
            case "ADD_VEHICLE" -> {
                Vehicle vehicle=new Gson().fromJson(pack4RBMQ.getContent(),Vehicle.class);
                result = driverDAO.addVehicle(
                        Math.toIntExact(jwtTokenProvider.getIdFromToken(pack4RBMQ.getToken())),
                        vehicle);
            }
            case "CHECK_PHONE_NUM" ->{
                result=driverDAO.isExistPhoneNumber(pack4RBMQ.getContent());
            }
            case "CHECK_DRIVER_INFOR" ->{
                DriverInfor driverInfor=new Gson().fromJson(pack4RBMQ.getContent(),DriverInfor.class);
                result=driverDAO.checkDriverInfo(driverInfor);
            }
            case "CHECK_VEHICLE_PLATE" ->{
                result=driverDAO.checkVehiclePlateNum(pack4RBMQ.getContent());
            }
            case "GET_VEHICLE" ->{
                result = driverDAO.getVehicle(Math.toIntExact(jwtTokenProvider.getIdFromToken(pack4RBMQ.getToken())));
            }
            case "GET_INFOR" ->{
                result=driverDAO.getDriverByToken(pack4RBMQ.getToken());
            }
            case "GET_WALLET" ->{
                result=driverDAO.getWallet(pack4RBMQ.getToken());
            }
            case "RECHARGE" ->{
                result=driverDAO.recharge(pack4RBMQ.getToken(), Double.parseDouble(pack4RBMQ.getContent()));
            }
            case "WITHDRAW" ->{
                result=driverDAO.withdraw(pack4RBMQ.getToken(), Double.parseDouble(pack4RBMQ.getContent()));
            }
            case "ACCEPT_BOOKING" ->{
                result=acceptBooking(pack4RBMQ);
            }
            case "ONLINE" ->{
                Position position=new Gson().fromJson(pack4RBMQ.getContent(),Position.class);
                result = online(pack4RBMQ);
            }
            case "OFFLINE" ->{
                result = offline(pack4RBMQ);
            }
            case "COMPLETE_TRIP" -> {
                result=completeTrip(pack4RBMQ);
            }
            case "UPDATE_POS" -> {
                result=updatePos(pack4RBMQ);
            }
            case "GET_FEEDBACKS" -> {
                result = driverDAO.getFeedBacks(pack4RBMQ.getToken());
            }
            case "GET_TRIPS" -> {
                result = driverDAO.getTrips(pack4RBMQ.getToken());
            }
            default -> result=new Result(false,null,"Something wrong");
        }
        return result;
    }

    private Result updatePos(Pack4RBMQ pack4RBMQ){
        try {
            String responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(pack4RBMQ).getData();
            if(responseFromFirebase==null) return new Result(false,null,null);
            return new Result(true,null,responseFromFirebase);
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    private Result completeTrip(Pack4RBMQ pack4RBMQ){
        try {
            Map<String, Object> map=new Gson().fromJson(pack4RBMQ.getContent(),Map.class);
            Double newData = (Double) map.get("requestId");
            int requestId = newData.intValue();
            String completeTime = (String) map.get("completeTime");

            String responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(pack4RBMQ).getData();
            if(responseFromFirebase==null) return new Result(false,null,"Failed to complete trip");

            Request request=requestRepository.findRequestByRequestId(requestId);
            request.setStatus("Completed");
            requestRepository.save(request);

            Trip trip = tripRepository.findByTripId(requestId);
            trip.setStatus("Complete");
            trip.setCompleteTime(completeTime);
            tripRepository.save(trip);

            if (trip.getPaymentType().equals(PaymentType.E_WALLET))
            {
                Wallet walletDriver = walletRepository.findByIdOwnerAndWalletType(trip.getDriverId(), WalletType.DRIVER);
                walletDriver.setBalance(walletDriver.getBalance() + trip.getVehicleAndPrice().getPrice());

                Wallet walletUser = walletRepository.findByIdOwnerAndWalletType(trip.getUserId(), WalletType.USER);
                walletUser.setBalance(walletUser.getBalance() - trip.getVehicleAndPrice().getPrice());

                walletRepository.save(walletDriver);
                walletRepository.save(walletUser);
            }


            return new Result(true, null, "Completed trip");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,null,e.getMessage());
        }

    }

    private Result acceptBooking(Pack4RBMQ pack4RBMQ){
        try {
            Map<String, Object> map=new HashMap<>();

            int requestId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
            Result driver=driverDAO.getDriverByToken(pack4RBMQ.getToken());
            if(!driver.getStatus()) return driver;
            DriverDTO driverDTO= (DriverDTO) driver.getData();
            driverDTO.getVehicleList().get(0).setDriverInfor(null);
            map.put("driverDTO", new Gson().toJson(driverDTO));
            Request request=requestRepository.findRequestByRequestId(requestId);
            if(request==null) return new Result(false,null,"Request doesn't exist");
            request.setStatus("Already have a driver");
            Trip trip=new Trip();
            //driverDTO.setVehicleList(null);
            //trip.setDriverDTO(driverDTO);
            trip.setDriverId(driverDTO.getId());
            request.setTrip(trip);
            trip.setDataRequest(request);
            Feedback feedback=new Feedback();
            feedback.setTripId(trip.getTripId());
            feedback.setTrip(trip);
            trip.setFeedback(feedback);

            //tripRepository.save(trip);
            requestRepository.save(request);

            request.setTrip(null);
            request.getUserInfor().setUser(null);
            if(request.getDiscount()!=null) request.getDiscount().setRequest(null);
            map.put("request",new Gson().toJson(request));
            Pack4RBMQ newPack=new Pack4RBMQ(
                    pack4RBMQ.getFunction(),
                    new Gson().toJson(map),
                    null
            );
            String responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(newPack).getData();
            return new Result(responseFromFirebase != null,
                    responseFromFirebase,
                    responseFromFirebase!=null?"Accept successfully": null);
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    private Result offline(Pack4RBMQ pack4RBMQ){
        try {
            Result driver=driverDAO.getDriverByToken(pack4RBMQ.getToken());
            if(!driver.getStatus()) return driver;
            DriverDTO driverDTO= (DriverDTO) driver.getData();
            Pack4RBMQ newPack=new Pack4RBMQ(pack4RBMQ.getFunction(),new Gson().toJson(driverDTO.getId()),null);
            String responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(newPack).getData();
            return new Result(responseFromFirebase == null,
                    null,
                    responseFromFirebase==null?"Offline successfully":responseFromFirebase);
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    private Result online(Pack4RBMQ pack4RBMQ){
        try {
            Result driver=driverDAO.getDriverByToken(pack4RBMQ.getToken());
            if(!driver.getStatus()) return driver;
            DriverDTO driverDTO= (DriverDTO) driver.getData();
            driverDTO.getVehicleList().get(0).setDriverInfor(null);
            Position position=new Gson().fromJson(pack4RBMQ.getContent(),Position.class);

            Map<String, Object> map=new HashMap<>();
            map.put("driverDTO", new Gson().toJson(driverDTO));
            map.put("position",new Gson().toJson(position));
            Pack4RBMQ newPack=new Pack4RBMQ(
                    pack4RBMQ.getFunction(),
                    new Gson().toJson(map),
                    null
            );
            String responseFromFirebase= (String) rabbitMQSender.sendMessage2FirebaseService(newPack).getData();
            return new Result(responseFromFirebase != null,
                    responseFromFirebase,
                    responseFromFirebase!=null?"Online successfully": null);
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }
}
