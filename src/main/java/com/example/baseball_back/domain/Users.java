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
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private Long socialId;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createAt= LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "myteam_pk")
    private MyTeam myTeam;
}
