package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDTOConverter {

    public GetPlaylistDTO playlistToGetPlaylistDTO(Playlist p) {
        return GetPlaylistDTO
                .builder().
                nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .build();
    }

    public Playlist createPlaylistDTOToPlaylisy(CreatePlaylistDTO cp) {
        return new Playlist(
                cp.getId(),
                cp.getNombre(),
                cp.getDescripcion());

    }
}
