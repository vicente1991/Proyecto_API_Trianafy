package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.repository.ArtistRepository;
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

@Tag(name = "Artist", description = "Controller de los artistas")
@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistRepository artistRepository;


    @Operation(summary = "Obtiene una lista con todos los artistas existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado los artistas",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista con artistas",
                    content = @Content),
    })

    @GetMapping("/")
        public ResponseEntity<List<Artist>> findAll(){

        if(artistRepository.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity
                    .ok()
                    .body(artistRepository.findAll());
        }

    }


    @Operation(summary = "Obtiene un artista de una lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el artista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el artista indicado",
                    content = @Content),
    })

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findOne(@PathVariable Long id){

        if(artistRepository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            return ResponseEntity
                    .of(artistRepository.findById(id));
        }
    }

    @Operation(summary = "Crea un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se creado y se ha guardado el artista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido guardar el artista porque hay algún" +
                            "error en los datos indicados",
                    content = @Content),
    })

    @PostMapping("/")
    public ResponseEntity<Artist> create (@RequestBody Artist nuevo){

        if(nuevo.getNombre().isEmpty()){
            return ResponseEntity.badRequest().build();

        } else {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(artistRepository.save(nuevo));

        }

    }

    @Operation(summary = "Actualiza la información de un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha realizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el artista a modificar",
                    content = @Content),
    })

    @PutMapping("/{id}")
    public ResponseEntity<Artist> edit(
            @RequestBody Artist a,
            @PathVariable Long id) {

        if(artistRepository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            return ResponseEntity.of(

                    artistRepository.findById(id).map(m -> {
                        m.setNombre(a.getNombre());
                        artistRepository.save(m);

                        return m;
                    })
            );
        }
    }

    @Operation(summary = "Borra un artista por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha realizado correctamente el borrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),

        })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
