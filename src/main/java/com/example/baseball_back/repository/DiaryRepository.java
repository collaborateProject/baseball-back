package com.example.baseball_back.repository;


import com.example.baseball_back.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {


}
