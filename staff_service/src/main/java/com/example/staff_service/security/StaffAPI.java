package com.example.staff_service.security;

import java.util.List;

/**
 * com.server.uber_clone.security.path.staff;
 * Created by Phuoc -19127520
 * Date 29/07/2022 - 04:01 CH
 * Description: ...
 */
public record StaffAPI() {
    public static final String[] staffPublicAPI={"/staff/login"};
    public static final List<String> onlyAdminAPI= List.of("/staff/deleteStaff", "/staff/signup");


}
