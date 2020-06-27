package jp.gr.java_conf.saka.tail;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class FileTailWatcherTest {

  @Test
  void start() throws IOException, InterruptedException {
    var watchServiceProvider = mock(IWatchServiceProvider.class);
    var watcher = mock(WatchService.class);
    var opener = mock(IFileOpener.class);
    var observer = mock(IFileChangeObserver.class);
    var available = mock(ITailAvailable.class);

    FileTailWatcher testTarget = new FileTailWatcher(
      watchServiceProvider,
      opener,
      available
    );

    var file = mock(Path.class);
    var parent = mock(Path.class);
    var dummyInput = mock(BufferedInputStream.class);
    var key1 = mock(WatchKey.class);
    var key2 = mock(WatchKey.class);
    when(file.getParent()).thenReturn(parent);
    when(watchServiceProvider.newWatchService(file)).thenReturn(watcher);
    when(watcher.take()).thenReturn(key1, key2);
    when(opener.open(file)).thenReturn(dummyInput);
    when(dummyInput.readNBytes(1024)).thenReturn(
      "abc\n".getBytes(UTF_8),
      "efg\n".getBytes(UTF_8),
      "".getBytes(UTF_8),
      "hij".getBytes(UTF_8),
      "".getBytes(UTF_8),
      "Must not be read".getBytes(UTF_8)
    );
    when(available.isAvailable()).thenReturn(
      true,
      true,
      false
    );
    testTarget.addObserver(observer);
    testTarget.start(file);

    ArgumentCaptor<byte[]> updated = ArgumentCaptor.forClass(byte[].class);
    verify(observer, times(3)).update(updated.capture());
    assertThat(updated.getAllValues())
      .containsExactly(
        "abc\n".getBytes(UTF_8),
        "efg\n".getBytes(UTF_8),
        "hij".getBytes(UTF_8)
      );
    verify(available, times(3)).isAvailable();
    verify(key1, times(1)).reset();
    verify(key2, times(1)).reset();
  }

  @Test
  void start_throws_UncheckedIOException_when_IOException_is_thrown() throws IOException {
    var watchServiceProvider = mock(IWatchServiceProvider.class);
    var watcher = mock(WatchService.class);
    var opener = mock(IFileOpener.class);
    var available = mock(ITailAvailable.class);
    var file = mock(Path.class);
    when(watchServiceProvider.newWatchService(file)).thenReturn(watcher);
    var dummyEx = new IOException("Dummy");
    when(opener.open(file)).thenThrow(dummyEx);
    FileTailWatcher testTarget = new FileTailWatcher(
      watchServiceProvider,
      opener,
      available
    );
    assertThatThrownBy(() -> testTarget.start(file))
      .isExactlyInstanceOf(UncheckedIOException.class)
      .hasCause(dummyEx);
  }

  @Test
  void start_ends_when_InterruptedException_occurs() throws InterruptedException, IOException {
    var watchServiceProvider = mock(IWatchServiceProvider.class);
    var watcher = mock(WatchService.class);
    var opener = mock(IFileOpener.class);
    var observer = mock(IFileChangeObserver.class);
    var available = mock(ITailAvailable.class);

    FileTailWatcher testTarget = new FileTailWatcher(
      watchServiceProvider,
      opener,
      available
    );

    var file = mock(Path.class);
    var parent = mock(Path.class);
    var dummyInput = mock(BufferedInputStream.class);
    when(file.getParent()).thenReturn(parent);
    when(watchServiceProvider.newWatchService(file)).thenReturn(watcher);
    when(watcher.take()).thenThrow(new InterruptedException());
    when(opener.open(file)).thenReturn(dummyInput);
    when(dummyInput.readNBytes(1024)).thenReturn(
      "abc\n".getBytes(UTF_8),
      "efg\n".getBytes(UTF_8),
      "".getBytes(UTF_8),
      "hij".getBytes(UTF_8),
      "".getBytes(UTF_8),
      "Must not be read".getBytes(UTF_8)
    );
    when(available.isAvailable()).thenReturn(true);
    testTarget.addObserver(observer);
    var t = new Thread(
      () -> {
        try {
          testTarget.start(file);
        } catch (RuntimeException e) {
          e.printStackTrace();
        }
      }
    );
    t.interrupt();
    t.start();
    t.join();

    verify(available, times(1)).isAvailable();
    verify(available, times(1)).stop();
  }

  @Test
  void stop() {
    var watchServiceProvider = mock(IWatchServiceProvider.class);
    var opener = mock(IFileOpener.class);
    var available = mock(ITailAvailable.class);
    FileTailWatcher testTarget = new FileTailWatcher(
      watchServiceProvider,
      opener,
      available
    );
    testTarget.stop();
    verify(available, times(1)).stop();
  }
}
