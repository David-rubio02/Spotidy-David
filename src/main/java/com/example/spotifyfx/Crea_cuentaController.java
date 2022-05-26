package com.example.spotifyfx;

import com.example.spotifyfx.modelos.Usuario;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crea_cuentaController implements Initializable {

    @FXML
    public Label usuario_label,contrasenya_label,email_label,genero_label,fnac_label,pais_label,codpostal_label;
    public TextField usuario,email,genero,pais,codpostal;
    public PasswordField contrasenya;
    public DatePicker fnac;
    Usuario u1 = new Usuario();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fijarTamañoMáximo(usuario,45);
        fijarTamañoMáximo(contrasenya,50);
        fijarTamañoMáximo(email,150);
        fijarTamañoMáximo(genero,1);
        fijarTamañoMáximo(pais,45);
        fijarTamañoMáximo(codpostal,20);
    }

    public void crear(ActionEvent actionEvent) {

        int entrada = 0;

        if (usuario.getText().equals("")){
            usuario_label.setText("Usuario:*");
        }else{
            usuario_label.setText("Usuario:");
            entrada++;
        }

        if (contrasenya.getText().equals("")){
            contrasenya_label.setText("Contraseña:*");
        }else{
            contrasenya_label.setText("Contraseña:");
            entrada++;
        }

        if (email.getText().equals("") || !(email.getText().contains("@"))){
            email_label.setText("Email:*");
        }else{
            email_label.setText("Email:");
            entrada++;
        }

        if (genero.getText().equals("H") || genero.getText().equals("M")){
            genero_label.setText("Genero:");
            entrada++;
        }else{
            genero_label.setText("Genero:*");

        }

        if (fnac.getValue()==null){
            fnac_label.setText("Fecha Nacimiento:*");
        }else{
            fnac_label.setText("Fecha Nacimiento:");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            fnac.setConverter(new LocalDateStringConverter(formatter, null));
            entrada++;
        }

        if (pais.getText().equals("")){
            pais_label.setText("Pais:*");
        }else{
            pais_label.setText("Pais:");
            entrada++;
        }

        if (codpostal.getText().equals("")){
            codpostal_label.setText("Codigo Postal:*");
        }else{
            codpostal_label.setText("Codigo Postal:");
            entrada++;
        }

        if (entrada==7){
            try {
                u1.setCodigo_postal(Integer.parseInt(codpostal.getText()));
                u1.setPais(pais.getText());
                u1.setUsername(usuario.getText());
                u1.setPassword(contrasenya.getText());
                u1.setEmail(email.getText());
                u1.setGenero(genero.getText());
                u1.setFecha_nacimiento(fnac.getValue());

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:33006/spotify",
                        "root",
                        "dbrootpass"
                );

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT username,email FROM usuario");

                boolean entry=false;
                while (rs.next()) {
                    String nom = rs.getString("username");
                    String em = rs.getString("email");
                    if (u1.getUsername().equals(nom) || u1.getEmail().equals(em)){
                        entry=false;
                        break;
                    }else{
                        entry=true;
                    }
                }

                if (entry){
                    Statement s = null;
                    // Ejecuta la consulta
                    s = con.createStatement();
                    s.executeUpdate("INSERT INTO usuario " +
                            "(username,`password`,email,genero,fecha_nacimiento,pais,codigo_postal) " +
                            "VALUES (" + '"' + u1.getUsername() + '"' + "," + '"' +u1.getPassword()+ '"' + "," + '"' +u1.getEmail()+ '"' + "," + '"' +u1.getGenero()+ '"' + "," + '"' +u1.getFecha_nacimiento()+ '"' + "," + '"' +u1.getPais()+ '"' + "," + '"' +u1.getCodigo_postal()+ '"' + ");");

                    s.executeUpdate("INSERT INTO free (fecha_revision,usuario_id) values (DATE_ADD(NOW(),INTERVAL '1' MONTH),(select id from usuario where username =" + '"' + u1.getUsername() + '"' + "));");

                    s.executeUpdate("INSERT INTO configuracion (idioma_id,calidad_id,tipo_descarga_id,usuario_id) values (1,1,1,(select id from usuario where username =" + '"' + u1.getUsername() + '"' + "));");

                    s.executeUpdate("INSERT INTO playlist (titulo,numero_canciones,fecha_creacion,usuario_id) values (" + '"' + "favoritas_"+ u1.getUsername() + '"' + ",0,curdate(),(select id from usuario where username =" + '"' + u1.getUsername() + '"' + "));");

                    s.executeUpdate("INSERT INTO favoritas (playlist_id) values ((select id from playlist where titulo = " + '"' + "favoritas_"+ u1.getUsername() + '"' + "));");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Usuario");
                    alert.setContentText("El usuario se ha añadido correctamente");
                    alert.showAndWait();

                    volver(actionEvent);
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("El usuario o el email ya esta en uso");
                    alert.showAndWait();
                }

                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void volver(ActionEvent actionEvent) {
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

    public static void fijarTamañoMáximo(final TextField campoTexto, final int tamañoMáximo) {
        campoTexto.lengthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number valorAnterior, Number valorActual) {

                if (valorActual.intValue() > valorAnterior.intValue()) {
                    Pattern permitido = Pattern.compile("[A-Za-zÑÇÁÉÍÓÚÀÈÌÒÙÏÜÂÊÎÔÛñçáéíóúàèìòùïüâêîôû@0123456789.]");
                    Matcher mpermitido = permitido.matcher(campoTexto.getText().substring(valorAnterior.intValue()));

                    if (!mpermitido.find()) {
                        // caracter no permitido, borrarlo
                        campoTexto.setText(campoTexto.getText().substring(0, valorAnterior.intValue()));
                        return;
                    }

                    // Revisa que la longitud del texto no sea mayor a la variable definida.
                    if (campoTexto.getText().length() >= tamañoMáximo) {
                        campoTexto.setText(campoTexto.getText().substring(0, tamañoMáximo));
                    }
                }
            }
        });
    }

}
