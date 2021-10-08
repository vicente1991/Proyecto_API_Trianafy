package com.salesianostriana.dam.trianafy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {


    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "artist", cascade = {CascadeType.PERSIST})
    private List <Song> listaCanciones;

}
