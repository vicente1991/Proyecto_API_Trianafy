package com.salesianostriana.dam.trianafy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    //@ElementCollection
    //@ManyToMany
    //private List <Song> listaCanciones;
}
