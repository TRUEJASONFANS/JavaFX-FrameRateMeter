/**
 * 
 */
package org.zhxie.main;

import org.zhxie.fx.ControlsPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author zhxie
 *
 */
public class FxApplicationTest extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ControlsPane pane = new ControlsPane();
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
