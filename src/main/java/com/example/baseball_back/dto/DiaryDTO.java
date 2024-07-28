package com.example.baseball_back.dto;

import com.example.baseball_back.domain.Icon;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class DiaryDTO {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiaryRequest{
        @NotNull(message = "날짜가 null이면 안됩니다")
        private LocalDateTime date;

        @NotNull(message = "경기장이 null이면 안됩니다.")
        private String stadium;

        @NotNull(message = "홈팀이 null이면 안됩니다.")
        private String homeTeam;

        @NotNull(message = "어웨이팀 null이면 안됩니다.")
        private String awayTeam;

        @NotNull(message = "홈팀 점수가 null이면 안됩니다.")
        private int homeTeamScore;

        @NotNull(message = "원정팀 점수가 null이면 안됩니다.")
        private int awayTeamScore;

        @NotNull(message = "관람 타입이 null이면 안됩니다.")
        private Boolean watchType;

        private String review;

        @NotNull(message = "아이콘이 null이면 안됩니다.")
        private Long iconPk;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryListRequest{
        private Long pk;
        private String stadium;
        private String homeTeam;
        private String awayTeam;
        private int homeTeamScore;
        private int awayTeamScore;
        private String review;
        private Boolean watch;
        private Icon icon;
        public static DiaryListRequest Records(Long pk, String stadium, String homeTeam, String awayTeam,int homeTeamScore, int awayTeamScore, String review, Boolean watch, Icon icon){
            return new DiaryListRequest(pk, stadium, homeTeam,awayTeam,homeTeamScore,awayTeamScore,review,watch,icon );
        }


    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiaryListResponse{
        private Long pk;
        private String stadium;
        private String homeTeam;
        private String awayTeam;
        private int homeTeamScore;
        private int awayTeamScore;
        private String review;
        private Boolean watch;
        private Long iconPk;
    }

}
