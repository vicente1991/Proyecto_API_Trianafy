package com.salesianostriana.dam.trianafy.repository;

import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long>{


    @Query("select p from Playlist p where ?1 MEMBER OF p.listaCanciones")
    List<Playlist> listasConCanciones(Song s);

}
