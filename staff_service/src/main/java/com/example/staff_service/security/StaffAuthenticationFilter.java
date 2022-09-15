package com.example.staff_service.security;


import com.example.staff_service.model.Result;
import com.example.staff_service.model.StaffType;
import com.example.staff_service.service.StaffDAO;
import com.example.staff_service.service.StaffDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * com.server.uber_clone.security.authentication;
 * Created by Phuoc -19127520
 * Date 28/07/2022 - 11:34 SA
 * Description: ...
 */

@Component
public class StaffAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
//    LogRepository logRepository;

    @Autowired
    StaffDAO staffDAO=new StaffDAOImpl();

    @Autowired
    StaffTokenUtils staffTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token=request.getHeader("token");

        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }
        try{
            Integer staffId = staffTokenUtils.getIdFromToken(token);
            if(staffTokenUtils.checkExpired(token))
            {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(new ObjectMapper().writeValueAsString(
                        new Result(
                                false,
                                staffDAO.refreshToken(staffId),
                                "Token has expired and this is new your token"))
                );
                out.flush();

                //response.flushBuffer();
                //filterChain.doFilter(request,response);
                return;
            }
            if(staffId !=-1L) {
                CustomStaffDetails staff= (CustomStaffDetails) staffDAO.loadStaffById(staffId);
                String path=new UrlPathHelper().getPathWithinApplication(request);
                if(!staff.getStaff().getToken().equals(token)){
                    filterChain.doFilter(request,response);
                    return;
                }
                if((StaffAPI.onlyAdminAPI.contains(path) && !staffTokenUtils.getRole(staffId).equals(StaffType.ADMIN))){
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.print(new ObjectMapper().writeValueAsString(
                            new Result(
                                    false,
                                    null,
                                    "Only just Admin has permission"))
                    );
                    out.flush();

                    //response.flushBuffer();
                    //filterChain.doFilter(request,response);
                    return;
                }
                // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(staff, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request,response);

    }
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return new AntPathMatcher().match("/driver/**", request.getServletPath())
//                ||new AntPathMatcher().match("/staff/**", request.getServletPath())
//                || new AntPathMatcher().match("/validate", request.getServletPath())
//                || new AntPathMatcher().match("/sendOTP",request.getServletPath());
//    }
}
