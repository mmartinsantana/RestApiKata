package com.example.kata.api_rest.demo.repository;

import com.example.kata.api_rest.demo.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // TODO Pageable?

    public AppUser findByUserName(String userName);

}
