package com.example.staff_service.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * com.server.uber_clone.dto;
 * Created by Phuoc -19127520
 * Date 03/07/2022 - 03:24 CH
 * Description: ...
 */

@Getter
@Setter
public class StaffDTO implements Serializable {
    private int id;
    private String username;
    private String fullname;
    private String phone;
    private String address;
    private String email;
    private String gender;
    private StaffType type;

    public StaffDTO(Staff staff){
        this.id=staff.getId();
        this.username=staff.getUsername();
        this.type=staff.getType();
        StaffInfor staffInfo = staff.getStaffInfor();
        if(staffInfo != null){
            fullname = staffInfo.getFullname();
            phone = staffInfo.getPhone();
            address = staffInfo.getAddress();
            email = staffInfo.getEmail();
            gender = staffInfo.getGender();
        }
    }
}
