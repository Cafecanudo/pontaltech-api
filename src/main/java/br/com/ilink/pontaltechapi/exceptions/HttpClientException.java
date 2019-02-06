package br.com.ilink.pontaltechapi.exceptions;

import java.io.IOException;

public class HttpClientException extends RuntimeException {

  public HttpClientException(IOException e) {
    super(e);
  }

  public HttpClientException() {
    super();
  }

  public HttpClientException(String message) {
    super(message);
  }
}
