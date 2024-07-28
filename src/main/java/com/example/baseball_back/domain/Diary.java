package com.example.baseball_back.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "diary")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_pk")
    private Users users;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private String stadium;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createAt= LocalDateTime.now();

    @NotNull
    private String homeTeam;

    @NotNull
    private String awayTeam;

    @NotNull
    private int homeTeamScore;

    @NotNull
    private int awayTeamScore;

    private String review;

    @NotNull
    @CreationTimestamp
    private LocalDateTime updateAt = LocalDateTime.now();

    @NotNull
    private Boolean watch;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "icon_pk")
    private Icon icon;




}
