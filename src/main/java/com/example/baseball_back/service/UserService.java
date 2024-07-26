package com.example.baseball_back.service;

import com.example.baseball_back.domain.MyTeam;
import com.example.baseball_back.domain.Users;
import com.example.baseball_back.dto.MyTeamDTO;
import com.example.baseball_back.exception.NotFoundException;
import com.example.baseball_back.repository.MyTeamRepository;
import com.example.baseball_back.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public MyTeamDTO.ModifyMyTeamResponse modifyMyTeam(String socialId, Long myTeamPk){
        Users user = usersRepository.findBySocialId(socialId)
                .orElseThrow(() -> new NotFoundException("해당 사용자가 존재하지 않습니다."));;
        MyTeam myTeam = myTeamRepository.findById(myTeamPk)
                .orElseThrow(() -> new NotFoundException("해당 팀이 존재하지 않습니다."));
        user.updateMyTeam(myTeam);
        return new MyTeamDTO.ModifyMyTeamResponse(user.getMyTeam().getPk());
    }


    public MyTeamDTO.getMyTeamResponse getMyTeam(String socialId) throws RuntimeException {

        Users user = usersRepository.findBySocialId(socialId)
                .orElseThrow(() -> new NotFoundException("해당 사용자가 존재하지 않습니다."));;

        return new MyTeamDTO.getMyTeamResponse(
                user.getMyTeam().getPk(),
              user.getMyTeam().getName()
        );
    }

}