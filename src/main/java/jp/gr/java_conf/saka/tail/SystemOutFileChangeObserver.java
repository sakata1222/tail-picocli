package jp.gr.java_conf.saka.tail;

import com.google.inject.Singleton;
import java.nio.charset.StandardCharsets;

@Singleton
public class SystemOutFileChangeObserver implements IFileChangeObserver {

  @Override
  public void update(byte[] buffer) {
    System.out.print(new String(buffer, StandardCharsets.UTF_8));
  }
}
