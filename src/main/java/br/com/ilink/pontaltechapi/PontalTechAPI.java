package br.com.ilink.pontaltechapi;

import br.com.ilink.pontaltechapi.exceptions.ProcessorException;
import br.com.ilink.pontaltechapi.exceptions.ValidationException;
import br.com.ilink.pontaltechapi.models.SMSRequestCheck;
import br.com.ilink.pontaltechapi.models.SMSUserConfig;
import br.com.ilink.pontaltechapi.models.SingleSMS;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

public class PontalTechAPI {

  @NoArgsConstructor
  public static class SMSRequest extends br.com.ilink.pontaltechapi.models.SMSRequest {

    public SMSRequest(Set<String> para, String mensagem, LocalDateTime agendado,
        String codigoInterno, String de, Integer contaId, String referenciaConta,
        String urlCallback,
        Boolean smsUrgente) {
      super(para, mensagem, agendado, codigoInterno, de, contaId, referenciaConta, urlCallback,
          smsUrgente);
    }
  }

  public static class SMSResponse extends br.com.ilink.pontaltechapi.models.SMSResponse {

  }

  public static class SMSResponseCheck extends br.com.ilink.pontaltechapi.models.SMSResponseCheck {

  }

  public static Config config(SMSUserConfig smsUserConfig) {
    return new PontalTechAPI.Config(smsUserConfig);
  }

  public static EnviarSMS preparar(PontalTechAPI.SMSRequest req) {
    return new PontalTechAPI.EnviarSMS(req);
  }

  public static CheckSMS preparar(SMSRequestCheck req) {
    return new PontalTechAPI.CheckSMS(req);
  }

  public static class Config {

    private SMSUserConfig smsUserConfig;

    public Config(SMSUserConfig smsUserConfig) {
      this.smsUserConfig = smsUserConfig;
    }

    public EnviarSMS prepare(SMSRequest req) {
      return new PontalTechAPI.EnviarSMS(req, smsUserConfig);
    }

  }

  /**
   * Classe para checar SMS
   */
  public static class CheckSMS {

    private SMSRequestCheck req;
    private SMSUserConfig smsUserConfig;

    public CheckSMS(SMSRequestCheck req) {
      this.req = req;
    }

    public CheckSMS(SMSRequestCheck req, SMSUserConfig smsUserConfig) {
      this.req = req;
      this.smsUserConfig = smsUserConfig;
    }

    public List<PontalTechAPI.SMSResponse> check() throws IOException {
      String json = HttpClient
          .POST(RestService.CheckSMS, ConverterUtil.toJsonString(req), smsUserConfig);
      return ConverterUtil.toObject(json, PontalTechAPI.SMSResponseCheck.class).getReports();
    }
  }

  /**
   * Classe de envio SMS
   */
  public static class EnviarSMS {

    private SMSRequest req;
    private SMSUserConfig smsUserConfig;

    public EnviarSMS(SMSRequest req) {
      this.req = req;
    }

    public EnviarSMS(SMSRequest req, SMSUserConfig smsUserConfig) {
      this.req = req;
      this.smsUserConfig = smsUserConfig;
    }

    public List<PontalTechAPI.SMSResponse> enviar() {
      if (req == null) {
        throw new ValidationException("Requisição está fazia");
      }
      return req.getPara().stream()
          .map(phone -> SingleSMS.builder()
              .to(phone)
              .message(req.getMensagem())
              .schedule(req.getAgendado())
              .reference(req.getCodigoInterno())
              .account(req.getContaId())
              .accountReference(req.getReferenciaConta())
              .from(req.getDe())
              .urlCallback(req.getUrlCallback())
              .flashSms(req.getSmsUrgente())
              .build())
          .map(this::processar)
          .collect(Collectors.toList());
    }

    private PontalTechAPI.SMSResponse processar(SingleSMS sms) {
      try {
        String json = HttpClient
            .POST(RestService.SendUniqueSMS, ConverterUtil.toJsonString(sms), smsUserConfig);
        return ConverterUtil.toObject(json, PontalTechAPI.SMSResponse.class);
      } catch (IOException e) {
        throw new ProcessorException(e);
      }
    }
  }
}
