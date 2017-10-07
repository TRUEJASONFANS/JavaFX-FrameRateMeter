package org.zhxie.main;


import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.zhxie.component.OldFXCanvas;
import org.zhxie.component.Recorder;
import org.zhxie.component.SWTTestUtil;

import com.google.common.base.Function;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MyFXCanvasTest {
  

  private final static long[] frameTimes = new long[100];
  private static int frameTimeIndex = 0 ;
  private static boolean arrayFilled = false ;
  
  public static void main(String[] args) {
    open(800, 600);
  }
  
  private static void open(final int width, final int height) {
    SWTTestUtil.openShell(String.format("FX Canvas (%dx%d)", width, height), 
        width+16, height+39, new Function<Shell, Control>() {

      @Override
      public Control apply(Shell shell) {
        OldFXCanvas fxCanvas = new OldFXCanvas(shell, SWT.NONE);
        AnchorPane root = getRoot();

        final Label label = new Label();
        AnimationTimer frameRateMeter = new AnimationTimer() {

            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                    label.setText(String.format("%.3f", frameRate));
                }
            }
        };
        label.textProperty().addListener(e -> {
        	Recorder.log(width, height, label.textProperty().getValue());
        });
        frameRateMeter.start();
        VBox vBox = new VBox();
        root.setPrefHeight(height);
        root.setPrefWidth(width);
        vBox.getChildren().addAll(root, label);
        Scene scene = new Scene(vBox);
        fxCanvas.setScene(scene);
        return fxCanvas;
      }
    });
    Recorder.close();
  }
  
  protected static AnchorPane getRoot() {
    FXMLLoader fxmlLoader = new FXMLLoader(MyFXCanvasTest.class.getResource("textControl.fxml"));
//    fxmlLoader.setController(this);
//    fxmlLoader.setResources(getResourceBundle());

    AnchorPane root = null;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
//      LOGGER.error(e.getMessage());
    }
    return root;
  }
}
