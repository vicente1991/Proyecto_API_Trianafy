package com.salesianostriana.dam.trianafy.repository;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
