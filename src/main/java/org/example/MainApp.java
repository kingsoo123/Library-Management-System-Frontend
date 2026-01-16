package org.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Button btn = new Button("Hello  from Kingsley");
        Button exist = new Button("Exist");
        exist.setOnAction(e-> System.out.println(exist.getText()));
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Hello from Kingsley Onyelo");
            }
        });
        //StackPane stackPane = new StackPane();
        //VBox root = new VBox();
        FXMLLoader  loader = new FXMLLoader(
                getClass().getResource("/Demo.fxml")
        );
        Scene scene = new Scene(loader.load(), 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("JavaFX Demo");
        stage.show();
    }

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
    public static void main() {
        launch();
    }
}
