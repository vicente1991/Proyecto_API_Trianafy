package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.GetPlaylistDTO;
import com.salesianostriana.dam.trianafy.dto.PlaylistDTOConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.repository.PlaylistRepository;
import com.salesianostriana.dam.trianafy.repository.SongRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Playlist", description = "Controller de las playlists")
@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistRepository repository;
    private final PlaylistDTOConverter converter;
    private final SongRepository songRepository;

    @GetMapping("/lists")
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
}
