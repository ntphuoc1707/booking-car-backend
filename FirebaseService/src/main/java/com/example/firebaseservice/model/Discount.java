package com.example.firebaseservice.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Discount {

    private int discountId;
    private String discountName;
    private float discountPercent;
    private String startDate;
    private String endDate;
    private int quantity;
    private List<Request> request;
}
