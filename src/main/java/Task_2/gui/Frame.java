package Task_2.gui;

import Task_2.models.Acquaintance;
import Task_2.models.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Frame
    extends Application
{
    private static DataBase dataBase;

    @SuppressWarnings("unused")  // for javaFX
    public Frame() {}

    public Frame(DataBase dataBase) {
        Frame.dataBase = dataBase;
    }

    public void run() {
        launch();
    }

    @Override
    public void start(Stage stage)
    throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        loader.<Controller>getController().initialize(dataBase);
        Scene scene = new Scene(root);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
}
