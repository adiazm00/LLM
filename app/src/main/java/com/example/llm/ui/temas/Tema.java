package com.example.llm.ui.temas;

import java.util.ArrayList;

public class Tema {
    String nombre, description, dificultad;
    ArrayList<String> estudiantes = new ArrayList<>(), ejercicios = new ArrayList<>(), insignias = new ArrayList<>();

    public Tema(String nombre, String description, String dificultad, ArrayList<String> estudiantes, ArrayList<String> ejercicios, ArrayList<String> insignias) {
        this.nombre = nombre;
        this.description = description;
        this.dificultad = dificultad;
        this.estudiantes = estudiantes;
        this.ejercicios = ejercicios;
        this.insignias = insignias;
    }

    public Tema(){}

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public ArrayList<String> getEstudiantes() {return estudiantes;}

    public void setEstudiantes(ArrayList<String> estudiantes) {this.estudiantes = estudiantes;}

    public ArrayList<String> getEjercicios() {return ejercicios;}

    public void setEjercicios(ArrayList<String> ejercicios) {this.ejercicios = ejercicios;}

    public ArrayList<String> getInsignias() {return insignias;}

    public void setInsignias(ArrayList<String> insignias) {this.insignias = insignias;}
}
