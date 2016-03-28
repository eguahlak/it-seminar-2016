package info.itseminar.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.hamcrest.CoreMatchers.is;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import static org.junit.Assert.*;

public class HttpRequestTest {
  private final Mockery mockery = new JUnit4Mockery();
  private final Context context = new ContextStub();
  private final static String simpleGetHttp =
      "GET /index.html HTTP/1.1\n"+
      "Content-Type: text/html\n"+
      "\n";
  

  @Test
  public void testSimpleGet() throws IOException {
    InputStream in = new ByteArrayInputStream(simpleGetHttp.getBytes());
    Request request = new HttpRequest(context, in);
    assertThat(request.getResource(), is("/index.html"));
    assertThat(request.getMethod(), is("get"));
    assertThat(request.getContentType(), is("text/html"));
    assertThat(request.getHeaders().get("Content-Type"), is("text/html"));
    } 
  
  }
