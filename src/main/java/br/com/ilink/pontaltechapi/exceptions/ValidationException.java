package br.com.ilink.pontaltechapi.exceptions;

public class ValidationException extends RuntimeException {

  public ValidationException(String nameField, String except) {
    super(String.format("(%s) %s", nameField, except));
  }

  public ValidationException(String except) {
    super(except);
  }

  public ValidationException(ReflectiveOperationException e) {
    super(e);
  }
}
