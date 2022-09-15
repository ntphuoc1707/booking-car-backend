package com.example.firebaseservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    private int requestId;
    private Integer userId;
    private Position startAddress;
    private Position destination;
    private String phoneNumber;
    private String createdTime;
    private VehicleAndPrice vehicleAndPrice;
    private DistanceAndTime distanceAndTime;
    private String note;
    private String status;
    private Integer discountId;
    private PaymentType paymentType;
    private UserInfor userInfor;
    private Discount discount;
    private Trip trip;
}