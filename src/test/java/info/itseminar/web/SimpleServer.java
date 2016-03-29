package info.itseminar.web;

public class SimpleServer extends Server {

  public static void main(String... args) {
    new SimpleServer().start();
    }
  
  public String sayHello() { return "Hello World!"; }
  
  public String getWelcomeHtml() {
    return "<h1>Welcome to Madrid</h1>";
    }
    
  }

