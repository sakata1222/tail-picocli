package jp.gr.java_conf.saka.tail;

import dagger.Component;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import javax.inject.Singleton;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "tail", mixinStandardHelpOptions = true, version = "0.1")
public class TailCli implements Callable<Integer> {

  private Tail tail;

  @Parameters(index = "0", paramLabel = "file", description = "path to a target file")
  private Path file;

  @Inject
  public TailCli(Tail tail) {
    this.tail = tail;
  }

  public static void main(String[] args) {
    TailCli cli = DaggerTailCli_TailFactory.builder().build().newInstance();
    new CommandLine(cli).execute(args);
  }

  @Override
  public Integer call() {
    tail.start(file.toAbsolutePath());
    return 0;
  }

  @Singleton
  @Component(
    modules = {
      FileOpenerModule.class,
      FileTailWatchModule.class,
      FileChangeObserverModule.class,
      StopRequestWaiterModule.class,
      ThreadFactoryModule.class,
      WatchServiceProviderModule.class
    }
  )
  interface TailFactory {

    TailCli newInstance();
  }
}
