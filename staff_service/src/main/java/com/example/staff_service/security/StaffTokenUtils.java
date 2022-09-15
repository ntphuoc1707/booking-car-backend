package com.example.staff_service.security;

import com.example.staff_service.model.Result;
import com.example.staff_service.model.StaffType;
import com.example.staff_service.service.StaffDAO;
import com.example.staff_service.service.StaffDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffTokenUtils {

    @Autowired
    StaffDAO staffDAO=new StaffDAOImpl();

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Boolean checkExpired(String token){
        Result result=jwtTokenProvider.validateToken(token);
        return result.getStatus() && result.getMessage().equals("Expired");
    }

    public Integer getIdFromToken(String token){
        Result result=jwtTokenProvider.validateToken(token);
        return result.getStatus()? (Integer) result.getData() :-1;
    }

    public StaffType getRole(int id){
        CustomStaffDetails staff= (CustomStaffDetails) staffDAO.loadStaffById(id);
        return staff.getStaff().getType();
    }
}
