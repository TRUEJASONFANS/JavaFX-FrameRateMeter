package org.zhxie.main;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.zhxie.component.Recorder;
import org.zhxie.component.SWTTestUtil;

import com.google.common.base.Function;

public class MyFXCanvasTest {

  public static void main(String[] args) {
    open(800, 600);
  }

  private static void open(final int width, final int height) {
    SWTTestUtil.openShell(String.format("FX Canvas (%dx%d)", width, height), width, height,
        new Function<Shell, Control>() {

      @Override
      public Control apply(Shell shell) {
        FXCanvasComposite fxCanvas = new FXCanvasComposite(shell, SWT.NONE, shell);
        new Thread(new AsyRunnableTask(shell,10000,600,400)).start();
        new Thread(new AsyRunnableTask(shell,20000,300, 200)).start();
        return fxCanvas;
      }

    });
    Recorder.close();
  }

  public static class AsyRunnableTask implements Runnable {

    private Shell shell;
    private long sleepMillsec;
    private int width;
    private int height;

    public AsyRunnableTask(Shell shell, long sleepMillsec, int width, int height) {
      super();
      this.shell = shell;
      this.sleepMillsec = sleepMillsec;
      this.width = width;
      this.height = height;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(sleepMillsec);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Display.getDefault().syncExec(() -> {
        if (!shell.isDisposed()) {
          shell.setSize(new Point(width, height));
        }
      });
    }
  }
}
