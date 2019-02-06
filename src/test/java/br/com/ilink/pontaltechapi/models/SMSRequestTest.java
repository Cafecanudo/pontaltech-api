package br.com.ilink.pontaltechapi.models;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import br.com.ilink.pontaltechapi.exceptions.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

public class SMSRequestTest {

  @Rule
  public ErrorCollector collector = new ErrorCollector();

  @Rule
  public ExpectedException exceptionGrabber = ExpectedException.none();

  @Test
  public void testTelefoneVazia() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage(
            "(para) Quantidade mínima da lista é 1 e máxima de [SEM LIMITES] itens, informou [0].");

    SMSRequest.builder()
        .mensagem(randomAlphabetic(160))
        .codigoInterno(randomNumeric(19))
        .build();
  }

  @Test
  public void testTelefoneInvalido() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage("(para) Quantidade mínimo 11 e máxima de 12 caracteres, informou [19].");

    SMSRequest.builder()
        .para(randomNumeric(19))
        .mensagem(randomAlphabetic(160))
        .codigoInterno(randomNumeric(19))
        .build();
  }

  @Test
  public void testTelefoneCaracteresInvalidos() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage("(para) Somente números.");

    SMSRequest.builder()
        .para(randomAlphabetic(12))
        .mensagem(randomAlphabetic(160))
        .codigoInterno(randomNumeric(19))
        .build();
  }

  @Test
  public void testMensagemVazia() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage("(mensagem) Não pode ser vazio.");

    SMSRequest.builder()
        .para(randomNumeric(12))
        .codigoInterno(randomNumeric(19))
        .build();
  }

  @Test
  public void testMensagemMax160Caracteres() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage("(mensagem) Quantidade máxima de 160 caracteres.");

    SMSRequest.builder()
        .para(randomNumeric(12))
        .mensagem(randomAlphabetic(161))
        .codigoInterno(randomNumeric(19))
        .build();
  }

  @Test
  public void testInteralIDVazio() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage("(codigoInterno) Não pode ser vazio.");

    SMSRequest.builder()
        .para(randomNumeric(12))
        .mensagem(randomAlphabetic(160))
        .build();
  }

  @Test
  public void testInteralIDMax20Caracteres() {
    exceptionGrabber.expect(ValidationException.class);
    exceptionGrabber
        .expectMessage(
            "(codigoInterno) Quantidade mínimo 0 e máxima de 20 caracteres, informou [21].");

    SMSRequest.builder()
        .para(randomNumeric(12))
        .mensagem(randomAlphabetic(160))
        .codigoInterno(randomNumeric(21))
        .build();
  }

}