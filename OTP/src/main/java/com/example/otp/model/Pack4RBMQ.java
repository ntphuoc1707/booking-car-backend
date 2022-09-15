package com.example.otp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 18/08/2022 - 06:05 CH
 * Description: ...
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Pack4RBMQ implements Serializable {
    private String function;
    private String content;
    private String token;

    public Pack4RBMQ(String function, String content, String token) {
        this.function = function;
        this.content = content;
        this.token = token;
    }
}
