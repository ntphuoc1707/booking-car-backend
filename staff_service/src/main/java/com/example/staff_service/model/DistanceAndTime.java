package com.example.staff_service.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 21/07/2022 - 11:22 SA
 * Description: ...
 */
@Embeddable
@Getter
@Setter
public class DistanceAndTime {

    private double distance;
    private int timeSecond;
}
