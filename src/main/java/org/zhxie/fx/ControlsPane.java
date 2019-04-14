package org.zhxie.fx;

import java.io.IOException;

import org.zhxie.fxcanvas.FXCanvasComposite;
import org.zhxie.fxcanvas.FrameAnimationTimer;
import org.zhxie.fxcanvas.ListViewTextUpdateThread;
import org.zhxie.fxcanvas.TableViewUpdateThread;
import org.zhxie.record.Recorder;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ControlsPane extends VBox {

	@FXML
	private Label label;
	@FXML
	private TableView<String> tableView;
	@FXML
	private TableColumn<String, String> nameCol;
	@FXML
	private TextArea textArea;

	public ControlsPane() {
		FXMLLoader fxmlLoader = new FXMLLoader(ControlsPane.class.getResource("ControlsPane.fxml"));
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(this);
		try {
			 fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		AnimationTimer frameRateMeter = createAnimationTimer(label);
		frameRateMeter.start();
		
		ListViewTextUpdateThread listViewTextUpdateThread = new ListViewTextUpdateThread(textArea);
		listViewTextUpdateThread.start();

		nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
		TableViewUpdateThread tableViewUpdateThread = new TableViewUpdateThread(tableView);
		tableViewUpdateThread.start();
	}

	private AnimationTimer createAnimationTimer(Label label) {
		FrameAnimationTimer frameRateMeter = new FrameAnimationTimer();
		label.textProperty().bind(frameRateMeter.frameValProperty());
		return frameRateMeter;
	}

}
