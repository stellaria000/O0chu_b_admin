package com.example.mariate.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MoiveDTO {
    private Long  movie_id;
    private String us_title;
    private String kr_title;
    private String genres;
    private String actors;
    private String directors;
    private String runtime;
    private String release_date;
    private String poster_path;
    private String trailer_url ;
    private String overview;
    private String available_on_ott;
    private String ott_logos;
    private char delete_yn;
    private String delete_time;
    private int clicks ;







}
