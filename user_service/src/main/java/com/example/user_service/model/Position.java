package com.example.user_service.model;

//import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 16/07/2022 - 12:25 SA
 * Description: ...
 */

@Embeddable
@Getter
@Setter
//@IgnoreExtraProperties
public class Position{
    private String address;
    private Double longitude;
    private Double latitude;

    @Override
    public String toString() {
        return "Position{" +
                "address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
