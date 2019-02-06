package br.com.ilink.pontaltechapi;

import br.com.ilink.pontaltechapi.exceptions.ProcessorException;
import br.com.ilink.pontaltechapi.exceptions.ValidationException;
import br.com.ilink.pontaltechapi.models.SMSRequest;
import br.com.ilink.pontaltechapi.models.SMSResponse;
import br.com.ilink.pontaltechapi.models.SMSUserConfig;
import br.com.ilink.pontaltechapi.models.SingleSMS;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PontalTechAPI {

  public static Config config(SMSUserConfig smsUserConfig) {
    return new PontalTechAPI.Config(smsUserConfig);
  }

  public static EnviarSMS preparar(SMSRequest req) {
    return new PontalTechAPI.EnviarSMS(req);
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

    public List<SMSResponse> envia() {
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
              .flashSms(req.isSmsUrgente())
              .build())
          .map(this::processar)
          .collect(Collectors.toList());
    }

    private SMSResponse processar(SingleSMS sms) {
      try {
        String json = HttpClient
            .POST(RestService.SendUniqueSMS, ConverterUtil.toJsonString(sms), smsUserConfig);
        return ConverterUtil.toObject(json, SMSResponse.class);
      } catch (IOException e) {
        throw new ProcessorException(e);
      }
    }
  }
}
