package info.itseminar.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

class HttpRequest implements Request {
  private final Context context;
  private final String method;
  private final String resource;
  private final String protocol;
  private int contentLength = 0;
  private final byte[] body;
  
  private final Map<String, String> parameters = new HashMap<>();
  private final Map<String, String> headers = new HashMap<>();
  
  private String readLine(InputStream in) throws IOException {
    StringBuilder builder = new StringBuilder();
    do {
      // all characters in the header is ASCII = single byte characters
      char c = (char)in.read();
      if (c == '\r') continue;
      if (c == '\n' || c == 65535) break;
      builder.append(c);
      }
    while (true);
    context.console().writeLine("<< "+builder);
    return builder.toString();
    }

  private byte[] read(InputStream in, int number) throws IOException {
    byte[] buffer = new byte[number];
    int count = in.read(buffer);
    context.console().writeLine("----");
    context.console().writeLine(new String(buffer, Charset.forName("utf-8")));
    return buffer;
    }
  
  private void setQuery(String query) throws UnsupportedEncodingException {
    String[] parts = query.split("&");
    for (String part : parts) {
      String[] pair = part.split("=", 2);
      parameters.put(
          URLDecoder.decode(pair[0], "UTF-8"),
          URLDecoder.decode(pair[1], "UTF-8")
          );
      context.console().writeLine("\n## "+URLDecoder.decode(pair[0], "UTF-8")+"="+URLDecoder.decode(pair[1], "UTF-8"));
      }
    }
  
  HttpRequest(Context context, InputStream in) throws IOException {
    this.context = context;
    String first = readLine(in);
    String[] parts = first.split(" ");
    if (parts.length != 3) throw new IOException("Bad request: "+first);
    method = parts[0].toLowerCase();
    String[] resourceParts = parts[1].split("\\?", 2);
    resource = resourceParts[0];
    if (resourceParts.length == 2) setQuery(resourceParts[1]);
    protocol = parts[2];

    do {
      String line = readLine(in).trim();
      if (line.isEmpty()) break;
      String[] pair = line.split(":");
      String key = pair[0].trim();
      String value = pair[1].trim();
      headers.put(key, value);
      if (key.equalsIgnoreCase("Content-Length")) contentLength = Integer.valueOf(value);
      } while (true);
    byte[] buffer = read(in, contentLength);
    if ("application/x-www-form-urlencoded".equals(getContentType())) {
      setQuery(new String(buffer, "UTF-8"));
      body = new byte[0];
      }
    else body = buffer;
    }

  @Override
  public Map<String, String> getHeaders() {
    return headers;
    }

  @Override
  public Map<String, String> getParameters() {
    return parameters;
    }

  @Override
  public String getParameter(String key) {
    return parameters.get(key);
    }

  @Override
  public String getMethod() {
    return method;
    }

  @Override
  public String getResource() {
    return resource;
    }

  @Override
  public byte[] getBody() {
    return body;
    }
  
  @Override
  public boolean hasBody() {
    return contentLength > 0;
    }

  @Override
  public int getContentLength() {
    return contentLength;
    }

  @Override
  public String getProtocol() {
    return protocol;
    }

  @Override
  public final String getContentType() {
    String mime = headers.get("Content-Type");
    if (mime == null) mime = "text/plain";
    int pos = mime.indexOf(';');
    if (pos >= 0) mime = mime.substring(0, pos - 1);
    return mime;
    }
  }
