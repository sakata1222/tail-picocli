package jp.gr.java_conf.saka.tail;

import java.util.concurrent.atomic.AtomicBoolean;

public class TailAvailable implements ITailAvailable {

  private final AtomicBoolean isAvailable = new AtomicBoolean(true);

  @Override
  public boolean isAvailable() {
    return isAvailable.get();
  }

  @Override
  public void stop() {
    isAvailable.set(false);
  }
}
