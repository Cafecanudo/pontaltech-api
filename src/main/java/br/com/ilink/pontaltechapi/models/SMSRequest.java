package br.com.ilink.pontaltechapi.models;

import br.com.ilink.pontaltechapi.BuilderValidation;
import br.com.ilink.pontaltechapi.PontalTechAPI;
import br.com.ilink.pontaltechapi.annotations.DefaultBooleanIfNULL;
import br.com.ilink.pontaltechapi.annotations.ListNotBlank;
import br.com.ilink.pontaltechapi.annotations.ListOnlyNumber;
import br.com.ilink.pontaltechapi.annotations.NotBlank;
import br.com.ilink.pontaltechapi.annotations.Size;
import br.com.ilink.pontaltechapi.annotations.SizeEachList;
import br.com.ilink.pontaltechapi.annotations.SizeList;
import br.com.ilink.pontaltechapi.exceptions.ValidationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSRequest {

  @SizeList
  @ListNotBlank
  @ListOnlyNumber
  @SizeEachList(min = 11, max = 12)
  @Default
  private Set<String> para = new HashSet<>();

  @NotBlank
  @Size(max = 160, message = "Quantidade máxima de 160 caracteres.")
  private String mensagem;

  /**
   * Hora de agendamento (UTC)
   */
  @Setter(AccessLevel.NONE)
  private LocalDateTime agendado;

  /**
   * seu identificador, para facilitar suas buscas no futuro
   */
  @NotBlank
  @Size(max = 20)
  private String codigoInterno;

  /**
   * Remetente
   */
  private String de;

  /**
   * Identificador da conta a ser disparada a mensagem (possivel pegar essa informação no menu
   * Gerenciar > Contas no Pointer)
   */
  private Integer contaId;

  /**
   * Referencia da conta a ser usada, caso tenha algum centro de custo e gostaria de referenciar a
   * uma conta criada no Pointer
   */
  private String referenciaConta;

  /**
   * URL de callback para receber as atualizações de status desta mensagem
   */
  private String urlCallback;

  /**
   * Caso queira que a mensagem seja enviada no estilo FlashSMS
   */
  @DefaultBooleanIfNULL
  private Boolean smsUrgente;

  public SMSRequest addPara(String phone) {
    if (phone != null) {
      this.para.add(phone);
    }
    return this;
  }

  public void agendado(LocalDateTime agendado) {
    this.agendado = agendado;
  }

  public static class SMSRequestBuilder extends BuilderValidation<PontalTechAPI.SMSRequest> {

    private Set<String> para = new HashSet<>();

    public PontalTechAPI.SMSRequest.SMSRequestBuilder para(Set<String> phone) {
      if (phone != null && !phone.isEmpty()) {
        this.para.addAll(phone);
      }
      return this;
    }

    public PontalTechAPI.SMSRequest.SMSRequestBuilder para(String phone) {
      if (phone != null) {
        this.para.add(phone);
      }
      return this;
    }

    public PontalTechAPI.SMSRequest.SMSRequestBuilder agendado(LocalDateTime agendado) {
      this.agendado = agendado;
      return this;
    }

    public PontalTechAPI.SMSRequest.SMSRequestBuilder agendado(Date agendado) {
      this.agendado = agendado.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
      return this;
    }

    @Override
    public PontalTechAPI.SMSRequest build() throws ValidationException {
      return validarParametros(
          new PontalTechAPI.SMSRequest(this.para, this.mensagem, this.agendado, this.codigoInterno,
              this.de, this.contaId, this.referenciaConta, this.urlCallback, this.smsUrgente)
      );
    }
  }
}
