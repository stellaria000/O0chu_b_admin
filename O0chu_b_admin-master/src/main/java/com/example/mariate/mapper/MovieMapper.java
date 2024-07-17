package com.example.mariate.mapper;

import com.example.mariate.domain.CommentDTO;
import com.example.mariate.domain.MoiveDTO;
import com.example.mariate.domain.RaingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MovieMapper {

    public List<MoiveDTO> getlist ();

    public List<MoiveDTO> getlist2();

    public  List<MoiveDTO> title();
    public int updatecnt (String movie_id);

    public List<MoiveDTO> getdetail (String movieName);

    public  int addcomment (CommentDTO comment);

    public List<CommentDTO> getcomment(long movie_id) ;

    public  int likes(int commentId);

    public  int inRating(RaingDTO Rating);

    public  List<RaingDTO> samere (String user_email, long movie_id);
    //업데이트

    public  int ratingup (RaingDTO rating);

    public  List<MoiveDTO> genres(String genres);
    public  List<MoiveDTO> gonpogenres(List<String> genres);

    public List<MoiveDTO>  guitar(String s);

    public List<MoiveDTO>  Newmovie();

    public List<MoiveDTO> moviesearch(String s);

    public List<MoiveDTO> suggestion();

    public List<MoiveDTO> moviedetail(Long movieId);


    public List<MoiveDTO> newsuggestion(String user_email);


    public  int userRating (String user_email, String  movie_id);

    public List<MoiveDTO> withmovie(Long moive_id) ;

    public List<CommentDTO> paging(int start, int end, String movie_id) ;


    public int TotalCount(String movie_id);

    public List<MoiveDTO> genresPaging(int start, int end, String genres);
    public List<MoiveDTO> twogenresPaging(int start, int end, List<String> genres);

    public int generespagingcout(String geners) ;
    public int twogenerespagingcout(List<String> genres) ;


    public int insertwish(String movie_id, String user_email, char wish) ;

    public  List<Character> wishstatus(String movie_id, String user_email) ;

    public List<MoiveDTO> wishlist(String user_email) ;

    public List<Integer> totalRating (String  movie_id);

    //댓글 삭제
    public int updateDeleteStatus(int commentId);

    //상세페이지 수정
    public boolean updateMovie(MoiveDTO updatedMovie);

    //영화 삭제
    public boolean updateDeleteYn(MoiveDTO updatedMovie);

    //휴지통
    public List<MoiveDTO> getDeletedMovies();

//    public List<String> wishcheck(String movie_id, String user_email);


    public List<MoiveDTO> maingenres(String genres) ;
    public  List<MoiveDTO> maintwogenres(List<String> genres);









}
