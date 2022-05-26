module com.example.spotifyfx {

    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spotifyfx to javafx.fxml;
    exports com.example.spotifyfx;
    exports com.example.spotifyfx.modelos;
    opens com.example.spotifyfx.img;
}