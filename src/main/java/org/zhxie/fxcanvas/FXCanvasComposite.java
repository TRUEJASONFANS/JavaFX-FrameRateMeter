package org.zhxie.fxcanvas;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.zhxie.component.Recorder;
import org.zhxie.main.MyFXCanvasTest;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swt.OldFXCanvas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class FXCanvasComposite extends Composite {

  @FXML private Label label;
  @FXML private TableView<String> tableView;
  @FXML private TableColumn<String,String> nameCol;
  @FXML private TextArea textArea;
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

    ListViewTextUpdateThread listViewTextUpdateThread = new ListViewTextUpdateThread(textArea);
    listViewTextUpdateThread.start();

    nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    TableViewUpdateThread tableViewUpdateThread = new TableViewUpdateThread(tableView);
    tableViewUpdateThread.start();

    fxCanvas.addDisposeListener(e -> {
      tableViewUpdateThread.setStop(true);
      listViewTextUpdateThread.setStop(true);
      Recorder.setStop(true);
    });
  }

  private VBox getRoot() {
    FXMLLoader fxmlLoader = new FXMLLoader(MyFXCanvasTest.class.getResource("FXCanvasComposite.fxml"));
    fxmlLoader.setController(this);
    VBox root = null;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }

  private AnimationTimer createAnimationTimer(Label label) {

    FrameAnimationTimer frameRateMeter = new FrameAnimationTimer();
    label.textProperty().bind(frameRateMeter.frameValProperty());
    return frameRateMeter;
  }


}
