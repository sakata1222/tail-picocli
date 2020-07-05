package jp.gr.java_conf.saka.tail;

import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Module
public class ThreadFactoryModule {

  @Provides
  static ThreadFactory provide() {
    return Executors.defaultThreadFactory();
  }
}
