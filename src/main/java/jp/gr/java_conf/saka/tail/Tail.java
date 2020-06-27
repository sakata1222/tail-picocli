package jp.gr.java_conf.saka.tail;

import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.concurrent.ThreadFactory;

public class Tail {

  private final IFileTailWatcherProvider watcherProvider;

  private final IFileChangeObserver observer;

  private final IStopRequestWaiter stopRequestWaiter;

  private final ThreadFactory threadFactory;

  @Inject
  public Tail(IFileTailWatcherProvider watcherProvider,
    IFileChangeObserver observer, IStopRequestWaiter stopRequestWaiter,
    ThreadFactory threadFactory) {
    this.watcherProvider = watcherProvider;
    this.observer = observer;
    this.stopRequestWaiter = stopRequestWaiter;
    this.threadFactory = threadFactory;
  }

  void start(Path path) {
    var watcher = watcherProvider.provide(path);
    watcher.addObserver(observer);
    watcher.start(path);
    var stopRequestWaitThread = threadFactory.newThread(() -> {
      stopRequestWaiter.waitStopRequest();
      watcher.stop();
    });
    stopRequestWaitThread.setDaemon(true);
    stopRequestWaitThread.start();
  }
}
