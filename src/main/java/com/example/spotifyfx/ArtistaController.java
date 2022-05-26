package com.example.spotifyfx;

import com.example.spotifyfx.modelos.Artista;
import com.example.spotifyfx.modelos.Cancion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArtistaController {

    @FXML
    private ImageView img;


    @FXML
    private Label nombre;


    public void setArtistas(Artista artista){
        Image imagen = new Image(artista.getImagen());
        img.setImage(imagen);
        nombre.setText(artista.getNombre());
    }
}
