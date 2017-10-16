package org.zhxie.component;


import java.io.IOException;

import org.zhxie.main.MyFXCanvasTest;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleFrameRateMeter extends Application {

  private final long[] frameTimes = new long[100];
  private int frameTimeIndex = 0;
  private boolean arrayFilled = false;
  private final int width = 300;
  private final int height = 200;
  private AnimationTimer frameRateMeter;

  @Override
  public void start(Stage primaryStage) {

    Label label = new Label();
    frameRateMeter = new AnimationTimer() {

      @Override
      public void handle(long now) {
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if (frameTimeIndex == 0) {
          arrayFilled = true;
        }
        if (arrayFilled) {
          long elapsedNanos = now - oldFrameTime;
          long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
          double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
          label.setText(String.format("%.3f", frameRate));
        }
      }
    };
    label.textProperty().addListener(e -> {
      Recorder.log(this.width, this.height, label.textProperty().getValue());
    });
    frameRateMeter.start();
    AnchorPane root = getRoot();
    VBox vBox = new VBox();
    vBox.getChildren().addAll(root, label);
    root.setPrefHeight(height);
    root.setPrefWidth(width);
    VBox.setVgrow(root, Priority.ALWAYS);
    primaryStage.setScene(new Scene(vBox));
    primaryStage.show();
  }

  protected static AnchorPane getRoot() {
    FXMLLoader fxmlLoader = new FXMLLoader(MyFXCanvasTest.class.getResource("textControl.fxml"));
    AnchorPane root = null;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      // LOGGER.error(e.getMessage());
    }
    return root;
  }

  @Override
  public void stop() throws Exception {
    frameRateMeter.stop();
    Recorder.close();
    super.stop();
  }

  public static void main(String[] args) {
    launch(args);
  }
}