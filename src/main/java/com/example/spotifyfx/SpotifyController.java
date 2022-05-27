package com.example.spotifyfx;

import com.example.spotifyfx.modelos.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
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

public class SpotifyController implements Initializable {

    public Button hacer_premium;
    public ImageView img_reproductor;
    public Label nombre_reproductor;
    public Label descripcion_reproductor;

    @FXML
    public HBox AzarContainer;
    public HBox PlaylistContainer;
    public ScrollPane scroll;
    public HBox ArtistasContainer;
    public VBox PlaylistCancionesContainer;
    public ScrollPane scrollPlaylist;

    @FXML
    private HBox podcastContainer;

    @FXML
    private HBox favoritasContainer;

    List<Podcast> podcasts;
    List<Cancion> favoritas;
    List<Cancion> azar;
    List<PlayList> playlist;
    List<Artista> artistas;
    List<String> playlist_canciones;

    public ImageView corazon;
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
    Artista artista;

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
        scrollPlaylist.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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

        if (!es_premium){
            bt_calidad.setVisible(false);
            bt_tipo_descarga.setVisible(false);
        }else{
            hacer_premium.setVisible(false);
        }

        //canciones
        podcasts = new ArrayList<>(getPodcast());
        favoritas = new ArrayList<>(getFavoritas());
        azar = new ArrayList<>(getQuizas());
        playlist = new ArrayList<>(getPlaylist());
        artistas = new ArrayList<>(getArtistas());
        fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));

        try {

            for (Cancion song : azar) {
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
                    }
                });
                focused(vBox);
            }

            for (Cancion song : favoritas) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("cancion-view.fxml"));

                VBox vBox = fxmlLoader.load();
                CancionController cancionController = fxmlLoader.getController();
                cancionController.setCanciones(song);

                favoritasContainer.getChildren().add(vBox);

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
                                int usuario_id= rs.getInt("usuario_id");
                                if (usuario_id == c1.getUsuario()){
                                    corazon.setImage(corazonrojo);
                                    fav_on=true;
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

            for (Podcast c: podcasts){
                FXMLLoader fxmlloader = new FXMLLoader();
                fxmlloader.setLocation(getClass().getResource("podcast-view.fxml"));

                VBox vbox = fxmlloader.load();
                PodcastController podcastController = fxmlloader.getController();
                podcastController.setDatos(c);

                podcastContainer.getChildren().add(vbox);

                vbox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        corazon.setVisible(false);

                        podcast.setDescripcion(c.getDescripcion());
                        podcast.setTitulo(c.getTitulo());
                        podcast.setImagen(c.getImagen());

                        img_reproductor.setImage(new Image(podcast.getImagen()));
                        nombre_reproductor.setText(podcast.getTitulo());
                        descripcion_reproductor.setText(podcast.getDescripcion());
                    }
                });
                focused(vbox);
            }

            for (PlayList c: playlist){
                FXMLLoader fxmlloader = new FXMLLoader();
                fxmlloader.setLocation(getClass().getResource("playlist-view.fxml"));

                VBox vbox = fxmlloader.load();
                PlaylistController playlistcontroller = fxmlloader.getController();
                playlistcontroller.setPLaylist(c);

                PlaylistContainer.getChildren().add(vbox);

                vbox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        corazon.setVisible(false);

                        playlist1.setNumero_canciones(c.getNumero_canciones());
                        playlist1.setTitulo(c.getTitulo());

                        img_reproductor.setImage(new Image("com/example/spotifyfx/img/disco2.png"));
                        nombre_reproductor.setText(playlist1.getTitulo());
                        descripcion_reproductor.setText(String.valueOf("Nº Canciones: " + playlist1.getNumero_canciones()));

                        playlist_canciones  = new ArrayList<>(getCancionesPlaylist());

                        PlaylistCancionesContainer.getChildren().removeAll(PlaylistCancionesContainer.getChildren());
                        for (String can : playlist_canciones) {
                            Label label = new Label();
                            label.setStyle("-fx-text-fill: white;");
                            label.setText(can);
                            PlaylistCancionesContainer.getChildren().add(label);
                        }
                    }
                });
                focused(vbox);
            }

            for (Artista art : artistas) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("artista-view.fxml"));

                VBox vBox = fxmlLoader.load();
                ArtistaController artistaController = fxmlLoader.getController();
                artistaController.setArtistas(art);

                ArtistasContainer.getChildren().add(vBox);

                vBox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        corazon.setVisible(false);

                        artista.setImagen(art.getImagen());
                        artista.setNombre(art.getNombre());

                        img_reproductor.setImage(new Image (artista.getImagen()));
                        nombre_reproductor.setText(artista.getNombre());
                        descripcion_reproductor.setText("");
                    }
                });
                focused(vBox);
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    private List<Podcast> getPodcast(){
        List<Podcast> lista = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("select podcast.titulo ,podcast.imagen,podcast.descripcion " +
                    "from podcast_usuario pu " +
                    "inner join podcast on podcast.id=pu.podcast_id " +
                    "inner join usuario on usuario.id=pu.usuario_id " +
                    "where pu.usuario_id = " + "'" + c1.getUsuario() + "'");

            while (rs.next()){
                podcast = new Podcast();
                podcast.setTitulo(rs.getString("titulo"));
                podcast.setDescripcion(rs.getString("descripcion"));
                podcast.setImagen(rs.getString("imagen"));
                lista.add(podcast);
            }

            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Cancion> getFavoritas(){
        List<Cancion> ls = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("select cancion.titulo, cancion.ruta, artista.nombre " +
                    "from guarda_cancion gc " +
                    "inner join cancion on cancion.id=gc.cancion_id " +
                    "inner join usuario on usuario.id=gc.usuario_id " +
                    "inner join album on album.id=cancion.album_id " +
                    "inner join artista on artista.id=album.artista_id " +
                    "where usuario.id =" + '"' + c1.getUsuario() + '"');

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

    public List<Cancion> getQuizas(){
        List<Cancion> ls = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("select cancion.titulo, cancion.ruta, artista.nombre " +
                    "from guarda_cancion gc " +
                    "inner join cancion on cancion.id=gc.cancion_id " +
                    "inner join usuario on usuario.id=gc.usuario_id " +
                    "inner join album on album.id=cancion.album_id " +
                    "inner join artista on artista.id=album.artista_id " +
                    "where cancion.id  not in (select cancion_id from guarda_cancion where usuario_id = " + '"' + c1.getUsuario() + '"' +
                    ") group by titulo limit 10;");

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

    public List<PlayList> getPlaylist(){
        List<PlayList> ls = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.numero_canciones,p.titulo\n" +
                    "from sigue_playlist sp\n" +
                    "inner join playlist p on sp.playlist_id = p.id\n" +
                    "where sp.usuario_id = " + '"' + c1.getUsuario() + '"' + " group by p.titulo;");

            while (rs.next()){
                playlist1 = new PlayList();
                playlist1.setTitulo(rs.getString("titulo"));
                playlist1.setNumero_canciones(rs.getInt("numero_canciones"));
                ls.add(playlist1);
            }

            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public List<Artista> getArtistas(){
        List<Artista> ls = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT a.nombre, a.imagen\n" +
                    "FROM artista a\n" +
                    "inner join sigue_artista sa on sa.artista_id = a.id\n" +
                    "where sa.usuario_id =" + '"' + c1.getUsuario() + '"');

            while (rs.next()){
                artista = new Artista();
                artista.setNombre(rs.getString("nombre"));
                artista.setImagen(rs.getString("imagen"));
                ls.add(artista);
            }

            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public List<String> getCancionesPlaylist(){
        List<String> ls = new ArrayList<>();

        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("select c.titulo " +
                            "from cancion c " +
                            "inner join album al on al.id = c.album_id " +
                            "inner join artista a on a.id = al.artista_id " +
                            "inner join anyade_cancion_playlist acp on acp.cancion_id = c.id " +
                            "inner join playlist p on p.id = acp.playlist_id " +
                            "where p.titulo = '" + playlist1.getTitulo() + "';");

            while (rs.next()){
                String titulo = (rs.getString("titulo"));
                ls.add(titulo);
            }
            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public void explorar(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(SpotifyApplication.class.getResource("explore-view.fxml"));

        try {
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Explorar");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void premium(ActionEvent actionEvent) {
        try {
            Statement s = null;
            s = baseDEDatos().createStatement();
            int numReg = s.executeUpdate( "DELETE FROM free WHERE usuario_id =" + '"' + c1.getUsuario() + '"' +";" );

            // Informamos del número de registros borrados
            System.out.println ("\nSe borró " + numReg + " registros\n") ;

            s.executeUpdate("INSERT INTO premium " +
                    " (fecha_renovación,usuario_id) " +
                    " VALUES (DATE_ADD(NOW(),INTERVAL '1' MONTH) ," + '"' + c1.getUsuario() + '"' +")");

            baseDEDatos().close();
            salida(actionEvent);

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

    public void para_ti(MouseEvent mouseEvent) {scroll.setVvalue(0);}

    public void fav(MouseEvent mouseEvent) {scroll.setVvalue(0.29);}

    public void podcast(MouseEvent mouseEvent) {scroll.setVvalue(0.58);}

    public void playlist(MouseEvent mouseEvent) {scroll.setVvalue(0.87);}

    public void artistas(MouseEvent mouseEvent) {scroll.setVvalue(1);}
}