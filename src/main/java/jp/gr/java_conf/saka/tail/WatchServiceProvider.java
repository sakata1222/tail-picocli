package jp.gr.java_conf.saka.tail;

import com.google.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.WatchService;

@Singleton
public class WatchServiceProvider implements IWatchServiceProvider {

  @Override
  public WatchService newWatchService(Path path) {
    try {
      return path.getFileSystem().newWatchService();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
