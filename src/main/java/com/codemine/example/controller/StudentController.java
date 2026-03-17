package com.codemine.example.controller;

import com.codemine.example.entity.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    /*
    here we created the get now for Get through Postman will work but
    when it comes to Post it will not work. This happened because of CSRF
     */
    private List<Student> students= new ArrayList<>(List.of(
            new Student(1,"Navin",60),
            new Student(2, "Kiran", 70)
    ));

    @GetMapping("/students")
    public List<Student> getStudents(){
        return students;
    }
/*
only Spring Security is enabled but now Post will not work because of CSRF
Postman is some other client trying to access request which will give 401
for Post Put and Delete same this will give 401

to solve this we have to pass the CSRF-Token in the header of the request
key: C-CSRF-TOKEN    value:???
how to get value? 1. using browser do inspect
2. use the request object and then try to getAttribute("_csrf")

now first get the csrf token then pass in the header and the can create the student

CSRF attacks rely on browser automatically sending cookies.
In JWT-based authentication, the token is sent manually
in the Authorization header, so there is no automatic
credential sharing. Hence CSRF protection is not required.

One more way to avoid CSRF is that use same site strict that means
dont allow other sites to access the apis
 */
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student){
        students.add(student);
        return student;
    }


    @GetMapping("/csrf-token")
    public CsrfToken getCsrftoken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
        //getAttribute will return Object type so have to type caste (Csrftoken)
    }

    /*
now we want to use the DB and do the authentication so we will create a Config class
     */
}
