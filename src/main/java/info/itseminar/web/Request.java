package info.itseminar.web;

import java.util.Map;

/**
 * 
 */
public interface Request {
  /**
   * Get a map of headers.
   * @return 
   */
  Map<String, String> getHeaders();
  Map<String, String> getParameters();
  byte[] getBodyBytes();
  String getBodyString();

  String getParameter(String key);
  String getMethod();
  /**
   * Gets the resource of the request.
   * The resource is the second value in the first line of the HTTP request
   * header.
   * @return the resource 
   */
  String getResource();
  
  /**
   * Whether this request has a body.
   * The request has a body if the <code>Content-Length</code> header is set and
   * is not 0
   * 
   * @return <code>true</code> if the request has a body.
   * 
   */
  boolean hasBody();
  /**
   * Get the value of the <code>Content-Length</code> header.
   * Convenience method. 
   * 
   * @return the content length
   */
  int getContentLength();
  String getContentType();
  String getProtocol();
  
  }
