package com.example.baseball_back.controller;


import com.example.baseball_back.dto.MyTeamDTO;
import com.example.baseball_back.jwt.dto.PrincipalDetail;
import com.example.baseball_back.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "User 컨트롤러", description = "user 관련 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/myteam")
    public ResponseEntity createMyTeam(@AuthenticationPrincipal PrincipalDetail userDetails,
   @RequestBody @Valid MyTeamDTO.MyTeamRequest request){
        String socialId = (String) userDetails.getMemberInfo().get("socialId");
        userService.createMyTeam(socialId, request.getMyTeamPk());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/myteam")
    public ResponseEntity modifyMyTeam(@AuthenticationPrincipal PrincipalDetail userDetails,
                                       @RequestBody @Valid MyTeamDTO.MyTeamRequest request){
        String socialId = (String) userDetails.getMemberInfo().get("socialId");
        MyTeamDTO.ModifyMyTeamResponse response = userService.modifyMyTeam(socialId, request.getMyTeamPk());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/myTeam")
    public ResponseEntity<MyTeamDTO.getMyTeamResponse> getMyTeam(@AuthenticationPrincipal PrincipalDetail userDetails) throws RuntimeException{
        String socialId = (String) userDetails.getMemberInfo().get("socialId");
        MyTeamDTO.getMyTeamResponse response = userService.getMyTeam(socialId);
        return ResponseEntity.ok(response);
    }


}
