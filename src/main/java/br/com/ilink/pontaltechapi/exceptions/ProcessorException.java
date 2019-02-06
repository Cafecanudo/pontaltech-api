package br.com.ilink.pontaltechapi.exceptions;

public class ProcessorException extends RuntimeException {

  public ProcessorException(Exception e) {
    super(e);
  }

}
