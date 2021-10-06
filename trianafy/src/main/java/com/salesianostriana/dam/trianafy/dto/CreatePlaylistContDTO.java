package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlaylistContDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private int contador;
}
