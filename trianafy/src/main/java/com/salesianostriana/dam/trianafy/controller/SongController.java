package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongRepository repository;

    @PostMapping("/")
    public ResponseEntity<Song> create(@RequestBody Song nueva){
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> edit(
            @RequestBody Song s,
            @PathVariable Long id) {

        return ResponseEntity.of(
                repository.findById(id).map(m -> {
                    m.setTitulo(s.getTitulo());
                    m.setArtista(s.getArtista());
                    m.setAlbum(s.getAlbum());
                    m.setAnio(s.getAnio());
                    repository.save(m);
                    return m;
                })
        );

    }
}

