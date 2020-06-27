package jp.gr.java_conf.saka.tail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.concurrent.ThreadFactory;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class TailTest {

  @Test
  void start() {
    var watcherProvider = mock(IFileTailWatcherProvider.class);
    var watcher = mock(IFileTailWatcher.class);
    var observer = mock(IFileChangeObserver.class);
    var waiter = mock(IStopRequestWaiter.class);
    var threadFactory = mock(ThreadFactory.class);

    Tail testTarget = new Tail(
      watcherProvider,
      observer,
      waiter,
      threadFactory
    );
    Path path = mock(Path.class);
    when(watcherProvider.provide(path)).thenReturn(watcher);
    var thread = mock(Thread.class);
    when(threadFactory.newThread(any())).thenReturn(thread);
    testTarget.start(path);

    verify(watcherProvider, times(1)).provide(same(path));
    verify(watcher, times(1)).addObserver(same(observer));
    verify(watcher, times(1)).start(same(path));
    ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
    verify(threadFactory, times(1)).newThread(captor.capture());
    Runnable requestWaitRunnable = captor.getValue();
    verify(waiter, never()).waitStopRequest();
    verify(watcher, never()).stop();
    requestWaitRunnable.run();
    verify(waiter, times(1)).waitStopRequest();
    verify(watcher, times(1)).stop();
  }
}
