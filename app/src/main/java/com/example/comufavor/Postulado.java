package com.example.comufavor;

public class Postulado {
    private String nombre;
    private String calificacion;

    public Postulado(String nombre, String calificacion) {
        this.nombre = nombre;
        this.calificacion = calificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCalificacion() {
        return calificacion;
    }
}
