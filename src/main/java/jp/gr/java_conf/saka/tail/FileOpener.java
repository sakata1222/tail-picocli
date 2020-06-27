package jp.gr.java_conf.saka.tail;

import com.google.inject.Singleton;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class FileOpener implements IFileOpener {

  @Override
  public BufferedInputStream open(Path path) throws IOException {
    return new BufferedInputStream(Files.newInputStream(path));
  }
}
