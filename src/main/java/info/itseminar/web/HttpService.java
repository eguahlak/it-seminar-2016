package info.itseminar.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

class HttpService implements Runnable {
  private final Context context;
  private final InputStream in;
  private final OutputStream out;

  public HttpService(Context context, InputStream in, OutputStream out) {
    this.context = context;
    this.in = in;
    this.out = out;
    }

  @Override
  public void run() {
    try {
      Request request = new HttpRequest(context, in);
      Response response = new HttpResponse(context, out);
      String resource = request.getResource();
      boolean done = false;
      if ("/list".equals(resource)) {
        String html = "<html><body>";
        html += "<table><tr><th>Name</th></tr>";
        for (Method method : context.getClass().getDeclaredMethods()) {
//          if (method.getDeclaringClass() == Server.class) continue;
//          if (method.getDeclaringClass() == Object.class) continue;
          html += "<tr><td>"+method.getName()+"(...)</td></tr>";
          }
        html += "</table>";
        html += "</body></html>";
        response.type("html");
        response.send(html);
        done = true;
        }
      else if (resource.endsWith(".html")) {
        // resource: /welcome.html
        String middle = 
            resource.substring(1, 2).toUpperCase()+
            resource.substring(2, resource.indexOf("."));
        String methodName = request.getMethod()+middle+"Html";
        try {
          Method method = context.getClass().getMethod(methodName);
          String html = (String)method.invoke(context);
          response.type("html");
          response.send(html);
          done = true;
          }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
          }
        }
      else if (resource.startsWith("/service/")) {
        // Do service call here
        done = true;
        }
      if (!done) {
        File file = context.fileFrom(resource);
        response.send(file);
        }
      }
    catch (IOException ex) {
      ex.printStackTrace();
      context.report(ex.getMessage());
      }
    }
  
  }
