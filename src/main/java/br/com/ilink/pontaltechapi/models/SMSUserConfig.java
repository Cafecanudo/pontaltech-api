package br.com.ilink.pontaltechapi.models;

import lombok.Data;

@Data
public class SMSUserConfig {

  private String usuario;
  private String senha;

  public SMSUserConfig(String usuario, String senha) {
    this.usuario = usuario;
    this.senha = senha;
  }

  public static SMSUserConfig build(String usuario, String senha) {
    return new SMSUserConfig(usuario, senha);
  }
}
