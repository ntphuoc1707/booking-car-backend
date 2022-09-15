package com.example.staff_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Request")
public class Request {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "requestId")
    private int requestId;

    @Column(name = "userId")
    private Integer userId;

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


    @Column(name = "phoneNumber")
    private String phoneNumber;

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

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private String status;

    //@OneToOne(fetch = FetchType.EAGER, mappedBy = "Discount")
    //@JoinColumn(name = "request_discount", referencedColumnName = "discountId")
    @Column(name = "discountId")
    private Integer discountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentType")
    private PaymentType paymentType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    private UserInfor userInfor;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "discountId",insertable = false,updatable = false)
    private Discount discount;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Trip trip;
}