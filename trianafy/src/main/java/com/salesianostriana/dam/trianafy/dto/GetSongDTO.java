package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetSongDTO {
    private Long id;
    private String titulo;
    private Long nombreArtista;
    private String album;
    private String anio;
}
