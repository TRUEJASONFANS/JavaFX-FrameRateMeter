package org.zhxie.main;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.zhxie.component.OldFXCanvas;
import org.zhxie.component.Recorder;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class FXCanvasComposite extends Composite {
  private final static long[] frameTimes = new long[100];
  private static int frameTimeIndex = 0;
  private static boolean arrayFilled = false;

  @FXML private Label label;
  @FXML private TableView<String> tableView;
  @FXML private TableColumn<String,String> nameCol;
  @FXML private ListView<String> listView;
  private OldFXCanvas fxCanvas;

  public FXCanvasComposite(Composite container, int style, Shell shell) {
    super(container, style);
    this.setLayout(new GridLayout(1, false));
    fxCanvas = new OldFXCanvas(this, SWT.NONE);
    fxCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    VBox root = getRoot();
    AnimationTimer frameRateMeter = createAnimationTimer(label);
    label.textProperty().addListener(e -> {
      Recorder.log(shell.getSize().x, shell.getSize().y, label.textProperty().getValue());
    });
    frameRateMeter.start();
    Scene scene = new Scene(root);
    fxCanvas.setScene(scene);

    Thread listViewTextUpdateThread = new ListViewTextUpdateThread(listView);
    listViewTextUpdateThread.start();

    nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    Thread tableViewUpdateThread = new TableViewUpdateThread(tableView);
    tableViewUpdateThread.start();
  }

  private VBox getRoot() {
    FXMLLoader fxmlLoader = new FXMLLoader(MyFXCanvasTest.class.getResource("FXCanvasComposite.fxml"));
    fxmlLoader.setController(this);
    VBox root = null;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      // LOGGER.error(e.getMessage());
    }
    return root;
  }

  private AnimationTimer createAnimationTimer(Label label) {

    AnimationTimer frameRateMeter = new AnimationTimer() {

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
          if (!fxCanvas.isDisposed()) {
            label.setText(String.format("%.3f", frameRate));
          }
        }
      }
    };
    return frameRateMeter;
  }


}
