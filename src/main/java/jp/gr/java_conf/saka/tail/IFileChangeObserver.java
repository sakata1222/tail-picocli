package jp.gr.java_conf.saka.tail;

public interface IFileChangeObserver {

  void update(byte[] buffer);
}
