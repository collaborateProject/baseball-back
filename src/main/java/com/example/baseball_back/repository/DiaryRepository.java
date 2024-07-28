package com.example.baseball_back.repository;


import com.example.baseball_back.domain.Diary;
import com.example.baseball_back.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
List<Diary> findByUsers(Users users);

}
