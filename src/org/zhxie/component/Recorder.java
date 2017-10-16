package org.zhxie.component;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import au.com.bytecode.opencsv.CSVWriter;


public class Recorder {

  private static class Request {
    private int width;
    private int height;
    private String number;
    Request(int width, int height, String number) {
      this.width = width;
      this.height = height;
      this.number = number;
    }
  }

  private static CSVWriter writer;

  static {
    new LogThread().start();
    try {
      writer = new CSVWriter(new FileWriter(new File("statistic.csv")), ',', '"');
      writer.writeNext(new String[] { "Resolution", "FPS" });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static BlockingQueue<Request> queue = new LinkedBlockingQueue<>();

 
  public static void log(int width, int height, String number) {
    try {
      queue.put(new Request(width, height, number));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void close() {
    while (!queue.isEmpty()) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    try {
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static class LogThread extends Thread {

    private void logToFile(int width, int height, String number) throws IOException {
      writer.writeNext(new String[] { String.format("%sx%s", width, height), number });
    }

    @Override
    public void run() {
      while (true) {
        if (queue.isEmpty()) {
          try {
            Thread.sleep(50);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        } else {
          try {
            Request request = queue.take();
            logToFile(request.width, request.height, request.number);
          } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }

  }
}
