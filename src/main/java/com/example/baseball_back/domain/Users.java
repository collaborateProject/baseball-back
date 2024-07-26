package com.example.baseball_back.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

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
    private String socialId;

    @org.jetbrains.annotations.NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="role_type")
    private RoleType roleType = RoleType.USER;

    @CreationTimestamp
    private LocalDateTime createAt= LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "myteam_pk")
    private MyTeam myTeam;

    public void updateMyTeam(MyTeam myTeam){
        this.myTeam = myTeam;
    }
}
