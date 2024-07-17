package com.example.mariate.domain;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentDTO {
    private int comment_id;
    private String nickname;
    private int Likes;
    private String update_time;
    private char comment_delete_yn;
    private LocalDateTime comment_delete_time;
    private String comments;
    private long movie_id;
}
