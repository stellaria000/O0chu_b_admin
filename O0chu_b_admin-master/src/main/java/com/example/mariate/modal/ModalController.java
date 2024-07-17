package com.example.mariate.modal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


@Controller
@RestController
@RequestMapping("/api")
public class ModalController {

    @Autowired
    private ModalService modalService;

    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam("SEARCH_QUERY") String searchQuery) {



        try {
            String encodedSearchQuery = URLEncoder.encode(searchQuery, "UTF-8");

            String API_KEY = "84b60af94cdba5e1b0e2897e704fa28a";
            String BASE_URL = "https://api.themoviedb.org/3";


            // 영화 검색 및 결과 가져오는 코드

            String response = sendGetRequest(BASE_URL + "/search/movie?api_key=" + API_KEY + "&language=ko-KR&query=" + encodedSearchQuery);
            JSONObject movieData = new JSONObject(response);
            System.out.println(movieData);
            JSONObject movie = null;

            if (movieData.getJSONArray("results").length() != 0) {
                movie = movieData.getJSONArray("results").getJSONObject(0);
                // 영화 정보를 가져온 후, MariaDB에 저장
                MovieInfo2 movieInfo2 = modalService.getMovieInfo(movie);
                modalService.saveMovieInfoToMariaDB(movieInfo2);
                // 여기서 SEARCH_QUERY를 사용하여 TMDB API에 요청을 보내고 결과를 처리
                if (movieInfo2 != null) {
                    return ResponseEntity.ok("검색 성공!");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("검색 실패");
                }
            } else {
                return ResponseEntity.ok("없는 데이터입니다.");
            }
        }
        catch(
                UnsupportedEncodingException e)

        {
            // 인코딩 예외를 처리하도록 포함하여, 예를 들어 에러 응답을 반환합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("searchQuery를 인코딩하는 중 에러 발생");
        }
    }

    private static String sendGetRequest (String urlString){


        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

}

