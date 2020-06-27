package jp.gr.java_conf.saka.tail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Path;

public interface IFileOpener {

  BufferedInputStream open(Path path) throws IOException;
}
