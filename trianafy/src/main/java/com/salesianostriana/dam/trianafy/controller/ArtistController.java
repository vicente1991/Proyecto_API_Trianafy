package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artista;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repository.ArtistaRepository;
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
@RequestMapping("/artista")
public class ArtistController {

    private final ArtistaRepository artistaRepository;


    @Operation(summary = "Obtiene una lista con todos los artistas existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado los artistas",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artista.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna lista con artistas",
                    content = @Content),
    })

    @GetMapping("/")
        public ResponseEntity<List<Artista>> findAll(){

        if(artistaRepository.findAll().isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity
                    .ok()
                    .body(artistaRepository.findAll());
        }

    }


    @Operation(summary = "Obtiene un artista de una lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el artista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artista.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el artista indicado",
                    content = @Content),
    })

    @GetMapping("/{id}")
    public ResponseEntity<Artista> findOne(@PathVariable Long id){

        if(artistaRepository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            return ResponseEntity
                    .of(artistaRepository.findById(id));
        }
    }

    @Operation(summary = "Crea un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se creado y se ha guardado el artista",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artista.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido guardar el artista porque hay algún" +
                            "error en los datos indicados",
                    content = @Content),
    })

    @PostMapping("/")
    public ResponseEntity<Artista> create (@RequestBody Artista nuevo){

        if(nuevo.getNombre().isEmpty()){
            return ResponseEntity.badRequest().build();

        } else {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(artistaRepository.save(nuevo));

        }

    }

    @Operation(summary = "Actualiza la información de un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha realizado correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artista.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el artista a modificar",
                    content = @Content),
    })

    @PutMapping("/{id}")
    public ResponseEntity<Artista> edit(
            @RequestBody Artista a,
            @PathVariable Long id) {

        if(artistaRepository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

        }else {
            return ResponseEntity.of(
                    artistaRepository.findById(id).map(m -> {
                        m.setNombre(a.getNombre());
                        artistaRepository.save(m);
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
                            schema = @Schema(implementation = Artista.class))}),

        })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        artistaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
