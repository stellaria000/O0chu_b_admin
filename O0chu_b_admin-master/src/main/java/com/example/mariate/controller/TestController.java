package com.example.mariate.controller;

import com.example.mariate.domain.CommentDTO;
import com.example.mariate.domain.MoiveDTO;
import com.example.mariate.domain.RaingDTO;
import com.example.mariate.service.Movieservice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private Movieservice movieservice;


    @GetMapping("/test")
    public String test() {

        return "testtest";
    }

    @GetMapping("/getlist")
    public List<MoiveDTO> list() {
        List<MoiveDTO> list = movieservice.getmovie();
        return list;

    }

    @PostMapping("/addClick")
    public String addclick(@RequestBody String requestBody) {


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);
            String movieName = rootNode.get("movie_id").asText();


            int ks = movieservice.update(movieName);
            if (ks == 1) {
                return "success";
            } else if (ks == 0) {
                return "fail";
            } else {
                return "ffff";
            }
        } catch (IOException e) {
            return "Error parsing JSON request body.";
        }


    }

    @GetMapping("/detail/{movieName}")
    public List<MoiveDTO> detail(@PathVariable String movieName) {


        List<MoiveDTO> list = movieservice.detail(movieName);
        return list;

    }

    @PostMapping("/addcomment")
    public String commentadd(@RequestBody CommentDTO comment) {
        int re = movieservice.addcomment(comment);

        if (re == 1) {
            return "success!";
        } else {
            return "false";
        }

    }

    @GetMapping("/getcomment/{movie_id}")
    public List<CommentDTO> getcomment(@PathVariable long movie_id) {


        List<CommentDTO> list = movieservice.getcomment(movie_id);
        return list;

    }

    @PostMapping("/comment/likes/{commentId}")
    public String likes(@PathVariable int commentId) {

        int up = movieservice.likes(commentId);

        if (up == 1) {
            return "success!";
        } else {
            return "false";
        }


    }

    @GetMapping("/movies/getmovie2")
    public List<MoiveDTO> list2() {
        List<MoiveDTO> list2 = movieservice.getmovie2();

        return list2;
    }


    @GetMapping("/movieTitle")
    public List<MoiveDTO> title() {

        List<MoiveDTO> list3 = movieservice.title();
        return list3;

    }

    @PostMapping("/Rating")
    public String inrating(@RequestBody RaingDTO rating) {


        int same = movieservice.samere(rating.getUser_email(), rating.getMovie_id());


        if (same == 0) {
            int re = movieservice.inRaing(rating);

            if (re == 1) {
                return "success";
            } else {
                return "error";
            }

        } else {

            int re = movieservice.ratingup(rating);

            if (re == 1) {
                return "success update!";
            } else {
                return "fail update";
            }

        }


//        else {
//            //upda
//            int ru = movieservice.ratingup(rating.getScore(), rating.getUser_id());
//
//            if(ru==1){
//                return  "success" ;
//            }
//            else{
//                return "fail";
//            }
//        }


    }

    @PostMapping("/total/Rating")
    public List<Integer> totalRating(@RequestBody String requestBody) {
        System.out.println(requestBody);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);
//            String user_email = rootNode.get("user_email").asText();
            String movie_id = rootNode.get("movie_id").asText();
            List<Integer> ks = movieservice.totalRating(movie_id);
            return ks;
        } catch (IOException e) {
            return null;
        }
    }

    @PostMapping("/samre")
    public boolean same(@RequestBody RaingDTO rating) {


        boolean flag = false;

        System.out.println(movieservice.samere(rating.getUser_email(), rating.getMovie_id()));

        int cc = movieservice.samere(rating.getUser_email(), rating.getMovie_id());

        if (cc == 0) {
            flag = true;
        }
        if (cc == 1) {
            flag = false;
        }
        return flag;


    }

    @SneakyThrows
    @GetMapping("/movies/list/{genres}")
    public List<MoiveDTO> genres(@PathVariable String genres) {

        System.out.println(genres);
        String s = URLEncoder.encode(genres, "UTF-8");

        if (("공포 스릴러").equals(genres)) {
            String[] genreArray = genres.split(" ");
            List<String> genreList = Arrays.asList(genreArray);
            List<MoiveDTO> list1 = movieservice.genres2(genreList);

            return list1;
        } else if (("멜로 드라마").equals(genres)) {
            String[] genreArray = genres.split(" ");
            List<String> genreList = Arrays.asList(genreArray);
            List<MoiveDTO> list1 = movieservice.genres2(genreList);

            return list1;
        } else if (("기타").equals(genres)) {
            List<MoiveDTO> list = movieservice.guitar(genres);
            return list;
        } else {
            List<MoiveDTO> list = movieservice.genres(genres);
            return list;

        }


    }

    @PostMapping("/user/Rating")
    public int userRating(@RequestBody String requestBody) {
        System.out.println(requestBody);



        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);
            String user_email = rootNode.get("user_email").asText();
            String movie_id = rootNode.get("movie_id").asText();



            int ks = movieservice.userRating(user_email,movie_id);
            return ks;
        } catch (IOException e) {
            return -1;
        }
    }

    //댓글 삭제
    @DeleteMapping("/comment/updateDeleteStatus/{commentId}")
    public String updateDeleteStatus(@PathVariable int commentId) {
        int result = movieservice.updateDeleteStatus(commentId);


        if (result == 1) {
            return "success";
        } else {
            return "fail";
        }

    }

    //상세페이지 수정
    @PutMapping("/update-movie")
    public ResponseEntity<String> updateMovie(@RequestBody MoiveDTO updatedMovie) {
        try {

            boolean updated = movieservice.updateMovie(updatedMovie);

            if (updated) {
                return ResponseEntity.ok("{ \"message\": \"데이터가 성공적으로 업데이트되었습니다.\" }");
            } else {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{ \"message\": \"데이터 업데이트 중 오류가 발생했습니다.\"}");
            }
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{ \"message\": \"데이터 업데이트 중 오류가 발생했습니다.\"}");
        }
    }

    //영화 삭제
    @PutMapping("/update-delete-yn")
    public String updateDeleteYn(@RequestBody MoiveDTO updatedMovie) {
        try {
            boolean result = movieservice.updateDeleteYn(updatedMovie);
            if (result) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            return "error";
        }
    }

    //휴지통
    @GetMapping("/movies/trash")
    public List<MoiveDTO> getDeletedMovies(){
        List<MoiveDTO> deletedMovies = movieservice.getDeletedMovies();
        return deletedMovies;
    }



}
