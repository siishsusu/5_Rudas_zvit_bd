package com.example.demo.nav;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class NavController {
    @FXML
    private Button task1Button, task2Button;

    @FXML
    private ScrollPane mainPane;

    @FXML
    public void initialize() {
        task1Button.setOnAction(event -> loadView("/com/example/demo/task1/task1-view.fxml"));
        task2Button.setOnAction(event -> loadView("/com/example/demo/task2/task2-view.fxml"));
    }

    private void loadView(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane view = loader.load();

            mainPane.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
