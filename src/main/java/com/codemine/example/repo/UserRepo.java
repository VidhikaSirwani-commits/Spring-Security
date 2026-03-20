package com.codemine.example.repo;

import com.codemine.example.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);

}
