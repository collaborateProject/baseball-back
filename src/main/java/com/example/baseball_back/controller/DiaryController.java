package com.example.baseball_back.controller;

import com.example.baseball_back.dto.DiaryDTO;
import com.example.baseball_back.exception.NotFoundException;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
import com.example.baseball_back.service.DiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "Diary 컨트롤러", description = "diary 관련 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class DiaryController {

    private final DiaryService diaryService;
    @PostMapping("/diary")
    public ResponseEntity createDiary(@AuthenticationPrincipal PrincipalDetail userDetails,
                                       @RequestBody @Valid DiaryDTO.CreateDiaryRequest request){
        String socialId = (String) userDetails.getMemberInfo().get("socialId");
        diaryService.createDiary(socialId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/diary/iscreate")
    public ResponseEntity<Boolean> getMyTeam(@AuthenticationPrincipal PrincipalDetail userDetails,
                                             @RequestParam String date,
                                             @RequestParam String homeTeam) throws RuntimeException {
        String socialId = (String) userDetails.getMemberInfo().get("socialId");

        // DateTimeFormatter를 사용하여 문자열을 LocalDateTime으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        boolean response = diaryService.isCreateDiary(socialId, dateTime, homeTeam);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/diary")
    public ResponseEntity<List<DiaryDTO.DiaryListResponse>> getShotRecordsList(@AuthenticationPrincipal PrincipalDetail userDetails,@RequestParam String date
                                                                               ){

        String userId = (String)  userDetails.getMemberInfo().get("socialId");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        List<DiaryDTO.DiaryListResponse> diaryList = diaryService.getDiaryList(userId,dateTime);
        return ResponseEntity.ok(diaryList);
    }

    @DeleteMapping("/diary/{diary_pk}")
    public ResponseEntity deleteShotRecord(@PathVariable Long diary_pk){
        try{
            diaryService.deleteDiary(diary_pk);
            return ResponseEntity.noContent().build();
        }
        catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
