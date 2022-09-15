package com.example.user_service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * com.server.uber_clone.models;
 * Created by Phuoc -19127520
 * Date 01/07/2022 - 02:07 CH
 * Description: ...
 */
@Component
@Getter
@Setter
public class Result implements Serializable {

    private Boolean status;
    private Object data;
    private String message;

    public Result() {
    }

    public Result(Boolean status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
