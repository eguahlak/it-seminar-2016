package info.itseminar.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Server implements Runnable, Context {
  private volatile boolean running = false;
  private final Map<String, Parser> parsers = new HashMap<>();
  private final int port;
  private File root;
  private Console console;
  private final BufferedReader in;
  
  public Server(int port) {
    this.port = port;
    if (Console.class.isAssignableFrom(getClass())) console = (Console)this;
    else console = new SystemConsole();
    in = new BufferedReader(new InputStreamReader(System.in));
    // Default value for root 
    root = new File(getClass().getResource("/").getPath());
    // Default parser is the JSON parser
    parser(new JsonParser());
    }

  public Server() {
    this(4711);
    }
    
  protected final Parser parser(String mime) {
    if (mime == null || !parsers.containsKey(mime)) mime = Parser.JSON;
    return parsers.get(mime);
    }
  
  protected final Server parser(Parser value) {
    parsers.put(value.getMime(), value);
    return this;
    }
  
  protected Server root(File root) {
    this.root = root;
    return this;
    }
  
  protected Server root(String path) {
    root = new File(path);
    return this;
    }
  
  @Override
  public File fileFrom(String path) throws FileNotFoundException {
    return new File(root, path);
    }
  
  @Override
  public Console console() { return console; }
  
  public Server console(Console console) {
    this.console = console;
    return this;
    }
  
  
  protected void command(String line) {
    if ("stop".equals(line)) stop();
    }
  
  public Server start() {
    new Thread(this).start();
    return this;
    }
  
  public void stop() {
    console.writeLine("\n$$ server stopping...");
    running = false;
    }
  
  @Override
  public void run() {
    running = true;
    try (ServerSocket server = new ServerSocket(port)) {
      new Thread(() -> {
          try { while (running) if (in.ready()) command(in.readLine()); }
          catch (IOException ioe) { System.err.println(ioe.getMessage()); }
          }).start();
      server.setSoTimeout(10000);
      console.writeLine("$$ Root: "+root.getAbsolutePath());
      console.writeLine("$$ Waiting for requests on "+port+"...");
      int i = 0;
      while (running) {
        try {
          Socket socket = server.accept();
          HttpService service = new HttpService(this, socket.getInputStream(), socket.getOutputStream());
          new Thread(service).start();
          }
        catch (SocketTimeoutException sto) {
          if (i == 0) console.writeLine("$$ .");
          else console.write(".");
          i = (i + 1)%100;
          } 
        }
      }
    catch (IOException ioe) {
      Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ioe);
      }
    finally {
      console.writeLine("$$ Server stopped");
      }
    }
  
  @Override
  public void report(String message) {
    System.err.print("\n$$ REPORTING '"+message+"'");
    }
  
  }
