package com.example.staff_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "discount")
public class Discount {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "discountId")
    private int discountId;

    @Column(name = "discountName")
    private String discountName;

    @Column(name = "discountPercent")
    private float discountPercent;

    @Column(name = "startDate")
    private String startDate;

    @Column(name = "endDate")
    private String endDate;

    @Column(name = "quantity")
    private int quantity;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "discountId")
    private List<Request> request;
}
