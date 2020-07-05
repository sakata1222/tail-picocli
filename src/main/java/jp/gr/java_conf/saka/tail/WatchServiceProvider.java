package jp.gr.java_conf.saka.tail;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.WatchService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WatchServiceProvider implements IWatchServiceProvider {

  @Inject
  public WatchServiceProvider() {
  }

  @Override
  public WatchService newWatchService(Path path) {
    try {
      return path.getFileSystem().newWatchService();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
