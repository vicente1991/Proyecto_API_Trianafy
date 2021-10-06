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
                .numCanciones(p.getListaCanciones().size())
                .build();
    }

    public Playlist createPlaylistDTOToPlaylist(CreatePlaylistDTO cp) {
        return new Playlist(
                cp.getId(),
                cp.getNombre(),
                cp.getDescripcion());

    }

    public Playlist createPlayListContDTOToPlaylist(CreatePlaylistContDTO ccp){
        return new Playlist(
                ccp.getId(),
                ccp.getNombre(),
                ccp.getDescripcion(),
                ccp.getContador());
    }
}
