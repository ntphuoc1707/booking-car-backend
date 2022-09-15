package com.example.otp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * com.example.otp.config;
 * Created by Phuoc -19127520
 * Date 31/08/2022 - 08:07 SA
 * Description: ...
 */
@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/otp/**");
    }
}
