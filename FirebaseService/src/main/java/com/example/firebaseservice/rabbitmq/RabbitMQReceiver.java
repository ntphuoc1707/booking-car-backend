package com.example.firebaseservice.rabbitmq;

import com.example.firebaseservice.model.*;
import com.example.firebaseservice.service.FirebaseSevice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * com.example.firebaseservice.rabbitmq;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 11:17 CH
 * Description: ...
 */
@Service
public class RabbitMQReceiver {



    @Autowired
    FirebaseSevice firebaseSevice;

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public String consumeJsonMessage(Pack4RBMQ pack4RBMQ) {
        Result result = null;
        switch (pack4RBMQ.getFunction()) {
            case "Booking" -> {
                try{
                    Request request=new Gson().fromJson(pack4RBMQ.getContent(),Request.class);
                    String typeList= pack4RBMQ.getFunction();
                    String response=firebaseSevice.onlineOrBooking(
                            request.getVehicleAndPrice().getVehicleType()
                            ,request.getRequestId()
                            ,typeList
                            ,request
                            ,request.getStartAddress()
                    );
                    result=new Result(response!=null,
                            response,
                            response!=null?typeList+" successfully":null);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());
                }
            }
            case "ONLINE" -> {
                try{
                    String typeList=pack4RBMQ.getFunction();
                    Map<String, Object> map=new Gson().fromJson(pack4RBMQ.getContent(),Map.class);
                    DriverDTO driverDTO= new Gson().fromJson((String) map.get("driverDTO"),DriverDTO.class);
                    Position position= new Gson().fromJson((String) map.get("position"),Position.class);
                    String response=firebaseSevice.onlineOrBooking(
                            driverDTO.getVehicleList().get(0).getTypeOfVehicle(),
                            driverDTO.getDriverId(),
                            "DriverList"
                            ,driverDTO
                            ,position
                    );
                    result=new Result(response!=null,
                            response,
                            response!=null?typeList+" successfully":null);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());
                }
            }
            case "OFFLINE" ->{
                try{
                    int driverId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                    String response=firebaseSevice.offline(driverId);
                    result=new Result(response == null,null,response==null?"Offline successfully":response);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());;
                }
            }
            case "CANCEL_BOOKING" -> {
                try {
                    int requestId=new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                    String response=firebaseSevice.cancelBooking(requestId);
                    result=new Result(response == null,null,response==null?"Cancel successfully":response);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,"Something wrong");
                }
            }
            case "ACCEPT_BOOKING" -> {
                try {
                    Map<String, Object> map=new Gson().fromJson(pack4RBMQ.getContent(),Map.class);
                    Request request = new Gson().fromJson((String) map.get("request"),Request.class);
                    DriverDTO driverDTO= new Gson().fromJson((String) map.get("driverDTO"),DriverDTO.class);
                    String response=firebaseSevice.acceptBooking(request, driverDTO);

                    result=new Result(response != null,response,response!=null?"Accept successfully": null);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());
                }
            }
            case "UPDATE_POS" -> {
                try {
                    Map<String, Object> map=new Gson().fromJson(pack4RBMQ.getContent(),Map.class);
                    Double newData = (Double) map.get("tripId");
                    int tripId = newData.intValue();
                    Position position= new Gson().fromJson((String) map.get("position"),Position.class);
                    String response=firebaseSevice.updatePos(tripId,position);
                    result=new Result(response != null,response,response!=null?"Cancel successfully":null);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());
                }
            }
            case "COMPLETE_TRIP" -> {
                try {
                    Map<String, Object> map=new Gson().fromJson(pack4RBMQ.getContent(),Map.class);
                    Double newData = (Double) map.get("requestId");
                    int tripId = newData.intValue();
                    String completeTime = (String) map.get("completeTime");
                    String response=firebaseSevice.completeTrip(tripId);
                    result=new Result(response != null,response,response!=null?"Cancel successfully":null);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());
                }
            }
            case "TRACK_DRIVER" -> {
                try {
                    int tripId= new Gson().fromJson(pack4RBMQ.getContent(),Integer.class);
                    Position position=firebaseSevice.trackingDriver(tripId);
                    result=new Result(position != null,position,position!=null?"Tracking successfully":null);
                }catch (Exception e){
                    e.printStackTrace();
                    result=new Result(false,null,e.getMessage());
                }
            }
            default -> result=new Result(false,null,"Something wrong");
        }
        return new Gson().toJson(result);
    }

}
