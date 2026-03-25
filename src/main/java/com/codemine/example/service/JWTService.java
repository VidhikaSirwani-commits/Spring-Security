package com.codemine.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private String secretKey="";

    public JWTService(){
        try {
            //creating instance of KeyGenerator because we can generate key
            KeyGenerator keyGen= KeyGenerator.getInstance("HmacSha256");
            //generating a secret key
            SecretKey sk=keyGen.generateKey();
            //converting that key into string we will use these Class Base64
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public String generateToken(String username) {
//we are creating our claims first so that we can pass in JWT
        Map<String, Object> claims= new HashMap<>();
/*creating our token by building it, previously they did all this using the setter methods but now
all we have to do by building it one by one
Here when building we will use add(), and() and all these functions   */
        return Jwts.builder()
                .claims()
                .add(claims)
                //creating the subject
                .subject(username)
                //create the issue date
                .issuedAt(new Date(System.currentTimeMillis()))
                //create expiration date
                .expiration(new Date(System.currentTimeMillis()+1000*60*30))
                .and() //we want to add one more condition so we used and()
                .signWith(getKey())
                //we are trying to create a signature with the secret key
                .compact();
                //this will generate the JWT Token
    }

    private Key getKey() {
        //now have to convert string secret key into Key type because of the return type as Key
        //but the method uses byte stream so first convert to byte stream and then pass it
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        // the below method used to convert it into Key object
        return Keys.hmacShaKeyFor(keyBytes);
    }

//run the program, generate the token and check the token is valid or not using the JWT website

    public String extracUserName(String token) {
        return extrectClaim(token, Claims::getSubject);
    }

    private <T> T extrectClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        //older way used to work this way
//        return Jwts.parser()
//                .setSigningKey(getKey())
//                .build().parseClaimsJws(token).getBody();
        //newer version after jjwt 0.12
        return Jwts
                .parser()
                .verifyWith((SecretKey) getKey()) // new method
                .build()
                .parseSignedClaims(token)//all are pervious methods but replaced with new names
                .getPayload();
    }

    //now lets check for token validation
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName= extracUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extrectClaim(token, Claims::getExpiration);
    }
}
