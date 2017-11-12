package org.zhxie.main;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ListViewTextUpdateThread extends Thread {

  private TextArea textArea;
  private boolean stop = false;

  public ListViewTextUpdateThread(TextArea textArea) {
    this.textArea = textArea;
  }

  @Override
  public void run() {
    while (!stop) {
      try {
        sleep(50);
        Platform.runLater(() -> {
          textArea.appendText(getRandomString(3));
          textArea.setScrollTop(10);
        });
      } catch (InterruptedException e) {
      }
    }
  }

  public static String getRandomString(int length) { //length
    String base = "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789";
    return base.toString();
  }

  /**
   * @param stop the stop to set
   */
  public void setStop(boolean stop) {
    this.stop = stop;
  }

}
