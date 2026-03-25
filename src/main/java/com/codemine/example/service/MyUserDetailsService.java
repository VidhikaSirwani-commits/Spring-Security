package com.codemine.example.service;

import com.codemine.example.entity.UserPrincipal;
import com.codemine.example.entity.Users;
import com.codemine.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
/*
here we got the implemented method loadUserByUsername(). Now this username we will get from Repo
we will create a repo layer and get our data from there
*/
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
// find the user based on the username
        Users users= userRepo.findByUsername(username);
        System.out.println(">>> loadUserByUsername called with: " + username);
        if (users==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }

/*  now here we have to return a UserDetails, but this is an interface
So our approach will be to create an implementation class and return that
in our project we have given it as UserPrincipal because Principal will refer to the current user
 */
        return new UserPrincipal(users);
//        return null;
    }
}
