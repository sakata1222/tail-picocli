package jp.gr.java_conf.saka.tail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FileOpener implements IFileOpener {

  @Inject
  public FileOpener() {
  }

  @Override
  public BufferedInputStream open(Path path) throws IOException {
    return new BufferedInputStream(Files.newInputStream(path));
  }
}
