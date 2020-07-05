package jp.gr.java_conf.saka.tail;

import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SystemOutFileChangeObserver implements IFileChangeObserver {

  @Inject
  public SystemOutFileChangeObserver() {
  }

  @Override
  public void update(byte[] buffer) {
    System.out.print(new String(buffer, StandardCharsets.UTF_8));
  }
}
