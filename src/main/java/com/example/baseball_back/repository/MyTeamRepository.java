package com.example.baseball_back.repository;

import com.example.baseball_back.domain.MyTeam;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MyTeamRepository  extends JpaRepository<MyTeam, Long> {


}
