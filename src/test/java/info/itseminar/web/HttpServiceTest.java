package info.itseminar.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class HttpServiceTest {
  private final Context context = new ContextStub();
  private final static String simpleGetHttp =
      "GET /index.html HTTP/1.1\n"+
      "Content-Type: text/html\n"+
      "\n";
  

  @Test
  public void testSimpleServiceResquest() {
    InputStream in = new ByteArrayInputStream(simpleGetHttp.getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    HttpService service = new HttpService(context, in, out);
    service.run();
    String response = new String(out.toByteArray(), Charset.forName("UTF-8"));
    assertThat(response, startsWith("HTTP/1.1 200 OK"));
    }
  
  }
