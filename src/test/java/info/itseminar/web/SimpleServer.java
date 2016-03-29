package info.itseminar.web;

public class SimpleServer extends Server {
  private int count = 0;

  public static void main(String... args) {
    new SimpleServer().start();
    }
  
  public String getHelloHtml() { 
    count++;
    return "<h1 style='color: red;'>Hello World!</h1>"; }
  
  public String getWelcomeHtml() {
    count += 1;
    return "<h1>Welcome to Madrid</h1>";
    }
  
  public String getCountHtml() {
    count = count + 1;
    return "<h1 style='font-size: 400%;'>#"+count+"</h1>";
    }
  
  public String getPersonDataHtml(Request request) {
    return 
        "<h1>Hello "+request.getParameter("name")+
        " you are "+request.getParameter("age")+
        " years old, and we assigned you the id "+request.getParameter("id")+
        "</h1>";
    }
    
  }

