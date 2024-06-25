package com.example.baseball_back.repository;

import com.example.baseball_back.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository  extends JpaRepository<Users, Long> {

   Optional<Users> findBySocialId(String socialId);

}
