package com.example.user_service.security;

import com.example.user_service.model.Result;
import com.example.user_service.service.UserDAO;
import com.example.user_service.service.UserDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
public class UserAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
//    LogRepository logRepository;

    @Autowired
    UserDAO userDAO=new UserDAOImpl();

    @Autowired
    private UserTokenUtils userTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path=new UrlPathHelper().getPathWithinApplication(request);
//        if(path.equals("/user/login") || path.equals("/user/signup")) {
//            filterChain.doFilter(request,response);
//            return;
//        };
//        try{
//            Log log = new Log();
//            log.setLogMessage(this.toString());
//            log.setSource(request.getRequestURI());
//            log.setTime(LocalDateTime.now().toString());
//            logRepository.save(log);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        String token=request.getHeader("token");
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);
            return;
        }
        try{
            Integer userId = userTokenUtils.getIdFromToken(token);
            if(userTokenUtils.checkExpired(token))
            {
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(new ObjectMapper().writeValueAsString(
                        new Result(
                                false,
                                userDAO.refreshToken(userId),
                                "Token has expired and this is new your token"))
                );
                out.flush();
                out.close();
                //response.flushBuffer();
                //filterChain.doFilter(request,response);
                return;
            }
            if(userId !=-1) {
                CustomUserDetails user= (CustomUserDetails) userDAO.loadUserById(Math.toIntExact(userId));
                if(!user.getUser().getToken().equals(token)){
                    filterChain.doFilter(request,response);
                    return;
                }
                // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(user, null, null);
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
