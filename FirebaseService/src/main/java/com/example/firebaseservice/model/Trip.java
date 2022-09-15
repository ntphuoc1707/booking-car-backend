package com.example.firebaseservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Trip {

    private int tripId;
    private Position startAddress;
    private Position destination;
    private String createdTime;
    private VehicleAndPrice vehicleAndPrice;
    private DistanceAndTime distanceAndTime;
    private Integer discountId;
    private PaymentType paymentType;
    private Integer userId;
    private String completeTime;
    private Integer driverId;
    private String status;
    //private Feedback feedback;
    private DriverInfor driverInfor;
    private Request request;


    public void setDataRequest(Request request){
        this.setRequest(request);
        this.tripId=request.getRequestId();
        this.userId=request.getUserId();
        this.paymentType=request.getPaymentType();
        if(request.getPaymentType()==null) this.paymentType= PaymentType.CASH;
        else this.paymentType=request.getPaymentType();
        this.distanceAndTime= request.getDistanceAndTime();
        this.discountId=request.getDiscountId();
        this.vehicleAndPrice=request.getVehicleAndPrice();
        this.createdTime=request.getCreatedTime();
        this.startAddress=request.getStartAddress();
        this.destination=request.getDestination();
        this.setStatus("Driver accepted");
    }
    public void setDriverId(int driverId){
        this.driverId=driverId;
    }

}

