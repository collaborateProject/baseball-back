package com.example.baseball_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MyTeamDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyTeamRequest{
        @NotNull(message = "마이팀 pk가 null이면 안됩니다.")
        private Long myTeamPk;

    }
}
