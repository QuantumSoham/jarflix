package com.movieapp.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieServerConfig  {

    @Value("${movies.folder}")
    private String movieFolder;

    public String getMovieFolder() {
        return movieFolder;
    }
}
