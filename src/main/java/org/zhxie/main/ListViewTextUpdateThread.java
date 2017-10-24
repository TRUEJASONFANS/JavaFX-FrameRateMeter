package org.zhxie.main;

import java.util.Random;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ListViewTextUpdateThread extends Thread {

  private ListView<String> listView;
  private ObservableList<String> inputs = FXCollections.observableArrayList();

  public ListViewTextUpdateThread(ListView<String> listView) {
    this.listView = listView;
    this.listView.setItems(inputs);
  }

  @Override
  public void run() {
    while (!isInterrupted()) {
      try {
        sleep(500);
        Platform.runLater(() -> {
          inputs.add(getRandomString(3));
        });
      } catch (InterruptedException e) {
      }
    }
  }

  public static String getRandomString(int length) { //length��ʾ�����ַ����ĳ���
    String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }
}
