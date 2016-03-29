package info.itseminar.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

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
      if ("/list".equals(resource)) {
        String text = "Methods";
        for (Method method : context.getClass().getMethods()) {
          text += "\n"+method.getName()+"(...)";
          }
        response.type("txt");
        response.send(text);
        }
      else if (resource.startsWith("/service/")) {
        // Do service call here
        }
      else {
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
