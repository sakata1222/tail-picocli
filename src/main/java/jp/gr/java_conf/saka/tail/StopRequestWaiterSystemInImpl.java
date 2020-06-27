package jp.gr.java_conf.saka.tail;

import com.google.inject.Singleton;
import java.io.IOException;

@Singleton
public class StopRequestWaiterSystemInImpl implements IStopRequestWaiter {

  @Override
  public void waitStopRequest() {
    try {
      System.in.read();
    } catch (IOException ignored) {
    }
  }
}
