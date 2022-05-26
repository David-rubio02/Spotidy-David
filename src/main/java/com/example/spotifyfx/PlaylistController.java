package com.example.spotifyfx;

import com.example.spotifyfx.modelos.Cancion;
import com.example.spotifyfx.modelos.PlayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlaylistController {
    @FXML
    private ImageView img;

    @FXML
    private Label nombrePlaylist;

    @FXML
    private Label numeroCanciones;

    public void setPLaylist(PlayList playlist){
        Image imagen = new Image("com/example/spotifyfx/img/disco2.png");
        img.setImage(imagen);
        nombrePlaylist.setText(playlist.getTitulo());
        numeroCanciones.setText(String.valueOf(playlist.getNumero_canciones()));
    }
}
