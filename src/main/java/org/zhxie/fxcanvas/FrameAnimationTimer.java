package org.zhxie.fxcanvas;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FrameAnimationTimer extends AnimationTimer {

  private final static long[] frameTimes = new long[100];
  private static int frameTimeIndex = 0;
  private static boolean arrayFilled = false;

  private final StringProperty frameValProperty = new SimpleStringProperty();

  @Override
  public void handle(long now) {
    long oldFrameTime = frameTimes[frameTimeIndex];
    frameTimes[frameTimeIndex] = now;
    frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
    if (frameTimeIndex == 0) {
      arrayFilled = true;
    }
    if (arrayFilled) {
      long elapsedNanos = now - oldFrameTime;
      long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
      double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
      frameValProperty.set(String.format("%.3f", frameRate));
    }
  }

  public StringProperty frameValProperty() {
    return this.frameValProperty;
  }
}