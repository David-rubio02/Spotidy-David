package com.example.spotifyfx;

import com.example.spotifyfx.modelos.Cancion;
import com.example.spotifyfx.modelos.Configuracion;
import com.example.spotifyfx.modelos.PlayList;
import com.example.spotifyfx.modelos.Podcast;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExploreController implements Initializable {

    public ImageView img_reproductor;
    public Label nombre_reproductor;
    public Label descripcion_reproductor;
    public String busqueda;
    public HBox AzarContainer;
    List<Cancion> encontradas;

    @FXML
    public ScrollPane scroll;

    public ImageView corazon;
    public TextField buscador;
    private boolean fav_on = false;
    FXMLLoader fxmlLoader;
    public Label nom_usu;
    public Label calidad_lbl;
    public Label tipo_descarga_lbl;
    public MenuButton bt_tipo_descarga;
    public MenuButton bt_calidad;
    Configuracion c1;
    Cancion cancion;
    Podcast podcast;
    PlayList playlist1;

    Image espana = new Image("com/example/spotifyfx/img/espana.png");
    Image ingles = new Image("com/example/spotifyfx/img/ingles.png");
    Image francia = new Image("com/example/spotifyfx/img/francia.png");
    Image italia = new Image("com/example/spotifyfx/img/italia.png");
    Image alemania = new Image("com/example/spotifyfx/img/alemania.png");

    Image corazonrojo = new Image("com/example/spotifyfx/img/ic_love2.png");
    Image corazonblanco = new Image("com/example/spotifyfx/img/ic_love.png");

    boolean es_premium;

    @FXML
    ImageView bandera;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nom_usu.setText(LoginController.usuarios);

        //configuracion
        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT idioma_id,calidad_id,tipo_descarga_id,usuario_id FROM configuracion WHERE usuario_id=(SELECT id FROM usuario WHERE username=" + '"' + nom_usu.getText() + '"' + ")");

            while (rs.next()) {
                c1 = new Configuracion(rs.getInt("idioma_id"),rs.getInt("calidad_id"),rs.getInt("tipo_descarga_id"),rs.getInt("usuario_id"));
            }

            //Idioma por defecto al iniciar sesion
            if (c1.getIdioma()==1){
                bandera.setImage(espana);
            }else if(c1.getIdioma()==2){
                bandera.setImage(ingles);
            }else if(c1.getIdioma()==3){
                bandera.setImage(francia);
            }else if(c1.getIdioma()==4){
                bandera.setImage(italia);
            }else if(c1.getIdioma()==5){
                bandera.setImage(alemania);
            }

            //Calidad por defecto al iniciar sesion
            if (c1.getCalidad()==1){
                calidad_lbl.setText("Automatica");
            }else if(c1.getCalidad()==2){
                calidad_lbl.setText("Baja");
            }else if(c1.getCalidad()==3){
                calidad_lbl.setText("Media");
            }else if(c1.getCalidad()==4){
                calidad_lbl.setText("Alta");
            }else if(c1.getCalidad()==5){
                calidad_lbl.setText("Muy Alta");
            }

            //Tipo descarga por defecto al iniciar sesion
            if (c1.getDescarga()==1){
                tipo_descarga_lbl.setText("Automatica");
            }else if(c1.getDescarga()==2){
                tipo_descarga_lbl.setText("Baja");
            }else if(c1.getDescarga()==3){
                tipo_descarga_lbl.setText("Media");
            }else if(c1.getDescarga()==4){
                tipo_descarga_lbl.setText("Alta");
            }else if(c1.getDescarga()==5){
                tipo_descarga_lbl.setText("Muy Alta");
            }

            rs = stmt.executeQuery("SELECT usuario_id FROM premium");
            while (rs.next()) {
                int usuario_id= rs.getInt("usuario_id");
                if (usuario_id==c1.getUsuario()){
                    es_premium=true;
                    break;
                }
            }

            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void principal(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(SpotifyApplication.class.getResource("spotify-view.fxml"));

        try {
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            stage.setTitle("Explorar");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscar(ActionEvent actionEvent) {
        busqueda=buscador.getText();

        encontradas = new ArrayList<>(getCanciones());
        fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));

        try {
            AzarContainer.getChildren().removeAll(AzarContainer.getChildren());

            for (Cancion song : encontradas) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("cancion-view.fxml"));

                VBox vBox = fxmlLoader.load();
                CancionController cancionController = fxmlLoader.getController();
                cancionController.setCanciones(song);

                AzarContainer.getChildren().add(vBox);

                vBox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        corazon.setVisible(true);
                        corazon.setImage(corazonblanco);

                        cancion.setArtista(song.getArtista());
                        cancion.setNombre(song.getNombre());
                        cancion.setCover(song.getCover());

                        img_reproductor.setImage(new Image(cancion.getCover()));
                        nombre_reproductor.setText(cancion.getNombre());
                        descripcion_reproductor.setText(cancion.getArtista());

                        try {
                            Statement stmt = baseDEDatos().createStatement();
                            ResultSet rs = stmt.executeQuery("select usuario_id \n" +
                                    "from guarda_cancion gc\n" +
                                    "inner join cancion c on c.id = gc.cancion_id\n" +
                                    "where cancion_id = (select id from cancion where titulo =" + '"' + cancion.getNombre() + '"' + ");");

                            while (rs.next()) {
                                int usuario_id = rs.getInt("usuario_id");
                                if (usuario_id == c1.getUsuario()) {
                                    corazon.setImage(corazonrojo);
                                    fav_on = true;
                                    break;
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                focused(vBox);
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public List<Cancion> getCanciones(){
        List<Cancion> ls = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cancion.titulo, cancion.ruta, artista.nombre \n" +
                    "FROM cancion \n" +
                    "inner join album on album.id=cancion.album_id\n" +
                    "inner join artista on artista.id=album.artista_id\n" +
                    "where cancion.titulo like '%" + busqueda + "%';");

            while (rs.next()){
                cancion = new Cancion();
                cancion.setNombre(rs.getString("titulo"));
                cancion.setArtista(rs.getString("nombre"));
                cancion.setCover(rs.getString("ruta"));
                ls.add(cancion);
            }

            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public void love(ActionEvent actionEvent) {

        try {
            Statement s = baseDEDatos().createStatement();

            if (!fav_on) {
                corazon.setImage(corazonrojo);
                fav_on = true;
                s.executeUpdate("insert into guarda_cancion (usuario_id, cancion_id) values (" + '"' + c1.getUsuario() + '"' + ",(select id from cancion where titulo =" + '"' + cancion.getNombre() + '"' + "));");
            } else {
                corazon.setImage(corazonblanco);
                fav_on = false;
                s = baseDEDatos().createStatement();
                int numReg = s.executeUpdate( "delete from guarda_cancion where usuario_id = " + '"' + c1.getUsuario() + '"' + " and cancion_id = (select id from cancion where titulo =" + '"' + cancion.getNombre() + '"' + ");" );
                System.out.println ("\nSe borró " + numReg + " registros\n") ;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("spotify-view.fxml"));

            try {
                Parent root = fxmlLoader.load();

                Scene scene = new Scene(root);
                Stage stage;
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setTitle("Spotify");
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salida(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));

        try {
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Spotify");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //idiomas
    public void id_aleman(ActionEvent actionEvent) {
        bandera.setImage(alemania);
        c1.setIdioma(5);
        insertar_pais(alemania);
    }

    public void id_espanol(ActionEvent actionEvent) {
        bandera.setImage(espana);
        c1.setIdioma(1);
        insertar_pais(espana);
    }

    public void id_ingles(ActionEvent actionEvent) {
        bandera.setImage(ingles);
        c1.setIdioma(2);
        insertar_pais(ingles);
    }

    public void id_italiano(ActionEvent actionEvent) {
        bandera.setImage(italia);
        c1.setIdioma(4);
        insertar_pais(italia);
    }

    public void id_frances(ActionEvent actionEvent) {
        bandera.setImage(francia);
        c1.setIdioma(3);
        insertar_pais(francia);
    }

    public void insertar_pais(Image imagen){
        bandera.setImage(imagen);

        try {
            Statement s = baseDEDatos().createStatement();
            s.executeUpdate("UPDATE configuracion SET idioma_id=" + '"' + c1.getIdioma() + '"' +  "WHERE usuario_id=" + '"' + c1.getUsuario() + '"');
            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //calidad
    public void id_auto(ActionEvent actionEvent) {
        calidad_lbl.setText("Automatica");
        c1.setCalidad(1);
        insertar_calidad();
    }

    public void id_baja(ActionEvent actionEvent) {
        calidad_lbl.setText("Baja");
        c1.setCalidad(2);
        insertar_calidad();
    }

    public void id_normal(ActionEvent actionEvent) {
        calidad_lbl.setText("Media");
        c1.setCalidad(3);
        insertar_calidad();
    }

    public void id_alta(ActionEvent actionEvent) {
        calidad_lbl.setText("Alta");
        c1.setCalidad(4);
        insertar_calidad();
    }

    public void id_muyAlta(ActionEvent actionEvent) {
        calidad_lbl.setText("Muy Alta");
        c1.setCalidad(5);
        insertar_calidad();
    }

    public void insertar_calidad(){
        try {
            Statement s = baseDEDatos().createStatement();
            s.executeUpdate("UPDATE configuracion SET calidad_id=" + '"' + c1.getCalidad() + '"' +  "WHERE usuario_id=" + '"' + c1.getUsuario() + '"');
            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //tipo descarga
    public void id_auto_descarga(ActionEvent actionEvent) {
        tipo_descarga_lbl.setText("Automatica");
        c1.setDescarga(1);
        insertar_descarga();
    }

    public void id_baja_descarga(ActionEvent actionEvent) {
        tipo_descarga_lbl.setText("Baja");
        c1.setDescarga(2);
        insertar_descarga();
    }

    public void id_normal_descarga(ActionEvent actionEvent) {
        tipo_descarga_lbl.setText("Media");
        c1.setDescarga(3);
        insertar_descarga();
    }

    public void id_alta_descarga(ActionEvent actionEvent) {
        tipo_descarga_lbl.setText("Alta");
        c1.setDescarga(4);
        insertar_descarga();
    }

    public void id_muyAlta_descarga(ActionEvent actionEvent) {
        tipo_descarga_lbl.setText("Muy Alta");
        c1.setDescarga(5);
        insertar_descarga();
    }

    public void insertar_descarga(){
        try {
            Statement s = baseDEDatos().createStatement();
            s.executeUpdate("UPDATE configuracion SET tipo_descarga_id=" + '"' + c1.getDescarga() + '"' +  "WHERE usuario_id=" + '"' + c1.getUsuario() + '"');
            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection baseDEDatos() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                "root",
                "dbrootpass");
        return con;
    }

    public void focused(VBox vbox){
        vbox.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                vbox.setStyle("-fx-background-color: #555555;");
            }
        });

        vbox.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                vbox.setStyle("-fx-background-color: transparent;");
            }
        });
    }


}