package com.example.spotifyfx.modelos;

public class PlayList {
    private int id,numero_canciones;
    private String titulo,fecha_creacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero_canciones() {
        return numero_canciones;
    }

    public void setNumero_canciones(int numero_canciones) {
        this.numero_canciones = numero_canciones;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
