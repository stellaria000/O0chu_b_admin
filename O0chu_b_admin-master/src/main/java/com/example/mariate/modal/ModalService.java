package com.example.mariate.modal;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ModalService {

    private final JdbcTemplate jdbcTemplate;
    String API_KEY = "84b60af94cdba5e1b0e2897e704fa28a";
    String BASE_URL = "https://api.themoviedb.org/3";
    String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    String genreUrl = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;

    @Autowired
    public ModalService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveMovieInfoToMariaDB(MovieInfo2 movieInfo2) {
        String insertSQL = "INSERT INTO movie.movie_info ( MOVIE_ID, us_title, KR_TITLE, genres, ACTORS, DIRECTORS, runtime, release_date, poster_path, overview,trailer_url, available_on_ott, ott_logos) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(insertSQL,
                movieInfo2.getMovieId(),
                movieInfo2.getUsTitle(),
                movieInfo2.getKoreanTitle(),
                movieInfo2.getGenres(),
                movieInfo2.getActors(),
                movieInfo2.getDirectors(),
                movieInfo2.getRuntime(),
                movieInfo2.getReleaseDate(),
                movieInfo2.getPosterPath(),
                movieInfo2.getOverview(),
                movieInfo2.getTrailerUrl(),
                movieInfo2.getAvailableOnOTT(),
                movieInfo2.getOttLogos()

        );

    }

    public MovieInfo2 getMovieInfo(JSONObject movie) {


        int movieId = movie.getInt("id");

        String usTitle = requestMovieTitle(movieId, "en-US", API_KEY);
        String koreanTitle = requestMovieTitle(movieId, "ko-KR", API_KEY);

        System.out.println("us_title: " + usTitle);
        System.out.println("korea_title: " + koreanTitle);

        //장르 정보 가져오기

        String genres = "";
        JSONArray genresArray = getGenresArray(genreUrl, movie);
        for (int i = 0; i < genresArray.length(); i++) {
            String englishGenre = genresArray.getJSONObject(i).getString("name");
            String koreanGenre = mapGenreToKorean(englishGenre);
            genres += koreanGenre;
            if (i < genresArray.length() - 1) {
                genres += ", ";
            }
        }

        // 상세 정보 가져오기
        String detailsResponse = sendGetRequest(BASE_URL + "/movie/" + movieId + "?api_key=" + API_KEY + "&language=ko-KR");
        JSONObject detailsData = new JSONObject(detailsResponse);
        int runtime = detailsData.getInt("runtime");
        String releaseDate = detailsData.getString("release_date");
        String posterPath = POSTER_BASE_URL + detailsData.getString("poster_path");
        String overview = detailsData.getString("overview");
        System.out.println("Overview: " + overview);

        // 크레딧 정보 가져오기 (배우 및 감독)
        String creditsResponse = sendGetRequest(BASE_URL + "/movie/" + movieId + "/credits?api_key=" + API_KEY);
        JSONObject creditsData = new JSONObject(creditsResponse);


        JSONArray cast = creditsData.getJSONArray("cast");
        JSONArray crew = creditsData.getJSONArray("crew");
        String actors = "";
        String directors = "";

        for (int i = 0; i < cast.length(); i++) {
            actors += cast.getJSONObject(i).getString("name");
            if (i < cast.length() - 1) {
                actors += ", ";
            }
        }

        for (int i = 0; i < crew.length(); i++) {
            JSONObject crewMember = crew.getJSONObject(i);
            if ("Director".equals(crewMember.getString("job"))) {
                directors += crewMember.getString("name");
                if (i < crew.length() - 1) {
                    directors += ", ";
                }
            }
        }

        //trailerUrl 가져오기
        String trailerUrl = "";
        String videosUrl = BASE_URL + "/movie/" + movieId + "/videos?api_key=" + API_KEY;

        String videosResponse = sendGetRequest(videosUrl);
        JSONObject videosData = new JSONObject(videosResponse);
        JSONArray results = videosData.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject video = results.getJSONObject(i);
            if ("Trailer".equals(video.getString("type"))) {
                trailerUrl = "https://www.youtube.com/embed/" + video.getString("key");
                break;
            }
        }

        System.out.println("trailerUrl :" + trailerUrl);

        // OTT 정보 가져오기
        String watchProvidersUrl = BASE_URL + "/movie/" + movieId + "/watch/providers?api_key=" + API_KEY;
        String watchProvidersResponse = sendGetRequest(watchProvidersUrl);
        JSONObject watchProvidersData = new JSONObject(watchProvidersResponse);

        // availableOnOTT 정보 얻기
        JSONObject watchProviders = watchProvidersData.getJSONObject("results");

        String ottLogos = "";
        String availableOnOTT = "";
        if (watchProviders.has("KR")) {

            JSONObject krWatchProviders = watchProviders.getJSONObject("KR"); // 한국 정보


            if (krWatchProviders.has("flatrate")) {
                JSONArray flatrate = krWatchProviders.getJSONArray("flatrate");

                for (int i = 0; i < flatrate.length(); i++) {
                    JSONObject ottPlatform = flatrate.getJSONObject(i);
                    String platformName = ottPlatform.getString("provider_name");


                    availableOnOTT += platformName;
                    if (i < flatrate.length() - 1) {
                        availableOnOTT += ", ";
                    }
                }
            }


            // ottLogos 정보 얻기
            if (krWatchProviders.has("flatrate")) {
                JSONArray flatrate = krWatchProviders.getJSONArray("flatrate");

                for (int i = 0; i < flatrate.length(); i++) {
                    JSONObject ottPlatform = flatrate.getJSONObject(i);
                    String logoUrl = ottPlatform.getString("logo_path");
                    // 로고 URL을 사용하여 이미지를 표시하거나 저장할 수 있습니다.
                    ottLogos = "http://image.tmdb.org/t/p/original/" + ottLogos + logoUrl;
                    if (i < flatrate.length() - 1) {
                        ottLogos = "http://image.tmdb.org/t/p/original/" + ottLogos;
                    }
                }
            }
        }



        MovieInfo2 movieInfo2 = new MovieInfo2(
                movieId,
                usTitle,
                koreanTitle,
                genres,
                runtime,
                releaseDate,
                posterPath,
                overview,
                actors,
                directors,
                trailerUrl,
                availableOnOTT,
                ottLogos

        );
        System.out.println(movieInfo2);
        return movieInfo2;
    }

    private String requestMovieTitle(int movieId, String language, String apiKey) {
        try {
            String apiUrl = BASE_URL + "/movie/" + movieId + "?api_key=" + apiKey + "&language=" + language;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject movieData = new JSONObject(response.body());
                return movieData.optString("title");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null; // 에러 발생 시 null 반환
    }

    public String mapGenreToKorean(String englishGenre){
        switch (englishGenre) {
            case "Action":
                return "액션";
            case "Adventure":
                return "모험";
            case "Comedy":
                return "코미디";
            case "Crime":
                return "범죄";
            case "Drama":
                return "드라마";
            case "Fantasy":
                return "판타지";
            case "Horror":
                return "공포";
            case "Mystery":
                return "미스터리";
            case "Science Fiction":
                return "SF";
            case "Thriller":
                return "스릴러";
            case "Western":
                return "서부";
            case "Romance":
                return "로맨스";
            case "Family":
                return "가족";
            case "Animation":
                return "애니메이션";
            case "Music":
                return "음악";
            case "History":
                return "역사";
            case "War":
                return "전쟁";
            case "Documentary":
                return "다큐멘터리";
            default:
                return englishGenre;
        }
    }




    private JSONArray getGenresArray(String genreUrl, JSONObject movie) {
        try {
            String genreResponse = sendGetRequest(genreUrl);
            JSONObject genreData = new JSONObject(genreResponse);
            if (genreData.has("genres")) {
                JSONArray genresList = genreData.getJSONArray("genres");
                JSONArray movieGenres = new JSONArray();

                JSONArray movieGenreIds = movie.optJSONArray("genre_ids");
                if (movieGenreIds != null) {
                    for (int i = 0; i < movieGenreIds.length(); i++) {
                        int genreId = movieGenreIds.getInt(i);
                        for (int j = 0; j < genresList.length(); j++) {
                            JSONObject genre = genresList.getJSONObject(j);
                            if (genre.getInt("id") == genreId) {
                                movieGenres.put(genre);
                                break;
                            }
                        }
                    }
                }
                return movieGenres;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray(); // 에러 발생 시 빈 JSONArray 반환
    }

    private static String sendGetRequest(String urlString) {
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


