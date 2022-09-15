package com.example.driver_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Trip")
public class Trip {

    @Id
    @Column(name = "tripId")
    private int tripId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "startAddress")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longPickingAddr")),
            @AttributeOverride(name = "latitude", column = @Column(name = "latPickingAddr"))
    })
    private Position startAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "destination")),
            @AttributeOverride(name = "longitude", column = @Column(name = "longArriveAddr")),
            @AttributeOverride(name = "latitude", column = @Column(name = "latArriveAddr"))
    })
    private Position destination;

    @Column(name = "createdTime")
    private String createdTime;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "vehicleType", column = @Column(name = "vehicleType")),

            @AttributeOverride(name = "price", column = @Column(name = "price")),
    })
    private VehicleAndPrice vehicleAndPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="distance", column = @Column(name = "distance")),
            @AttributeOverride(name="timeSecond", column = @Column(name = "timeSecond"))
    })
    private DistanceAndTime distanceAndTime;
    //@OneToOne(fetch = FetchType.EAGER, mappedBy = "Discount")
    //@JoinColumn(name = "request_discount", referencedColumnName = "discountId")
    @Column(name = "discountId")
    private Integer discountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentType")
    private PaymentType paymentType;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "completeTime")
    private String completeTime;


    @Column(name = "driverId")
    private Integer driverId;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Feedback feedback;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "driverId",insertable = false,updatable = false)
    private DriverInfor driverInfor;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "tripId",insertable = false,updatable = false)
    private Request request;


    public void setDataRequest(Request request){
        this.setRequest(request);
        this.tripId=request.getRequestId();
        this.userId=request.getUserId();
        this.paymentType=request.getPaymentType();
        if(request.getPaymentType()==null) this.paymentType=PaymentType.CASH;
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

