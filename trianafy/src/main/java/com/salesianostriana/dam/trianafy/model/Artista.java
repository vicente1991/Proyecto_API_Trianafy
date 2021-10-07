package com.salesianostriana.dam.trianafy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artista {


    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @OneToMany
    private List<Song> canciones = new ArrayList<>();

    public void addCancion(Song c) {canciones.add(c);}
    public void deleteCancion(Long id){
        for(Song cancion: canciones){
            if (cancion.getId()==id) canciones.remove(cancion);
        }


    }


}
