module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;

    exports com.example.demo.nav;
    opens com.example.demo.nav to javafx.fxml;
    exports com.example.demo.task1;
    opens com.example.demo.task1 to javafx.fxml;
    exports com.example.demo.task2;
    opens com.example.demo.task2 to javafx.fxml;
}