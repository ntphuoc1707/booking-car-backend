package com.example.staff_service.security;

import com.example.staff_service.model.Staff;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * com.server.uber_clone.security.authentication.customDetails;
 * Created by Phuoc -19127520
 * Date 28/07/2022 - 11:27 SA
 * Description: ...
 */

public class CustomStaffDetails implements UserDetails {
    private Staff staff;

    public Staff getStaff() {
        return staff;
    }

    public CustomStaffDetails(Staff staff) {
        this.staff = staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return staff.getPassword();
    }

    @Override
    public String getUsername() {
        return staff.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}