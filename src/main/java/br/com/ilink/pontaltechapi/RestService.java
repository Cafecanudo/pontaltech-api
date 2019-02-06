package br.com.ilink.pontaltechapi;

public enum RestService {

  SendUniqueSMS("/single-sms"),
  CheckSMS("/sms-report");

  public String path;

  RestService(String path) {
    this.path = path;
  }

}
