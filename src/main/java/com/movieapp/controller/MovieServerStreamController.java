package com.movieapp.controller;



import com.movieapp.service.MovieServerService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/stream")
public class MovieServerStreamController {

    private final MovieServerService service;

    public MovieServerStreamController(MovieServerService service) {
        this.service = service;
    }

    @GetMapping("/{movie}")
    public Mono<ResponseEntity<Resource>> stream(
            @PathVariable String movie,
            @RequestHeader(value = "Range", required = false) String range
    ) throws IOException {

        Resource video = service.getMovie(movie);
        long length = video.contentLength();

        long start = 0;
        long end = length - 1;

        if (range != null && range.startsWith("bytes=")) {
            String[] parts = range.replace("bytes=", "").split("-");
            start = Long.parseLong(parts[0]);
            if (parts.length > 1 && !parts[1].isEmpty()) {
                end = Long.parseLong(parts[1]);
            }
        }

        long contentLength = end - start + 1;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, Files.probeContentType(video.getFile().toPath()));
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_RANGE,
                "bytes " + start + "-" + end + "/" + length);
        headers.setContentLength(contentLength);

        return Mono.just(
                ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .headers(headers)
                        .body(video)
        );
    }
}
