package com.example.mariate.service;

import com.example.mariate.domain.CommentDTO;
import com.example.mariate.domain.MoiveDTO;
import com.example.mariate.domain.RaingDTO;
import com.example.mariate.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieserviceImpl implements Movieservice {
    @Autowired
    private MovieMapper movieMapper;

    @Override
    public List<MoiveDTO> getmovie() {
        return movieMapper.getlist();

    }

    @Override
    public List<MoiveDTO> getmovie2() {
        return movieMapper.getlist2();
    }

    @Override
    public List<MoiveDTO> title() {
        return movieMapper.title();
    }

    @Override
    public int update(String movieName) {
        int cnt = 0;
        cnt = movieMapper.updatecnt(movieName);


        return cnt;
    }

    @Override
    public List<MoiveDTO> detail(String movieName) {

        return movieMapper.getdetail(movieName);


    }

    @Override
    public int addcomment(CommentDTO comment) {

        int result = movieMapper.addcomment(comment);

        return result;
    }

    @Override
    public List<CommentDTO> getcomment(long movie_id) {

        return movieMapper.getcomment(movie_id);


    }

    @Override
    public int likes(int commentId) {

        int  up= movieMapper.likes(commentId);
        return up;
    }

    @Override
    public int inRaing(RaingDTO Rating) {
        int inra = movieMapper.inRating(Rating);

        return  inra;
    }

    @Override
    public int samere(String user_email, long movie_id) {

        List<RaingDTO> list = movieMapper.samere(user_email, movie_id);

        if(list.isEmpty()){
            return 0;
        }
        else {
            return 1;

        }

    }

    @Override
    public int ratingup(RaingDTO rating) {

        int ru = movieMapper.ratingup(rating);




        return ru;
    }

    @Override
    public List<MoiveDTO> genres(String genres) {

        List<MoiveDTO> list = movieMapper.genres(genres);



        return list;
    }

    @Override
    public List<MoiveDTO> genres2(List<String> geners) {

        List<MoiveDTO> list = movieMapper.gonpogenres(geners);
        return list;
    }

    @Override
    public List<MoiveDTO> newmovie() {

        List<MoiveDTO> list = movieMapper.Newmovie();
        return list;
    }

    @Override
    public List<MoiveDTO> guitar(String s) {

        List<MoiveDTO> list = movieMapper.guitar(s);
        return list;
    }

    @Override
    public List<MoiveDTO> search(String s) {

        List<MoiveDTO> list = movieMapper.moviesearch(s);

        return list ;




    }

    @Override
    public List<MoiveDTO> suggestion() {

        List<MoiveDTO> list = movieMapper.suggestion();
        return list;
    }

    @Override
    public List<MoiveDTO> moviedetail(Long movieId) {

        List<MoiveDTO> list = movieMapper.moviedetail(movieId);
        return list;
    }

    @Override
    public List<MoiveDTO> newsuggestion(String user_email) {


        List<MoiveDTO> list = movieMapper.newsuggestion(user_email) ;
        return list;
    }



    @Override
    public int userRating(String user_email, String movie_id) {

        int re = movieMapper.userRating(user_email, movie_id);
        return  re;

    }

    @Override
    public List<MoiveDTO> withmovie(Long movie_id) {

        List<MoiveDTO> list = movieMapper.withmovie(movie_id);
        return list;
    }

    @Override
    public List<CommentDTO> paging(int start, int end, String movie_id) {


        List<CommentDTO> list = movieMapper.paging(start, end, movie_id) ;


        return list;
    }

    @Override
    public int Totalcount(String movie_id) {


        int count = movieMapper.TotalCount(movie_id);
        return count;
    }

    @Override
    public List<MoiveDTO> genrespaging(int start, int end, String genres) {

        List<MoiveDTO> list = movieMapper.genresPaging(start,end, genres);

        return  list;
    }

    @Override
    public List<MoiveDTO> twogenrespaging(int start, int end, List<String> genres) {

        List<MoiveDTO> list = movieMapper.twogenresPaging(start,end,genres);


        return list;
    }

    @Override
    public int genrespagingcount(String genres) {

        int count = movieMapper.generespagingcout(genres) ;
        return count;
    }

    @Override
    public int twogenrespagingcount(List<String> genres) {
        int count = movieMapper.twogenerespagingcout(genres);
        return count;
    }

    @Override
    public int insertwish(String movie_id, String user_email, char wish) {


        int result = movieMapper.insertwish(movie_id,user_email,wish) ;

        System.out.println(result);
        return result;
    }

    @Override
    public List<Character> wishstatus(String movie_id, String user_eamil) {


        return movieMapper.wishstatus(movie_id,user_eamil);
    }

    @Override
    public List<MoiveDTO> wishlist(String user_email) {


        return  movieMapper.wishlist(user_email) ;

    }

    @Override
    public List<Integer> totalRating(String movie_id) {

        List<Integer> re = movieMapper.totalRating(movie_id);
        return  re;

    }


    //댓글 삭제
    @Override
    public int updateDeleteStatus(int commentId) {

        int result = movieMapper.updateDeleteStatus(commentId);
        return result;
    }

    //상세페이지 수정
    @Override
    public boolean updateMovie(MoiveDTO updatedMovie) {

        boolean result = movieMapper.updateMovie(updatedMovie);
        return result;
    }

    //영화 삭제
    @Override
    public boolean updateDeleteYn(MoiveDTO updatedMovie) {

        boolean result = movieMapper.updateDeleteYn(updatedMovie);
        return result;
    }

    //휴지통
    public List<MoiveDTO> getDeletedMovies() {
        List<MoiveDTO> deletedMovies = movieMapper.getDeletedMovies();
        return deletedMovies;
    }

    @Override
    public List<MoiveDTO> maingenres(String genres) {

        List<MoiveDTO> list = movieMapper.maingenres(genres);

        return  list ;


    }

    @Override
    public List<MoiveDTO> maintwogenres(List<String> genres) {

        List<MoiveDTO> list = movieMapper.maintwogenres(genres);
        return  list;
    }


}
