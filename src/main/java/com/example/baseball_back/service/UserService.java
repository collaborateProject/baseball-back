package com.example.baseball_back.service;

import com.example.baseball_back.domain.MyTeam;
import com.example.baseball_back.domain.Users;
import com.example.baseball_back.exception.NotFoundException;
import com.example.baseball_back.repository.MyTeamRepository;
import com.example.baseball_back.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class UserService {
    private final MyTeamRepository myTeamRepository;
    private final UsersRepository usersRepository;

    public void createMyTeam(String socialId, Long myTeamPk) {
        Users user = usersRepository.findBySocialId(socialId)
                .orElseThrow(() -> new NotFoundException("해당 사용자가 존재하지 않습니다."));;
        MyTeam myTeam = myTeamRepository.findById(myTeamPk)
                 .orElseThrow(() -> new NotFoundException("해당 팀이 존재하지 않습니다."));
         user.updateMyTeam(myTeam);

       usersRepository.save(user);
    }
}