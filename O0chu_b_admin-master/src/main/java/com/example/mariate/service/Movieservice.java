package com.example.mariate.service;

import com.example.mariate.domain.CommentDTO;
import com.example.mariate.domain.MoiveDTO;
import com.example.mariate.domain.RaingDTO;

import java.util.List;

public interface Movieservice {

   public List<MoiveDTO> getmovie();

   public List<MoiveDTO> getmovie2();

   public  List<MoiveDTO> title();
   public int update(String movieName);

   public List<MoiveDTO> detail(String movieName);

   public int addcomment (CommentDTO comment);

   public  List<CommentDTO> getcomment (long movie_id) ;

   public  int likes (int commentId);

   public  int inRaing(RaingDTO Rating);

   public  int samere(String user_email, long movie_id);

   public  int ratingup (RaingDTO rating);

   public  List<MoiveDTO> genres(String genres);

   public  List<MoiveDTO> genres2(List<String> geners);

   public List<MoiveDTO>  newmovie();

   public List<MoiveDTO> guitar(String s);

   public List<MoiveDTO> search(String s);

   public List<MoiveDTO> suggestion();

   public List<MoiveDTO> moviedetail(Long movieId);

   public List<MoiveDTO> newsuggestion(String user_email);

   public  int userRating(String user_email, String movie_id);

   public List<MoiveDTO> withmovie(Long movie_id);

   public List<CommentDTO> paging(int start, int end, String movie_id);

   public int Totalcount(String movie_id);

   public  List<MoiveDTO> genrespaging(int start, int end, String genres);
   public  List<MoiveDTO> twogenrespaging(int start, int end, List<String> genres);
   public  int genrespagingcount(String genres);
   public  int twogenrespagingcount(List<String> genres);

   public  int insertwish(String movie_id, String user_email, char wish);

   public List<Character> wishstatus(String movie_id, String user_eamil) ;

   public List<MoiveDTO> wishlist(String user_email);
   public List<Integer> totalRating(String movie_id);

   //댓글 삭제
   public int updateDeleteStatus(int commentId);

   //상세페이지 수정
   public boolean updateMovie(MoiveDTO updatedMovie);

   //영화 삭제
   public boolean updateDeleteYn(MoiveDTO updatedMovie);

   //휴지통
   public List<MoiveDTO> getDeletedMovies();


   public  List<MoiveDTO> maingenres(String genres);
   public  List<MoiveDTO> maintwogenres(List<String> genres) ;




//   public int wishcheck(String movie_id, String user_email);



}
