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
                .id(p.getId())
                .build();
    }

    public Playlist createPlaylistDTOToPlaylist(CreatePlaylistDTO cp) {
        return new Playlist(
                cp.getId(),
                cp.getNombre(),
                cp.getDescripcion());

    }

    public GetPlaylistContDTO playlistToGetPlaylistContDTO(Playlist p) {
        return GetPlaylistContDTO
                .builder().
                nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .id(p.getId())
                .contador(0)
                .build();
    }




}
