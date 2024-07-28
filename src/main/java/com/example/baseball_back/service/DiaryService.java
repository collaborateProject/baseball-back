package com.example.baseball_back.service;

import com.example.baseball_back.domain.Diary;
import com.example.baseball_back.domain.Icon;
import com.example.baseball_back.domain.Users;
import com.example.baseball_back.dto.DiaryDTO;
import com.example.baseball_back.exception.NotFoundException;
import com.example.baseball_back.repository.DiaryRepository;
import com.example.baseball_back.repository.IconRepository;
import com.example.baseball_back.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final UsersRepository usersRepository;
    private final IconRepository iconRepository;
    private final DiaryRepository diaryRepository;

    public void createDiary(String socialId, DiaryDTO.CreateDiaryRequest createDiaryRequest){

        Users users = usersRepository.findBySocialId(socialId)
                .orElseThrow(() -> new NotFoundException("해당 사용자가 존재하지 않습니다."));

        Icon icon = iconRepository.findById(createDiaryRequest.getIconPk())
                .orElseThrow(() -> new NotFoundException("해당 아이콘이 존재하지 않습니다."));

        Diary diary = Diary.builder()
                .users(users)
                .date(createDiaryRequest.getDate())
                .stadium(createDiaryRequest.getStadium())
                .homeTeam(createDiaryRequest.getHomeTeam())
                .awayTeam(createDiaryRequest.getAwayTeam())
                .homeTeamScore(createDiaryRequest.getHomeTeamScore())
                .awayTeamScore(createDiaryRequest.getAwayTeamScore())
                .review(createDiaryRequest.getReview())
                .watch(createDiaryRequest.getWatchType())
                .icon(icon)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        diaryRepository.save(diary);

    }
}
