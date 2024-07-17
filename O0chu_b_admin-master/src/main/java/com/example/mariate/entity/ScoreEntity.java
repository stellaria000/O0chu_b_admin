package com.example.mariate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="score")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rowno;
    private String user_email;
    private Long movie_id;
    private Integer score;
    private LocalDateTime times;

}