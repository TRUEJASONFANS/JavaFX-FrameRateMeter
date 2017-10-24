package org.zhxie.main;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Function;

public class SWTTestUtil {

  public static void openShell(String format, int weight, int height, Function<Shell, Control> function) {

    Display display = new Display();

    Shell shell = new Shell(display);

    shell.setSize(weight, height);
    shell.setLayout(new FillLayout());

    function.apply(shell);

    shell.open();

    while(!shell.isDisposed()) {
      if(!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
  }

}
