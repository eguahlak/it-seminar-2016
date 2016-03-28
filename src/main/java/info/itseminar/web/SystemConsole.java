package info.itseminar.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemConsole implements Console {
  private final BufferedReader in;

  public SystemConsole() {
    in = new BufferedReader(new InputStreamReader(System.in));
    }

  @Override
  public String readLine() {
    try {
      return in.readLine();
      }
    catch (IOException ex) {
      return null;
      }
    }

  @Override
  public void writeLine(String line) {
    System.out.print("\n"+line);
    }
  
  }
