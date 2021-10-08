package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.*;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.PlaylistRepository;
import com.salesianostriana.dam.trianafy.repository.SongRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.Optional;
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
    private final SongDTOConverter songDTOConverter;

    @Operation(summary = "Obtiene todas las listas de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las listas",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<List<GetPlaylistContDTO>> findAll(){

        List<Playlist> lista = repository.findAll();

        if(lista.isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            List <GetPlaylistContDTO> resultado = lista.stream()
                                              .map(converter::playlistToGetPlaylistContDTO)
                                              .collect(Collectors.toList());

            return ResponseEntity.ok().body(resultado);
        }
    }


    @Operation(summary = "Se obtiene una lista de reproducción a través de su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la lista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findOne(@PathVariable Long id){
        if(repository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            return ResponseEntity
                    .of(repository.findById(id));
        }
    }

    @Operation(summary = "Crea una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado y guardado la lista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha encontrado ninguna lista",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Playlist> add(@RequestBody CreatePlaylistDTO p){

        if(p.getNombre().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Playlist nueva = converter.createPlaylistDTOToPlaylist(p);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(nueva));

    }

    @Operation(summary = "Edita una lista de reproducción a través de su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha cambiado correctamente la lista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista",
                    content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Playlist> edit(
        @RequestBody Playlist p,
        @PathVariable Long id){

        if(repository.findById(id).isEmpty()){
            return ResponseEntity.badRequest().build();

        } else {
            return ResponseEntity.of(repository.findById(id).map(m -> {
                m.setNombre(p.getNombre());
                m.setDescripcion(p.getDescripcion());
                repository.save(m);
                return m;
            }));
        }
    }

    @Operation(summary = "Inserta una cancion dentro de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido la canción a la lista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista o canción con ese id",
                    content = @Content),
    })
    @PostMapping("/{idPlaylist}/songs/{idSong}")
    public ResponseEntity<Playlist> addSongToPlaylist(
            @RequestBody Playlist playlist,
            @PathVariable Long idPlaylist,
            @PathVariable Long idSong)
    {

        Optional <Playlist> lista = repository.findById(idPlaylist);
        Optional <Song> cancion = songRepository.findById(idSong);


        if ( lista.isEmpty() || cancion.isEmpty()){
            return ResponseEntity.badRequest().build();
        }else {
            lista.get().getListaCanciones().add(cancion.get());
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(repository.save(lista.get()));
        }


    }

    @Operation(summary = "Obtiene las canciones de dentro de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la lista con las canciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista",
                    content = @Content),
    })
    @GetMapping("/{id}/songs")
    public ResponseEntity <List<GetSongDTO>> findSongsOfPlaylist(@PathVariable Long id){

        Optional<Playlist> optPlaylist = repository.findById(id);

        if(optPlaylist.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {

            Playlist playlist = optPlaylist.get();
            List <GetSongDTO> resultado = playlist
                    .getListaCanciones()
                    .stream()
                    .map(songDTOConverter::songToGetSongDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(resultado);
        }

    }


    @Operation(summary = "Borra una lista de  reproducción a través de su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado la lista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))})

            })

    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> deletePlaylist(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Obtiene una cancion de una de las listas reproducción a través de sus ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la canción de la lista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la canción dentro de la lista",
                    content = @Content),
    })

    @GetMapping("/{idList}/songs/{idSong}")
    public ResponseEntity<Song> findSongOfPlayList(@PathVariable Long idList, @PathVariable Long idSong){

        Optional <Playlist> opLista = repository.findById(idList);

        Playlist lista = opLista.get();
        
        if(lista.getListaCanciones().stream().map(s -> s.getId()).anyMatch(x -> x == idSong)) {
            return ResponseEntity.of(songRepository.findById(idSong));

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("{idPlaylist}/songs/{idSong}")

    public ResponseEntity<Playlist> deleteSong(@RequestBody Playlist playlist,@PathVariable Long idPlaylist, @PathVariable Long idSong) {

        Optional <Playlist> lista = repository.findById(idPlaylist);

        if (lista.isEmpty() ||
                !lista.get().getListaCanciones().contains(songRepository.getById(idSong))) {

            return ResponseEntity.notFound().build();

        } else {

            lista.get().getListaCanciones().remove(songRepository.findById(idSong).get());
            repository.save(lista.get());

            return ResponseEntity.noContent().build();
        }
    }
}

