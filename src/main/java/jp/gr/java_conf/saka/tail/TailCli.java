package jp.gr.java_conf.saka.tail;

import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "tail", mixinStandardHelpOptions = true, version = "0.1")
public class TailCli implements Callable<Integer> {

  @Inject
  private Tail tail;

  @Parameters(index = "0", paramLabel = "file", description = "path to a target file")
  private Path file;

  public static void main(String[] args) {
    new CommandLine(TailCli.class, new ProductionGuiceFactory()).execute(args);
  }

  @Override
  public Integer call() {
    tail.start(file.toAbsolutePath());
    return 0;
  }
}
