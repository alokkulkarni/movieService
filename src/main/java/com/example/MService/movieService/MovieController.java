package com.example.MService.movieService;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movieList")
public class MovieController{


	private static List<Movie> movieList = Arrays.asList(new Movie("Ghostbusters", "1984"),
			  									  new Movie("X-Men", "2003"),
												  new Movie("Avengers", "2010"),
			 									  new Movie("Kunfu Panda", "2014"),
			 									  new Movie("Angry Birds", "2015"));

	@GetMapping("")
	public List<Movie> getMovieAllList() {
		return this.movieList;
	}


	@PostMapping(value = "/movie", consumes = "application/json")
	public List<Movie> createMovie(@RequestBody Movie movie) {
		
		List<Movie> movies;
		movies = movieList.stream().collect(Collectors.toList());
		movies.add(movie);
		return movies;
	}
}
