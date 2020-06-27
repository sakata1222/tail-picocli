package jp.gr.java_conf.saka.tail;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

public class ProductionGuiceFactory implements IFactory {

  private final Injector injector = Guice.createInjector(new ProductionModule());

  @Override
  public <K> K create(Class<K> cls) throws Exception {
    try {
      return injector.getInstance(cls);
    } catch (ConfigurationException ex) { // no implementation found in Guice configuration
      return CommandLine.defaultFactory().create(cls); // fallback if missing
    }
  }

  static class ProductionModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(Callable.class).to(TailCli.class);
      bind(ThreadFactory.class).toInstance(Executors.defaultThreadFactory());
      bind(IFileOpener.class).to(FileOpener.class);
      bind(IWatchServiceProvider.class).to(WatchServiceProvider.class);
      bind(IFileTailWatcherProvider.class).to(FileTailWatcherProvider.class);
      bind(IFileChangeObserver.class).to(SystemOutFileChangeObserver.class);
      bind(IStopRequestWaiter.class).to(StopRequestWaiterSystemInImpl.class);
    }
  }
}
