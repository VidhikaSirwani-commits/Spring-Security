package com.codemine.example.service;

import com.codemine.example.entity.Users;
import com.codemine.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

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
}
