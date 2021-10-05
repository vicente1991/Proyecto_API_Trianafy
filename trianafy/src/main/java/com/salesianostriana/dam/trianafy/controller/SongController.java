package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongRepository repository;

    @PostMapping("/")
    public ResponseEntity<Song> create(@RequestBody Song nueva){
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(nueva));
    }
}

