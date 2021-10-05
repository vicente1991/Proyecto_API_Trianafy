package com.salesianostriana.dam.trianafy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue
    private Long id;
    private String titulo;

    @ManyToOne
    private Artista artista;

    private String album;
    private String anio;

    public Song(Long id, String titulo, String album, String anio) {
        this.id = id;
        this.titulo = titulo;
        this.album = album;
        this.anio = anio;
    }


}
