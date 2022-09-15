package com.example.user_service.security;

/**
 * com.server.uber_clone.security.path.user;
 * Created by Phuoc -19127520
 * Date 29/07/2022 - 03:56 CH
 * Description: ...
 */
public record UserAPI() {
    public static final String[] userPublicAPI={
            "/user/login",
            "/user/signup",
            "/user/checkPhonenumber",
            "/user/checkEmail"};
}
