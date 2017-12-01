package org.zhxie.main;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.zhxie.record.Recorder;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class MyFXCanvasTest {

  public static void main(String[] args) {
    // launch(args);
    open(800, 600);
  }

  private static void open(final int width, final int height) {
    SWTTestUtil.openShell(String.format("FX Canvas Frame Rater (%dx%d)", width, height), width, height,
        new Function<Shell, Control>() {

      @Override
      public Control apply(Shell shell) {
        FXCanvasComposite fxCanvas = new FXCanvasComposite(shell, SWT.NONE);
        ModifySizeThread modifySizeThread = new ModifySizeThread(shell);
        modifySizeThread.addTask(600, 400);
        modifySizeThread.addTask(300, 200);
        modifySizeThread.addTask(0, 0);
        modifySizeThread.start();
        return fxCanvas;
      }

    });
    Recorder.close();
  }

  public static class ModifySizeThread extends Thread {

    private Shell shell;
    private static List<Point> queue = Lists.newArrayList();

    public ModifySizeThread(Shell shell) {
      super();
      this.shell = shell;

    }

    public void addTask(int width, int height) {
      queue.add(new Point(width, height));
    }

    @Override
    public void run() {
      for(Point p : queue) {
        int width = p.x;
        int height = p.y;
        try {
          Thread.sleep(15000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        Display.getDefault().syncExec(() -> {
          if (!shell.isDisposed()) {
            if(width ==0 && height ==0) {
              shell.close();
            }
            else {
              shell.setSize(new Point(width, height));
            }
          }
        });
      }
    }
  }

}
