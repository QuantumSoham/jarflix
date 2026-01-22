package com.movieapp.controller;


import com.movieapp.service.MovieServerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieServerMovieController {

    private final MovieServerService service;

    public MovieServerMovieController(MovieServerService service) {
        this.service = service;
    }

    @GetMapping("/movies")
    public List<String> listMovies() {
        return service.listMovies();
    }
}
