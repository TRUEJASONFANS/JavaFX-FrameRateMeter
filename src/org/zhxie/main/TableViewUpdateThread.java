package org.zhxie.main;

import java.util.Random;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class TableViewUpdateThread extends Thread {

  private TableView<String> tableView;
  private ObservableList<String> inputs = FXCollections.observableArrayList();

  public TableViewUpdateThread(TableView<String> tableView) {
    this.tableView = tableView;
    tableView.setItems(inputs);
  }

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(2000);
        Platform.runLater(()->{
          inputs.add(getRandomString(3));
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static String getRandomString(int length) { //length表示生成字符串的长度
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
