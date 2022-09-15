package com.example.driver_service.security;

import com.example.driver_service.model.Driver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * com.server.uber_clone.security.authentication.customDetails;
 * Created by Phuoc -19127520
 * Date 28/07/2022 - 11:27 SA
 * Description: ...
 */

public class CustomDriverDetails implements UserDetails {
    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public CustomDriverDetails(Driver driver) {
        this.driver = driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return driver.getPassword();
    }

    @Override
    public String getUsername() {
        return driver.getUsername();
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