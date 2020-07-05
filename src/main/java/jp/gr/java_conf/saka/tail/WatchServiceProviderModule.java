package jp.gr.java_conf.saka.tail;

import dagger.Binds;
import dagger.Module;

@Module
public interface WatchServiceProviderModule {

  @Binds
  IWatchServiceProvider bind(WatchServiceProvider bind);
}
