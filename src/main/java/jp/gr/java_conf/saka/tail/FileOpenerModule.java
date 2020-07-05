package jp.gr.java_conf.saka.tail;

import dagger.Binds;
import dagger.Module;

@Module
public interface FileOpenerModule {

  @Binds
  IFileOpener bind(FileOpener bind);
}
