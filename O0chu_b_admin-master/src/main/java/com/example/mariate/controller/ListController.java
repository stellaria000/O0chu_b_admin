package com.example.mariate.controller;

import com.example.mariate.domain.CommentDTO;
import com.example.mariate.domain.MoiveDTO;
import com.example.mariate.domain.PagingDTO;
import com.example.mariate.service.Movieservice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ListController {
    @Autowired
    Movieservice movieservice;

    @GetMapping("/newList")
    public List<MoiveDTO> Newmovielist() {

        List<MoiveDTO> list = movieservice.newmovie();

        return list;
    }

    //영화검색 api


    @GetMapping("/movie/search/{search}")
    public List<MoiveDTO> search(@PathVariable String search) {


        List<MoiveDTO> list = movieservice.search(search);
        return list;


    }

    @GetMapping("/movie/suggestion")
    public List<MoiveDTO> suggestion() {
        List<MoiveDTO> list = movieservice.suggestion();

        return list;
    }

    @GetMapping("/movie/getdetail/{movieId}")
    public List<MoiveDTO> detail(@PathVariable Long movieId) {
        List<MoiveDTO> list = movieservice.moviedetail(movieId);

        return list;

    }

    @GetMapping("/movie/newsuggestion")
    public List<MoiveDTO> newsuggestion(@RequestHeader("useremail") String user_email) {
        List<MoiveDTO> list = movieservice.newsuggestion(user_email);

        return list;
    }

    @GetMapping("/movie/withmovie/{movie_id}")
    public List<MoiveDTO> withmovie(@PathVariable Long movie_id) {
        List<MoiveDTO> list = movieservice.withmovie(movie_id);
        return list;
    }


    @PostMapping("/comment/paging")
    public List<CommentDTO> paging(@RequestBody PagingDTO page) {

        //end는 pageno * 5
        // start end -4

        int end = page.getPageNo() * 5;

        int start = end - 4;

        List<CommentDTO> list = movieservice.paging(start, end, page.getMovie_id());

        return list;


    }

    @GetMapping("/comment/{movie_id}")
    public Map<String, Object> paging2(@RequestParam("pageNo") String pageNo, @PathVariable String movie_id) {

        //end는 pageno * 5
        // start end -4

        int end = Integer.parseInt(pageNo) * 5;

        int start = end - 4;

        List<CommentDTO> list = movieservice.paging(start, end, movie_id);

        int total = movieservice.Totalcount(movie_id);

        Map<String, Object> res = new HashMap<>();

        res.put("comment", list);
        res.put("totalcout", total);


        return res;


    }

    @GetMapping("/movie/genres/{genres}")
    public Map<String, Object> gerenspage(@PathVariable String genres, @RequestParam("pageNo") String pageNo) {


        int end = Integer.parseInt(pageNo) * 20;
        int start = end - 19;

        System.out.println(genres);


        Map<String, Object> res = new HashMap<>();


        if (("공포 스릴러").equals(genres) || ("멜로 드라마").equals(genres)) {
            String[] genreArray = genres.split(" ");
            List<String> genreList = Arrays.asList(genreArray);
            List<MoiveDTO> list1 = movieservice.twogenrespaging(start, end, genreList);
            int count = movieservice.twogenrespagingcount(genreList);

            res.put("count", count);
            res.put("item", list1);

            return res;

        } else {
            List<MoiveDTO> list = movieservice.genrespaging(start, end, genres);
            int count = movieservice.genrespagingcount(genres);

            res.put("count", count);
            res.put("item", list);

            return res;


        }


    }

    //wish
    @PostMapping("/wish/addwish")
    public String addwish(@RequestBody String requestBody) {

        System.out.println(requestBody);

        int re = 0;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(requestBody);
            String movie_id = rootNode.get("movie_id").asText();
            String user_email = rootNode.get("user_email").asText();
            String wish = rootNode.get("wish").asText();

            char wi = wish.charAt(0);

            re = movieservice.insertwish(movie_id, user_email, wi);


            if (re == 1 || re == 2) {

                return "success";
            } else {
                return "fail";
            }


        } catch (IOException e) {
            return "Error parsing JSON request body.";
        }
    }

    @GetMapping("/wish/status")
    public List<Character> wishstatus(@RequestParam("movie_id") String movie_id, @RequestParam("user_email") String user_email) {


        List<Character> wish = movieservice.wishstatus(movie_id, user_email);

        if (wish.isEmpty()) {

            List<Character> wi = new ArrayList<Character>();
            wi.add('N');

            return wi;
        } else {

            return wish;
        }


    }

    @GetMapping("/wish/wishlist")
    List<MoiveDTO> wishlist(@RequestParam("user_email") String user_email) {


        List<MoiveDTO> list = movieservice.wishlist(user_email);

        return list;


    }


    @GetMapping("/movie/main/list")
    public List<MoiveDTO> maingenres(@RequestParam("genres") String genres) {


        System.out.println(genres);


        if (("공포 스릴러").equals(genres) || ("멜로 드라마").equals(genres)) {
            String[] genreArray = genres.split(" ");
            List<String> genreList = Arrays.asList(genreArray);

            List<MoiveDTO> list = movieservice.maintwogenres(genreList);

            return list;


//            List<MoiveDTO> list1 = movieservice.twogenrespaging(start, end, genreList);


        } else {
            List<MoiveDTO> list = movieservice.maingenres(genres);

            return list;


        }


    }

}
