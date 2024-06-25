package com.example.baseball_back.domain;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "stadium")
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String name;
}
