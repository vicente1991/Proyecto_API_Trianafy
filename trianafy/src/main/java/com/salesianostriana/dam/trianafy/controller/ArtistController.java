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


    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este metodo devuelve todos los artistas creados
     */
    @GetMapping("/")
        public ResponseEntity<List<Artista>> findAll(){
        return ResponseEntity
                .ok()
                .body(artistaRepository.findAll());
        }
    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este metodo devuelve un artista
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artista> findOne(@PathVariable Long id){
        return ResponseEntity
                .of(artistaRepository.findById(id));
    }

    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este metodo crea un artista
     */
    @PostMapping("/")
    public ResponseEntity<Artista> create (@RequestBody Artista nuevo){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistaRepository.save(nuevo));
    }

    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este metodo edita los artistas a partir del id
     */
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
    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este metodo borra el artista a partir del id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        artistaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
