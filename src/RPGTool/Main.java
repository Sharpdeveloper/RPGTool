package RPGTool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
            String path = Paths.get(System.getProperty("user.home"), "RPGTool").toString();
            Path file = Paths.get(path, "error.log");
            try (BufferedWriter bw = Files.newBufferedWriter(file, Charset.forName("UTF-8")))
            {
                bw.write("Exception : " + throwable.getMessage());
                bw.newLine();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        });
        Parent root = FXMLLoader.load(getClass().getResource("RPGTool.fxml"));
        primaryStage.setTitle("RPGTool");
        primaryStage.setScene(new Scene(root, 550, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
