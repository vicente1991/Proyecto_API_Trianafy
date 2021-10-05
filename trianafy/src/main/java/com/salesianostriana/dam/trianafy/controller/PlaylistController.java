package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.CreatePlaylistDTO;
import com.salesianostriana.dam.trianafy.dto.GetPlaylistDTO;
import com.salesianostriana.dam.trianafy.dto.PlaylistDTOConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.PlaylistRepository;
import com.salesianostriana.dam.trianafy.repository.SongRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tag(name = "Playlist", description = "Controller de las playlists")
@RestController
@RequiredArgsConstructor

@RequestMapping("/lists")

public class PlaylistController {

    private final PlaylistRepository repository;
    private final PlaylistDTOConverter converter;
    private final SongRepository songRepository;

    @GetMapping("/")
    public ResponseEntity<List<GetPlaylistDTO>> findAll(){

        List<Playlist> lista = repository.findAll();

        if(lista.isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            List <GetPlaylistDTO> resultado = lista.stream()
                                              .map(converter::playlistToGetPlaylistDTO)
                                              .collect(Collectors.toList());

            return ResponseEntity.ok().body(resultado);
        }
    }


    @PostMapping("/")
    public ResponseEntity<Playlist> add(@RequestBody CreatePlaylistDTO p){

        if(p.getNombre().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Playlist nueva = converter.createPlaylistDTOToPlaylist(p);

        //Devuelve una playlist sin canciones todavía)

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(nueva));

    }

    @PutMapping("/")
    public ResponseEntity<Playlist> edit(
        @RequestBody Playlist p,
        @PathVariable Long id){
            return ResponseEntity.of(repository.findById(id).map(m -> {
            m.setNombre(p.getNombre());
            m.setDescripcion(p.getDescripcion());
            repository.save(m);
            return m;
        }));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> delete(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/song/{id}")
    public ResponseEntity<Stream<Song>> findSongOfPlayList(@PathVariable Long idList, @PathVariable Long idSong){
        return ResponseEntity.of(repository.findById(idList).map(m ->{
            return (m.getListaCanciones().stream().filter(song -> song.getId().equals(idSong)));
        }));

    }
}

