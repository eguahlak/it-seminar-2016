package info.itseminar.web;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 */
public interface Context {
  Console console();
  void report(String message);
  File fileFrom(String path) throws FileNotFoundException;
  }
