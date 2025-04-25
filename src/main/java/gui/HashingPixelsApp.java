package gui;

import logic.Encode;
import logic.ReColor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.imageio.ImageIO;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;


public class HashingPixelsApp extends Application {

    private ImageView originalView = new ImageView();
    private ImageView transformedView = new ImageView();
    private File currentFile;
    private final int cube = 20;
    private final int colorLimit = 16;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("HashingPixels GUI");

        Button loadBtn = new Button("Load Image");
        Button encodeBtn = new Button("Encode");
        Button recolorBtn = new Button("ReColor");
        Button saveBtn = new Button("Save Output");

        loadBtn.setOnAction(e -> loadImage(primaryStage));
        encodeBtn.setOnAction(e -> encodeImage());
        recolorBtn.setOnAction(e -> recolorImage());
        saveBtn.setOnAction(e -> saveImage());

        HBox controls = new HBox(10, loadBtn, encodeBtn, recolorBtn, saveBtn);
        controls.setPadding(new Insets(10));

        HBox images = new HBox(10, originalView, transformedView);
        images.setPadding(new Insets(10));

        VBox layout = new VBox(controls, images);

        Scene scene = new Scene(layout, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Image img = new Image(new FileInputStream(file));
                originalView.setImage(img);
                currentFile = file;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void encodeImage() {
        if (currentFile != null) {
            try {
                Encode encoder = new Encode(currentFile.getAbsolutePath(), cube, colorLimit);
                encoder.makeEncoded(6, 15);
                String path = currentFile.getAbsolutePath().replace(".png", "Encoded.png");
                transformedView.setImage(new Image(new FileInputStream(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recolorImage() {
        if (currentFile != null) {
            try {
                ReColor recolorer = new ReColor(currentFile.getAbsolutePath(), cube, colorLimit);
                recolorer.makeRed();
                String path = currentFile.getAbsolutePath().replace(".png", "Red.png");
                transformedView.setImage(new Image(new FileInputStream(path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage() {
        if (transformedView.getImage() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image As");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG files", "*.png")
            );

            File saveFile = fileChooser.showSaveDialog(null);
            if (saveFile != null) {
                try {
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(transformedView.getImage(), null);
                    ImageIO.write(bufferedImage, "png", saveFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
