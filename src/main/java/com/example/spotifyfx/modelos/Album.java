package com.example.spotifyfx.modelos;

public class Album {
    private int id, anyo;
    private String titulo,imagen,fecha_inicio_patrocinio,fecha_fin_patrocinio;
    private boolean patrocinado;

    public Album(int id, int anyo, String titulo, String imagen, String fecha_inicio_patrocinio, String fecha_fin_patrocinio, boolean patrocinado) {
        this.id = id;
        this.anyo = anyo;
        this.titulo = titulo;
        this.imagen = imagen;
        this.fecha_inicio_patrocinio = fecha_inicio_patrocinio;
        this.fecha_fin_patrocinio = fecha_fin_patrocinio;
        this.patrocinado = patrocinado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha_inicio_patrocinio() {
        return fecha_inicio_patrocinio;
    }

    public void setFecha_inicio_patrocinio(String fecha_inicio_patrocinio) {
        this.fecha_inicio_patrocinio = fecha_inicio_patrocinio;
    }

    public String getFecha_fin_patrocinio() {
        return fecha_fin_patrocinio;
    }

    public void setFecha_fin_patrocinio(String fecha_fin_patrocinio) {
        this.fecha_fin_patrocinio = fecha_fin_patrocinio;
    }

    public boolean isPatrocinado() {
        return patrocinado;
    }

    public void setPatrocinado(boolean patrocinado) {
        this.patrocinado = patrocinado;
    }
}
