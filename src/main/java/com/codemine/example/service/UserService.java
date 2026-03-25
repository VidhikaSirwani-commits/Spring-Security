package com.codemine.example.service;

import com.codemine.example.entity.Users;
import com.codemine.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    //we are creating the passwordEncoder with a hashing rounds as 12
    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);

    public Users registerUser(Users user){
        // we actually store using the conversion of DTO to entity but not doing it here
/* when the user is registering he is also sending his UN and PWD so now what we can do is that
before storing the user into the db we will first getPassword(), then encode it using the
BCryptPasswordEncoder's object, then set the decrypted password and then save the object
 */
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
/* note after the hashing which we had given as 12 we get this
"$2a$12$607jXiM./mmpdnQ1aFaZgejDfsZvom2dOS4mdwm97EdwYkC.F0IG6",
see at the begininning we have 2a this is something
but next we have $12->this refers to number of rounds of hashing
 */

/* remember this now when we try to hit a request with the user details in our basic auth
it will not work..
because when saving the user details we have hashed and saved it but when we passing we are not
passing the hash value so its a bit problematic
to solve this we can do one thing which is when authenticating also we will decrypt the password
which is sent and then compare both the decrypted values. this will work
check the Security Config Class
 */
    }

 /* how are we going to verify?
 before this AuthenticationManger the login flow was happening automatically because
 AuthenticationManager was working but now we are changing the flow
 So now we have the AuthenticationManager object, use this object and get hold on Authentication object
 use this Authentication object and now can Authenticate the User
  */

    //pervious method
//    public String verify(Users user) {
///* Using the AuthenticationManager do the Authentication
//but now how and what to authenticate
// first the user details what we get we have to authenticate that
// */
////inside authenticate() we have to pass Authentication check in the function
//        Authentication authentication =
//                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
///* Note here from the authentication we are passing the Authentication bject which is not authenticated yet
//and in return we are getting the Authenticated user back inside the Authentication */
//
////Now check if the authentication object returned is authenticated user or not
//        if(authentication.isAuthenticated())
//            return jwtService.generateToken(user.getUsername());
//
//        return "fail";
//    }

    // testing if this method works?
public String verify(Users user) {
    try {
        System.out.println("USERNAME INPUT: " + user.getUsername());
        System.out.println("PASSWORD INPUT: " + user.getPassword());

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword()
                        )
                );

        System.out.println("AUTH OBJECT: " + authentication);
        System.out.println("IS AUTHENTICATED: " + authentication.isAuthenticated());

        return jwtService.generateToken(authentication.getName());

    } catch (Exception e) {
        System.out.println("❌ ERROR TYPE: " + e.getClass().getSimpleName());
        System.out.println("❌ ERROR MSG: " + e.getMessage());
        e.printStackTrace();
        return "fail";
    }
}
}
