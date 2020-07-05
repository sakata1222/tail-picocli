package jp.gr.java_conf.saka.tail;

import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StopRequestWaiterSystemInImpl implements IStopRequestWaiter {

  @Inject
  public StopRequestWaiterSystemInImpl() {
  }

  @Override
  public void waitStopRequest() {
    try {
      System.in.read();
    } catch (IOException ignored) {
    }
  }
}
