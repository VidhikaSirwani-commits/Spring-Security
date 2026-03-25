package com.codemine.example.config;

import com.codemine.example.service.JWTService;
import com.codemine.example.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    ApplicationContext context;
    @Autowired
    private JWTService jwtService;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        //from header of the request we are trying to take the Authorization part
//        String authHeader= request.getHeader("Authorization");
///* we need this so that from the Authorization we will only store the token
//because Authorization contains something like this
//Bearer ey...(token value)...
//so we need to remove the Bearer and the space and then only store the token */
//        String token=null;
////username we need so that we can do the authentication with this username
//        String username=null;
//
//
//        if (authHeader != null && authHeader.startsWith("Bearer "))
//        {
////we are removing the Bearer and only accessing the token
//            token=authHeader.substring(7);
////we will try to get the username from the token, but token is in an unreable format
////we need to read and access the token, so we will create a seperate method only for it
////we will create this in our service
//            username=jwtService.extracUserName(token);
//        }
//
//// now we will check if username is not null and check if the object is already authenticated or not?
//        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
////the SecurityContextHolder we will use to check if object is authenticated or not
//        {
////we have to validate the token so for this we will create a method in jwtService
////this service requires a UserDetails object so we are creating this using ApplicationContext
////why ApplicationContext is because we if we directly autowired then it will create a circular depenency
//            UserDetails userDetails= context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
////if token is valid token that means we have pass the control to next filter which will be
////UsernamePasswordAuthenticationFilter
//            if(jwtService.validateToken(token,userDetails))
//            {
///*to go to the next filter we will have to pass a token for that which will be the UPAT
//so we will create that first
//this UsernamePasswordAuthenticationToken will ask for 3 things
//1. principal    2. credentials   3. Authorities */
//                UsernamePasswordAuthenticationToken authToken=
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////we need to set the authToken once generated
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//            filterChain.doFilter(request,response);
//        }
//    }


//above comments helps explain each line what the code does
@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = jwtService.extracUserName(token);
    }
    if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

        UserDetails userDetails =
                context.getBean(MyUserDetailsService.class)
                        .loadUserByUsername(username);

        if (jwtService.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
    filterChain.doFilter(request, response);
}
}
