package com.movieapp.service;

import com.movieapp.config.MovieServerConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServerService {

    private final MovieServerConfig config;

    public MovieServerService(MovieServerConfig config) {
        this.config = config;
    }

    public List<String> listMovies() {
        File folder = new File(config.getMovieFolder());

        if (!folder.exists() || !folder.isDirectory())
            return List.of();

        return Arrays.stream(folder.listFiles())
                .filter(f -> f.isFile() && (f.getName().endsWith(".mp4") || f.getName().endsWith(".mkv") ))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public Resource getMovie(String name) {
        return new FileSystemResource(
                new File(config.getMovieFolder(), name)
        );
    }
}
