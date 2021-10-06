package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.CreateSongDTO;
import com.salesianostriana.dam.trianafy.dto.GetSongDTO;
import com.salesianostriana.dam.trianafy.dto.SongDTOConverter;
import com.salesianostriana.dam.trianafy.model.Artista;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.ArtistaRepository;
import com.salesianostriana.dam.trianafy.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongRepository repository;
    private final ArtistaRepository artistaRepository;
    private final SongDTOConverter converter;

    @GetMapping("/")
    public ResponseEntity<List<GetSongDTO>> findAll() {

        List<Song> canciones = repository.findAll();

        if(canciones.isEmpty()){

            return ResponseEntity.notFound().build();

        } else {

            List <GetSongDTO> resultados = canciones.stream()
                                           .map(converter::songToGetSongDto)
                                           .collect(Collectors.toList());

            return ResponseEntity
                    .ok()
                    .body(resultados);
        }
    }


    /*
   Este metodo ayuda a buscar una sola canción por su id
    */
    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable Long id) {
        return ResponseEntity
                .of(repository.findById(id));
    }


    @PostMapping("/")
    public ResponseEntity<Song> create(@RequestBody CreateSongDTO songDto) {


        if(songDto.getIdartista()==null){
            return ResponseEntity.badRequest().build(); //Código 400, petición errónea
        }

        Song nuevo = converter.createSongDtoToSong(songDto);

        Artista artist = artistaRepository.findById(songDto.getIdartista()).orElse(null);

        nuevo.setArtista(artist);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(nuevo));

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}

