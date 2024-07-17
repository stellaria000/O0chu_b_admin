package com.example.mariate.modal;


import lombok.Getter;

@Getter
public class MovieInfo2 {

    private int movieId;
    private String usTitle;
    private String koreanTitle;
    private String genres;
    private int runtime;
    private String releaseDate;
    private String posterPath;
    private String overview;
    private String actors;
    private String directors;
    private String trailerUrl;
    private String availableOnOTT;
    private String ottLogos;

    public MovieInfo2(int movieId, String usTitle, String koreanTitle, String genres, int runtime, String releaseDate, String posterPath, String overview, String actors, String directors, String trailerUrl, String availableOnOTT, String ottLogos) {
        this.movieId = movieId;
        this.usTitle = usTitle;
        this.koreanTitle = koreanTitle;
        this.genres = genres;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
        this.actors = actors;
        this.directors = directors;
        this.trailerUrl = trailerUrl;
        this.availableOnOTT = availableOnOTT;
        this.ottLogos = ottLogos;

    }

}


