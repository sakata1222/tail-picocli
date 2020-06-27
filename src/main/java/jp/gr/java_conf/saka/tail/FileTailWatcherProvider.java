package jp.gr.java_conf.saka.tail;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.file.Path;

@Singleton
public class FileTailWatcherProvider implements IFileTailWatcherProvider {

  private final IWatchServiceProvider watchServiceProvider;
  private final IFileOpener fileOpener;

  @Inject
  public FileTailWatcherProvider(IWatchServiceProvider watchServiceProvider,
    IFileOpener fileOpener) {
    this.watchServiceProvider = watchServiceProvider;
    this.fileOpener = fileOpener;
  }

  @Override
  public IFileTailWatcher provide(Path path) {
    return new FileTailWatcher(watchServiceProvider, fileOpener, new TailAvailable());
  }
}
