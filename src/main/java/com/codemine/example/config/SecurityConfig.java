package com.codemine.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//by enabling this we tell Spring to follow our given security and not the
//one which spring is giving
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

/*
In Security Config here we will give our Beans so that we can customize the security
By default Spring will work on SecurityFilterChain so its like
one by one the filters will execute. so first we will create a bean for it

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http.build();
    }

when only given this much we are removing all the security from the application
means the login form will also disapper because it is not doing any filter in the method
 */

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http){
//        /*
//Steps we follow
//1. disable csrf. we can do this in 2 ways
//-> before we would do it using csrf().disable()
//-> now need to write lambda csrf(csrf-> csrf.disable())
//
//behind the scenes this is happening
//// we will see that this is a functional interface and this only we convert to lambda
//// by doing this we have disabled csrf
//        Customizer<CsrfConfigurer<HttpSecurity>> custCsrf= new Customizer<CsrfConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CsrfConfigurer<HttpSecurity> customizer) {
//                customizer.disable();
//            }
//        };
//        http.csrf(custCsrf);
//         */
//        http.csrf(customizer-> customizer.disable());
////even after doing the above step still i will be able to access the url without login form
///*
//now the step we will do will give us the login form. This is done by authoririzing
//the statment below means authorize the http request.
//for any request coming in authenticate them
//even after doing the step below you send UN and PWD we will get 403 forbidden
//because we are sending the UN and PWD but no where are we using it in our code
//so how will the security know what to do with it
//for that we will use the formlogin and try to use the default creds
// */
//        http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
///*
//creating the form login
//note: the http.formLogin(Customizer.withDefaults()); will create a form login for browser
//with Postman when you try to hit you will get the loginform as response in Postman
//so for Postman we need to change the Login a bit
// */
//        //http.formLogin(Customizer.withDefaults());
//        http.httpBasic(Customizer.withDefaults()); // this works for Postman
///* Now we will make our http stateless because as discussed previously we are
//disabling the csrf then we no need to maintain the session
//Note: now when we did the session then on brower it will not work with formlogin
//because each time request comes we need to send credentials so in brower to work
//first diable form login then in browser it will work
//Now when each time we hit the request we get a new session id
// */
//        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
////   now all this we can do method chaining and it will be easy using Builder Pattern
//

        //this http.build(); will return the object of SecurityFilterChain
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http.csrf(customizer-> customizer.disable())
                .authorizeHttpRequests(request-> request.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

/* this code will be used to authenticate user from the DB
so its like a Service layer when we can customize our code and write logics for the DB
bascially UsernamePasswordAuthenticatedFiler will handle the UN and PWD
but how spring works like controller service and repo, the UserDetailsService is a Service layer
where we will tell spring use this logics to get UN and PWD and not use the properties file UN and PWD

but UserDetailsService is an interface so cannot create object of it
we will create object of InMemoryUserDetailsManager which indirectly implements UserDetailsService
 */
//    @Bean
//    public UserDetailsService userDetailsService(){
///*
//only by returning this object it will not be able to authenticate because we are not verifying
//in our code what exactly is the logic
//
//so InMemoryDetailsManager has a constructor where it will take many users and then try to take
//many UserDetails as input so now we can put our users and authenticate them
//Note: again this UserDetails is an interface so now we need a class object
//    User is a inbuild Spring class which implements UserDetails so we will use that to build our object
//
// */
//        UserDetails user1= User
//                //we first need to encode what every password we pass, cannot pass plain string
//                .withDefaultPasswordEncoder()
//                .username("kiran")
//                .password("k@123")
//                .roles("USER")
//                .build(); // this build() is returning the UserDetails object
//
//        UserDetails user2= User
//                //we first need to encode what every password we pass, cannot pass plain string
//                .withDefaultPasswordEncoder()
//                .username("harsh")
//                .password("h@123")
//                .roles("ADMIN")
//                .build();
//        //Once objects are built then pass them in the constructor
//        // now these users what we built those creds will work
//        return new InMemoryUserDetailsManager(user1,user2);
//    }
/* the above whole thing is using hardcoded value which we do not need
we need to work with the DB so the above method will also not work */


/*When we pass the UN and PWD, then an Authentication Object is created which is still unauthorized
To authorize this AuthenticationProvider will do it
so now need to create a bean of AuthenticationProvider and customize it
but again AuthenticationProvider is an interface so now need a class which will implement it
DaoAuthenticationProvider is the implementation class which we will usually use for DB
so we can create the object and return it

 */
    @Bean
    public AuthenticationProvider authenticationProvider(){
/* the userDetailsService can connect with Db so we need it and we pass into constructor
 but again this is an interface so we need to use a class
 but we do not have a customized class so we can create our implement class and then use it
 so we will create that now
 * */
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider(userDetailsService);
        //now still no DB connection is made so first need to make that
        // create a default password encoder
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        return provider;
    }
}
