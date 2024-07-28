package com.example.baseball_back.controller;

import com.example.baseball_back.dto.DiaryDTO;
import com.example.baseball_back.dto.MyTeamDTO;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
import com.example.baseball_back.service.DiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
