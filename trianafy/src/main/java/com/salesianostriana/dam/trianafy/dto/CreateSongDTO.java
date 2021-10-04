package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSongDTO {

    private Long id;
    private String titulo;
    private Long artistaiD;
    private String album;
    private String anio;



}
