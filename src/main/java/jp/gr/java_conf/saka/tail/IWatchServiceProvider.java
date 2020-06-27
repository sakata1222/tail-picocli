package jp.gr.java_conf.saka.tail;

import java.nio.file.Path;
import java.nio.file.WatchService;

public interface IWatchServiceProvider {

  WatchService newWatchService(Path path);
}
