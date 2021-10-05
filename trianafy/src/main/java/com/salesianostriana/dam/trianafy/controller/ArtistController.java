package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artista;
import com.salesianostriana.dam.trianafy.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artista")
public class ArtistController {

    private final ArtistaRepository artistaRepository;


    @GetMapping("/")
        public ResponseEntity<List<Artista>> findAll(){
        return ResponseEntity
                .ok()
                .body(artistaRepository.findAll());
        }
    @GetMapping("/{id}")
    public ResponseEntity<Artista> findOne(@PathVariable Long id){
        return ResponseEntity
                .of(artistaRepository.findById(id));
    }
    @PostMapping("/")
    public ResponseEntity<Artista> create (@RequestBody Artista nuevo){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistaRepository.save(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artista> edit(
            @RequestBody Artista a,
            @PathVariable Long id) {

        return ResponseEntity.of(
                artistaRepository.findById(id).map(m ->{
                    a.setId(a.getId());
                    a.setNombre(a.getNombre());
                    artistaRepository.save(m);
                    return m;
                })
        );

    }
/*Este metodo borra por el id */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        artistaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
