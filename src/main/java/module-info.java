module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;

    opens com.example to javafx.fxml;
    opens com.example.laporan to javafx.fxml;

    exports com.example;
    exports com.example.laporan;
}