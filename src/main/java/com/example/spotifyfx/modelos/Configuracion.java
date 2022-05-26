package com.example.spotifyfx.modelos;

public class Configuracion {
    int idioma,calidad,descarga,usuario;

    public Configuracion(int idioma, int calidad, int descarga, int usuario) {
        this.idioma = idioma;
        this.calidad = calidad;
        this.descarga = descarga;
        this.usuario = usuario;
    }

    public int getIdioma() {
        return idioma;
    }

    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }

    public int getCalidad() {
        return calidad;
    }

    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }

    public int getDescarga() {
        return descarga;
    }

    public void setDescarga(int descarga) {
        this.descarga = descarga;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
