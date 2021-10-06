package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class SongDTOConverter {
    public Song createSongDtoToSong(CreateSongDTO s) {
        return new Song(
                s.getTitulo(),
                s.getAlbum(),
                s.getAnio()
        );
    }

    public GetSongDTO songToGetSongDto(Song s) {
        GetSongDTO result = new GetSongDTO();
        result.setTitulo(s.getTitulo());
        result.setAlbum(s.getAlbum());
        result.setArtista(s.getArtista().getNombre());
        result.setAnio(s.getAnio());
        return result;
    }

}
