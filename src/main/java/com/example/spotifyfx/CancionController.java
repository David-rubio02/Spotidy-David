package com.example.spotifyfx;

import com.example.spotifyfx.modelos.Cancion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CancionController {
    @FXML
    private ImageView img;

    @FXML
    private Label nombreCancion;

    @FXML
    private Label nombreArtista;

    public void setCanciones(Cancion cancion){
        Image imagen = new Image(cancion.getCover());
        img.setImage(imagen);
        nombreCancion.setText(cancion.getNombre());
        nombreArtista.setText(cancion.getArtista());
    }
}
