package com.example.llm.ui.alumnado;

import java.util.ArrayList;

public class Estudiante {
    String nombre, email, curso;
    ArrayList<String> insignias;

    public Estudiante(String nombre, String email, String curso, ArrayList<String> insignias) {
        this.nombre = nombre;
        this.email = email;
        this.curso = curso;
        this.insignias = insignias;
    }

    public Estudiante() {}

    public ArrayList<String> getInsignias() {
        return insignias;
    }

    public void setInsignias(ArrayList<String> estudiantes) {
        this.insignias = estudiantes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCurso() {
        return curso;
    }

    public void setNombre(String nombre) {this.nombre = nombre; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
