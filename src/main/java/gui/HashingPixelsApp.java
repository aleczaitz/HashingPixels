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
import javafx.scene.control.Label;
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
    private Label statusLabel;
    private javafx.scene.control.TextField colorLimitInput;
    private javafx.scene.control.ColorPicker recolorPicker;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        originalView.setFitWidth(400);
        originalView.setFitHeight(400);
        originalView.setPreserveRatio(true);

        transformedView.setFitWidth(400);
        transformedView.setFitHeight(400);
        transformedView.setPreserveRatio(true);

        primaryStage.setTitle("HashingPixels - Image Encoder");

        colorLimitInput = new javafx.scene.control.TextField();
        colorLimitInput.setPromptText("Max Colors (e.g., 16)");
        colorLimitInput.setMaxWidth(120);

        recolorPicker = new javafx.scene.control.ColorPicker();
        recolorPicker.setStyle("-fx-background-color: #3a3a3a;");


        Button loadBtn = new Button("Load Image");
        Button encodeBtn = new Button("Encode");
        Button recolorBtn = new Button("ReColor");
        Button saveBtn = new Button("Save Output");

        loadBtn.setOnAction(e -> {
            loadImage(primaryStage);
        });
        encodeBtn.setOnAction(e -> {
            encodeImage();
        });
        recolorBtn.setOnAction(e -> {
            recolorImage();
        });
        saveBtn.setOnAction(e -> {
            saveImage();
        });

        // New: Status label at the bottom
        statusLabel = new Label("Ready");

        // HBox for buttons
        HBox controls = new HBox(10, loadBtn, encodeBtn, recolorBtn, saveBtn, colorLimitInput, recolorPicker);
        controls.setAlignment(javafx.geometry.Pos.CENTER);
        controls.setPadding(new Insets(10));

        // New: wrap transformedView in VBox to add left padding
        VBox transformedContainer = new VBox(transformedView);
        transformedContainer.setPadding(new Insets(0, 0, 0, 20)); // top, right, bottom, left

        // HBox for images
        HBox images = new HBox(20, originalView, transformedContainer);
        images.setAlignment(javafx.geometry.Pos.CENTER);
        images.setPadding(new Insets(10));

        // Full layout
        VBox layout = new VBox(20, controls, images, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1e1e;");

        loadBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;");
        loadBtn.setOnMouseEntered(e -> loadBtn.setStyle("-fx-background-color: #505050; -fx-text-fill: white;"));
        loadBtn.setOnMouseExited(e -> loadBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;"));

        encodeBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;");
        encodeBtn.setOnMouseEntered(e -> encodeBtn.setStyle("-fx-background-color: #505050; -fx-text-fill: white;"));
        encodeBtn.setOnMouseExited(e -> encodeBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;"));

        recolorBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;");
        recolorBtn.setOnMouseEntered(e -> recolorBtn.setStyle("-fx-background-color: #505050; -fx-text-fill: white;"));
        recolorBtn.setOnMouseExited(e -> recolorBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;"));

        saveBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #505050; -fx-text-fill: white;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: white;"));

        statusLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
        layout.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 850, 600);
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
                statusLabel.setText("Image loaded!");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                statusLabel.setText("Failed to load image.");
            }
        }
    }


    private void encodeImage() {
        if (currentFile != null) {
            try {
                int maxColors = colorLimit;

                if (!colorLimitInput.getText().isEmpty()) {
                    maxColors = Integer.parseInt(colorLimitInput.getText());
                }

                // Create encoder with updated color limit
                Encode encoder = new Encode(currentFile.getAbsolutePath(), cube, maxColors);

                // Now call the encoder to get the image (no parameters needed)
                BufferedImage outputImage = encoder.getEncodedImage();

                transformedView.setImage(SwingFXUtils.toFXImage(outputImage, null));
                statusLabel.setText("Image encoded!");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Encoding failed.");
            }
        }
    }




    private void recolorImage() {
        if (currentFile != null) {
            try {
                javafx.scene.paint.Color selectedColor = recolorPicker.getValue();

                ReColor recolorer = new ReColor(currentFile.getAbsolutePath(), cube, colorLimit);

                BufferedImage outputImage = recolorer.makeColorImage(
                        selectedColor.getRed(),
                        selectedColor.getGreen(),
                        selectedColor.getBlue()
                );

                transformedView.setImage(SwingFXUtils.toFXImage(outputImage, null));
                statusLabel.setText("Image recolored!");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Recoloring failed.");
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
                    statusLabel.setText("Image saved!");
                } catch (IOException e) {
                    e.printStackTrace();
                    statusLabel.setText("Saving failed.");
                }
            }
        }
    }



}
