package com.salesianostriana.dam.trianafy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToMany
    @ElementCollection
    private List<Song> listaCanciones;

    public Playlist(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Playlist(Long id, String nombre, String descripcion, int contador) {
    }
}
