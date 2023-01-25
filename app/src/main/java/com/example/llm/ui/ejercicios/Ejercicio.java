package com.example.llm.ui.ejercicios;

public class Ejercicio {

    String idEjercicio, tipo, enunciado;

    public Ejercicio(String idEjercicio, String enunciado) {
        this.idEjercicio = idEjercicio;
        this.enunciado = enunciado;
    }

    public Ejercicio(){

    }

    public String getIdEjercicio() { return idEjercicio; }

    public void setId(String idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

}
