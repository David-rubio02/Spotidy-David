package com.example.spotifyfx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;

public class LoginController {

    public TextField usuario;
    public TextField contrasenya;
    public Label usu;
    public Label con;
    boolean log = false;
    public static String usuarios;

    public void entrar(ActionEvent actionEvent) {
        int entrada = 0;

        if (usuario.getText().equals("")){
            usu.setText("Usuario:*");
        }else{
            usu.setText("Usuario:");
            entrada++;
        }

        if (contrasenya.getText().equals("")){
            con.setText("Contraseña:*");
        }else{
            con.setText("Contraseña:");
            entrada++;
        }

        if (entrada==2){
            realizarConsulta();

            if (!log) {
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
            }
        }
    }

    private void realizarConsulta() {
        try {
            Statement stmt = baseDEDatos().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username, password FROM usuario");

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                if (usuario.getText().equals(username) && contrasenya.getText().equals(password)){
                    System.out.println("Usuario y contraseña correcta");
                    log=false;
                    usuarios=username;
                    break;
                }else{
                    log = true;
                }
            }

            if (log){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("El usuario no existe");
                alert.showAndWait();
            }

            baseDEDatos().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void crear_cuenta(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("crea_cuenta-view.fxml"));

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

    public Connection baseDEDatos() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                "root",
                "dbrootpass");
        return con;
    }
}
