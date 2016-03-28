package info.itseminar.web;

import java.io.File;
import java.io.FileNotFoundException;

public class ContextStub implements Context {
  private final Console console = new SystemConsole();
  
  @Override
  public Console console() {
    return console;
    }

  @Override
  public void report(HttpService service, String message) {
    console.writeLine("!! "+message);
    }

  @Override
  public File fileFrom(String path) throws FileNotFoundException {
    return new File("/Users/AKA/Sites"+path);
    }

  }
