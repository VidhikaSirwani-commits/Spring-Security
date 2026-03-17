package com.codemine.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    /*
1. we will have default login page only by giving the security dependency
2. we can customize this login UN and PWD by giving inside the properties file
Note: each time login spring will maintain a session
->if we go to different browsers then we will have different instances
->this means we need different session id for each instance. this session id we can see when we do inspect inside the cookies
-> everytime me login the session id will change, but till logged in we will have same session id
     */
    @GetMapping("/")
    public String greet(){
        return "welcome to security";
    }
/*
     we can print the session id also by using the HttpServletRequest because
     internally Spring is working on servlets only.so basically we will have the
     request and response objects of servlet
 */
    @GetMapping("/session-id")
    public String greet1(HttpServletRequest http){
        return "welcome to security "+http.getSession().getId();
    }

    /*
    Now we can change the UN and PWD by adding into the properties file
we have a filter UsernamePasswordAuthenticationFilter this filter will check
the properties file if we have any UN and PWD
if metioned it will take that UN and PWD and not generate its own PWD
check properties file for UN and PWD configuration
-> when trying to login with Postman we will have to select basic auth and
    pass our UN and PWD there then hit the api
     */

    /*
Note: whenever we login the client will send creds to server. server will verify
    and send a session-id along with 200 ok
    now this session-id is with the client
    now each time cient hit the request it will send the session-id
    so now client no need to login again and again only using session-id is enough
    Eg: google once signed it we no need to sign in again and again

    but again we have disadvantage:-
    because any other mallicious website may use same session-id and try to
    get our info. this is bad. this is called Cross Site Request Forgery
    ie Cross Site(other site) Request Forgery(forgering our request through session-id)
     */

    /*  CSRF-> Spring Security will handle this CSRF
http has ->get ->post ->put ->delete
post,put,delete will do modification to data so by default spring will take care of CSRF
by not allowing these operations if we are not handling the CSRF

ways we can secure
1. so now we can have new session-id each time we hit a request. by this now no one can hack
because session-id each time gets changing
2. once sent request to server, server should generate unique token and then now
each time client send request send this token. so now this token is called the
CSRF-Token
check StudentController for csrf
     */
}
