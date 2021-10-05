package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Artista;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSongDTO {

    private Long id;
    private String titulo;
    private Artista artista;
    private String album;
    private String anio;



}
