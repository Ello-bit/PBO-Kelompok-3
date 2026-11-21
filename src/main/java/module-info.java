module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires java.sql;


    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to javafx.base, javafx.fxml;
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.database;
}
