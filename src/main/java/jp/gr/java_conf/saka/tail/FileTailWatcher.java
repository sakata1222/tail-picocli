package jp.gr.java_conf.saka.tail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileTailWatcher implements IFileTailWatcher {

  private final IWatchServiceProvider watchServiceProvider;
  private final IFileOpener fileOpener;

  private final List<IFileChangeObserver> observerList = new ArrayList<>();
  private final ITailAvailable available;

  @Inject
  FileTailWatcher(IWatchServiceProvider watchServiceProvider,
    IFileOpener fileOpener, ITailAvailable available) {
    this.watchServiceProvider = watchServiceProvider;
    this.fileOpener = fileOpener;
    this.available = available;
  }

  @Override
  public void addObserver(IFileChangeObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void start(Path file) {
    try (WatchService watcher = watchServiceProvider.newWatchService(file);
      var bis = fileOpener.open(file)) {
      file.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
      while (available.isAvailable()) {
        load(bis);
        var key = watcher.poll(1, TimeUnit.SECONDS);
        if (Objects.nonNull(key)) {
          key.reset();
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (InterruptedException e) {
      // abort
      Thread.interrupted();
      available.stop();
    }
  }

  @Override
  public void stop() {
    available.stop();
  }

  private void load(BufferedInputStream bis) throws IOException {
    byte[] buffer;
    while ((buffer = bis.readNBytes(1024)).length > 0) {
      final var lambdaBuffer = buffer;
      observerList.forEach(o -> o.update(lambdaBuffer));
    }
  }
}
