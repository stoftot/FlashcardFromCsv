module org.example.flashcardfromcsv {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.flashcardfromcsv to javafx.fxml;
    exports org.example.flashcardfromcsv;
}