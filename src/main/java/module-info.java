module com.example.revolutionary_hangman {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.revolutionary_hangman to javafx.fxml;
    exports com.example.revolutionary_hangman;
}