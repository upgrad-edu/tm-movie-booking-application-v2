package com.upgrad.mtb.controllers;

import com.upgrad.mtb.entity.Movie;
import com.upgrad.mtb.entity.Theatre;
import com.upgrad.mtb.dto.MovieDTO;
import com.upgrad.mtb.exceptions.*;
import com.upgrad.mtb.security.jwt.JwtTokenProvider;
import com.upgrad.mtb.services.CustomerService;
import com.upgrad.mtb.services.MovieService;
import com.upgrad.mtb.services.TheatreService;
import com.upgrad.mtb.utils.DTOEntityConverter;
import com.upgrad.mtb.utils.EntityDTOConverter;
import com.upgrad.mtb.validator.MovieValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MovieController {
  @Autowired
  MovieService movieService;
  @Autowired
  TheatreService theatreService;
  @Autowired
  MovieValidator movieValidator;
  @Autowired
  DTOEntityConverter dtoEntityConverter;
  @Autowired
  EntityDTOConverter entityDTOConverter;
  /**
   * TODO this will be used in the security session
   @Autowired
   JwtTokenProvider jwtTokenProvider;
   **/

  @Autowired
  CustomerService customerService;


  @RequestMapping(value= {"/sayHelloMovie"},method= RequestMethod.GET)
  public ResponseEntity<String> sayHello(){
    return new ResponseEntity<String>("Hello World To All From MovieController", HttpStatus.OK);
  }
  /**
   * TODO : This method will be changed after the security session
   * */
  @PostMapping(value="/movies",consumes= MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
  public ResponseEntity newMovie(@RequestBody MovieDTO movieDTO ) throws APIException, StatusDetailsNotFoundException, LanguageDetailsNotFoundException, TheatreDetailsNotFoundException, CustomerDetailsNotFoundException, MovieDetailsNotFoundException, BadCredentialsException {
    ResponseEntity responseEntity = null;
    /**
     * TODO : after spring security
     * String username = jwtTokenProvider.getUsername(accessToken);
     if(username == null)
     throw new APIException("Please add proper authentication");
     if(!customerService.getCustomerDetailsByUsername(username).getUserType().getUserType().equalsIgnoreCase("Admin"))
     throw new BadCredentialsException("This feature is only available to admin");**/
    try {
      movieValidator.validateMovie(movieDTO);
      Movie newMovie = dtoEntityConverter.convertToMovieEntity(movieDTO);
      Movie savedMovie = movieService.acceptMovieDetails(newMovie);
      MovieDTO savedMovieDTO = entityDTOConverter.convertToMovieDTO(savedMovie);
      responseEntity = ResponseEntity.ok(savedMovieDTO);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return responseEntity;
  }


  @GetMapping("/movies/{id}")
  public ResponseEntity getMovieDetails(@PathVariable(name = "id") int id) throws MovieDetailsNotFoundException, APIException {
    Movie responseMovie = movieService.getMovieDetails(id);
    MovieDTO responseMovieDTO = entityDTOConverter.convertToMovieDTO(responseMovie);
    return ResponseEntity.ok(responseMovieDTO);
  }

  @PutMapping(value="/movies/{id}",consumes= MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
  public ResponseEntity updateMovieDetails(@PathVariable(name = "id") int id, @RequestBody MovieDTO movieDTO,  @RequestHeader(value = "X-ACCESS-TOKEN") String accessToken) throws MovieDetailsNotFoundException, StatusDetailsNotFoundException, LanguageDetailsNotFoundException, APIException, ParseException, TheatreDetailsNotFoundException, CustomerDetailsNotFoundException, BadCredentialsException {
    /**
     * TODO : after spring security
     String username = jwtTokenProvider.getUsername(accessToken);
     if(username == null)
     throw new APIException("Please add authentication");
     if(!customerService.getCustomerDetailsByUsername(username).getUserType().getUserType().equalsIgnoreCase("Admin"))
     throw new BadCredentialsException("This feature is only available to admin");
     **/
    movieValidator.validateMovie(movieDTO);
    Movie newMovie = dtoEntityConverter.convertToMovieEntity(movieDTO);
    Movie updatedMovie = movieService.updateMovieDetails(id, newMovie);
    MovieDTO updatedMovieDTO = entityDTOConverter.convertToMovieDTO(updatedMovie);
    return  ResponseEntity.ok(updatedMovieDTO);
  }

  @GetMapping(value="/movies",produces=MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
  public ResponseEntity getAllMovies(@RequestParam(defaultValue = "-1") int number) throws APIException {
    if(number>0){
      List<Movie> movies = movieService.getAllMoviesDetails();
      if(movies.size() < number) {
        throw new APIException("Invalid number of movies");
      }
      else{
        List<Movie> movieList = movies.subList(Math.max(movies.size() - number, 0), movies.size());
        List<MovieDTO> movieDTOList = new ArrayList<>();
        for(Movie movie : movieList){
          movieDTOList.add(entityDTOConverter.convertToMovieDTO(movie));
        }
        return ResponseEntity.ok(movieDTOList);
      }
    }else {
      List<Movie> movieList = movieService.getAllMoviesDetails();
      List<MovieDTO> movieDTOList = new ArrayList<>();
      for(Movie movie : movieList){
        movieDTOList.add(entityDTOConverter.convertToMovieDTO(movie));
      }
      return ResponseEntity.ok(movieDTOList);
    }
  }

  //inter related APIs
  @GetMapping(value="/movies/{movieId}/theatres",produces=MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
  public ResponseEntity findAllTheatresForMovie( @PathVariable("movieId") int movieId) throws MovieDetailsNotFoundException {
    Movie movie = movieService.getMovieDetails(movieId);
    return ResponseEntity.ok(movie.getTheatres());
  }

  @DeleteMapping(value="/movies/{movieId}/theatres/{theatreId}",produces=MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
  public ResponseEntity removeTheatreForMovie(@PathVariable("movieId") int movieId , @PathVariable("theatreId") int theatreId) throws MovieDetailsNotFoundException, TheatreDetailsNotFoundException {
    Theatre theatre = theatreService.getTheatreDetails(theatreId);
    theatre.setMovies(null);
    Movie movie = movieService.getMovieDetails(movieId);
    List<Theatre> theatres = movie.getTheatres();
    theatres.remove(theatre);
    movie.setTheatres(theatres);
    movieService.updateMovieDetails(movieId, movie);
    return ResponseEntity.ok(theatreService.updateTheatreDetails(theatreId, theatre));
  }

}