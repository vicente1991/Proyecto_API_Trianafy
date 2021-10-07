package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.*;
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

    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método devuelve todas las playlist existentes
     */
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


    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método devuelve una playlist a traves de la id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findOne(@PathVariable Long id){

        return ResponseEntity
                .of(repository.findById(id));

    }

    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método añade una nueva playlist
     */
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

    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método edita una playlist a partir de una id
     */

    @PutMapping("/{id}")
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



    @PostMapping("/{idPlaylist}/songs/{idSong}")
    public ResponseEntity<Playlist> addSongToPlaylist(@RequestBody Playlist playlist, @PathVariable Long idPlaylist,
                                                @PathVariable Long idSong) {

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


    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método se le pasa una id de una cancion y busca ese id en la lista de canciones
     */
    @GetMapping("/{id}/songs")
    public ResponseEntity<List<GetSongDTO>> findSongsOfPlaylist(@PathVariable Long idLista){

        Optional<Playlist> optPlaylist = repository.findById(idLista);

        if(optPlaylist.isEmpty()){
            return ResponseEntity.notFound().build();
        } else{
            Playlist playlist = optPlaylist.get();
            List <GetSongDTO> resultado = playlist
                    .getListaCanciones()
                    .stream()
                    .map(songDTOConverter::songToGetSongDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(resultado);
        }

    }


    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método borra una lista de canciones a traves de la id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> delete(@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * @author Juan Carlos Ardana, Maria Inmaculada Dominguez, Vicente Rufo
     * @since v1 5/10/2021
     * @return Este método busca con la id de la canción dentro de la lista de canciones
     */
    @GetMapping("/{id1}/song/{id2}")
    public ResponseEntity<Song> findSongOfPlayList(@PathVariable ("id1") Long idList, @PathVariable ("id2") Long idSong){
        /*return ResponseEntity.of(repository.findById(idList)
                        .map(m -> (m.getListaCanciones()
                        .stream().filter(song -> song.getId().equals(idSong)))
        ));*/

        Optional <Playlist> opLista = repository.findById(idList);

        Playlist lista = opLista.get();

        /*return ResponseEntity
                .of(lista.getListaCanciones().*/

        if(lista.getListaCanciones().stream().map(s -> s.getId()).anyMatch(x -> x == idSong)) {
            return ResponseEntity.of(songRepository.findById(idSong));
        } else {
            return ResponseEntity.notFound().build();
        }



    }
}

