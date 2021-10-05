package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class SongDTOConverter {
    public Song createSongDtoToSong(CreateSongDTO s) {
        return new Song(
                s.getId(),
                s.getTitulo(),
                s.getArtista(),
                s.getAlbum(),
                s.getAnio()

        );
    }

    public GetSongDTO songToGetSongDto(Song s) {
        return GetSongDTO
                .builder()
                .titulo(s.getTitulo())
                .nombreArtista(s.getArtista().getNombre())
                .album(s.getAlbum())
                .anio(s.getAnio())
                .build();
    }

}
