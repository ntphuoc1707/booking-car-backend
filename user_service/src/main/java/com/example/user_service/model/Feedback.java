package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 18/08/2022 - 10:29 CH
 * Description: ...
 */
@Entity
@Getter
@Setter
@Table(name = "feedback")
public class Feedback {

    @Id
    @Column(name = "tripId")
    private Integer tripId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate")
    private RateStar rateStar;

    @Column(name = "note")
    private String note;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name ="tripId")
    private Trip trip;
}

