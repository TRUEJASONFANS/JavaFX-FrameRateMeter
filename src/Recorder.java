

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.asml.jex.path.FileUtil;
import com.google.common.collect.Maps;

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
  
  static {
    new LogThread().start();
  }
  
  private static BlockingQueue<Request> queue = new LinkedBlockingQueue<>();

  private static final Map<String, CSVWriter> writers = Maps.newHashMap();
  
  public static void log(int width, int height, String number) {
    try {
      queue.put(new Request(width, height, number));
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
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
    writers.values().forEach(w -> {
      try {
        w.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });
  }
  
  private static class LogThread extends Thread {
    
    
    private CSVWriter getWriter(int width, int height) {
      String filename = String.format("%dx%d.csv", width, height);
      CSVWriter writer = writers.get(filename);
      if (writer == null) {
        try {
          writer = new CSVWriter(FileUtil.createWriter(filename), ',', '"');
          writer.writeNext(new String[] {"FPS"});
          writers.put(filename, writer);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
          e.printStackTrace();
        }
      }
      return writer;
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
            CSVWriter writer = getWriter(request.width, request.height);
            writer.writeNext(new String[] {String.valueOf(request.number)});
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    }
    
  }
}
