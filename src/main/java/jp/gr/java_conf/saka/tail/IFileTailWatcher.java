package jp.gr.java_conf.saka.tail;

import java.nio.file.Path;

public interface IFileTailWatcher {

  void start(Path file);

  void addObserver(IFileChangeObserver observer);

  void stop();
}
