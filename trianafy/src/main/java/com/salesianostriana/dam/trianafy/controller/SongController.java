package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.CreateSongDTO;
import com.salesianostriana.dam.trianafy.dto.GetSongDTO;
import com.salesianostriana.dam.trianafy.dto.SongDTOConverter;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.ArtistRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Song", description = "Controller de las canciones")
@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongRepository repository;
    private final ArtistRepository artistRepository;
    private final SongDTOConverter converter;
    private final PlaylistRepository playlistRepository;


    @Operation(summary = "Obtiene una lista con todos las canciones existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las canciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista con canciones",
                    content = @Content),
    })
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


    @Operation(summary = "Obtiene una canci??n a partir de su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las canciones",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la canci??n buscada",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity findOne(@PathVariable Long id) {

        if(repository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            return ResponseEntity
                    .of(repository.findById(id));
        }
    }

    @Operation(summary = "Crea una canci??n nueva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha creado y guardado la canci??n",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha guardado la canci??n porque ha habido un error" +
                            "en los datos enviados",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Song> create(@RequestBody CreateSongDTO songDto) {

        if(songDto.getIdartista()==null){
            return ResponseEntity.badRequest().build(); //C??digo 400, petici??n err??nea
        }

        Song nuevo = converter.createSongDtoToSong(songDto);
        Artist artist = artistRepository.findById(songDto.getIdartista()).orElse(null);

        nuevo.setArtist(artist);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(nuevo));

    }

    @Operation(summary = "Editamos una canci??n existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha guardado la canci??n actualizada",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha guardado la canci??n porque ha habido un error" +
                            "en los datos enviados",
                    content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Song> edit(
            @RequestBody Song s,
            @PathVariable Long id) {

        if(repository.findById(id).isEmpty()){
            return ResponseEntity.badRequest().build();

        } else {
            return ResponseEntity.of(
                    repository.findById(id).map(m -> {
                        m.setTitulo(s.getTitulo());
                        m.setArtist(s.getArtist());
                        m.setAlbum(s.getAlbum());
                        m.setAnio(s.getAnio());
                        repository.save(m);
                        return m;
                    })
            );
        }
    }


    @Operation(summary = "Borramos una canci??n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))})
            })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        
        Optional<Song> theSong = repository.findById(id);

        if (theSong.isPresent()) {

            List <Playlist> lists = playlistRepository.listasConCanciones(theSong.get());
            if (!lists.isEmpty()) {
                //lists.remove(theSong.get());
                for(Playlist p : lists) {
                    p.getListaCanciones().remove(theSong.get());
                    playlistRepository.save(p);
                }

            }
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();

    }
}

