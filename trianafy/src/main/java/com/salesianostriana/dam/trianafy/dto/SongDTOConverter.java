package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongDTOConverter {
    public Song createSongDtoToSong(CreateSongDTO s) {
        return new Song(
                s.getId(),
                s.getTitulo(),
                s.getArtistaiD(),
                s.getAlbum(),
                s.getAnio()
        );
    }

}
