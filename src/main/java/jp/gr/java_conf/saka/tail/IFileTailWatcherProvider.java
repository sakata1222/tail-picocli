package jp.gr.java_conf.saka.tail;

import java.nio.file.Path;

public interface IFileTailWatcherProvider {

  IFileTailWatcher provide(Path file);
}
