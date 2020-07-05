package jp.gr.java_conf.saka.tail;

import dagger.Binds;
import dagger.Module;

@Module
public interface FileTailWatchModule {

  @Binds
  IFileTailWatcherProvider bind(FileTailWatcherProvider bind);
}
