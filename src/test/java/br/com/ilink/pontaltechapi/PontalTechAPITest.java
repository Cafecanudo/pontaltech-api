package br.com.ilink.pontaltechapi;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import br.com.ilink.pontaltechapi.exceptions.UnauthorizedException;
import br.com.ilink.pontaltechapi.models.SMSRequestCheck;
import br.com.ilink.pontaltechapi.models.SMSResponseError;
import br.com.ilink.pontaltechapi.models.SMSUserConfig;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClient.class, TrustConnection.class})
@PowerMockIgnore({"javax.net.ssl.*", "javax.security.*"})
public class PontalTechAPITest {

  @Rule
  ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testUsuarioOuSenhaInvalida() {
    expectedException.expect(UnauthorizedException.class);
    expectedException.expectMessage("{\"code\":99,\"message\":\"Unauthorized\"}");

    PontalTechAPI.SMSRequest req = PontalTechAPI.SMSRequest.builder()
        .para("62996020305")
        .para("62996020309")
        .mensagem("Mensagem de teste PontalTech")
        .codigoInterno("98486168")
        .build();

    PontalTechAPI.config(
        SMSUserConfig.build("USUARIO", "SENHA")
    )
        .prepare(req)
        .enviar();
  }

  @Test
  public void testEnvioMensagemSMS() {
    mockStatic(HttpClient.class);
    when(HttpClient.POST(eq(RestService.SendUniqueSMS), anyString(), any()))
        .thenReturn(
            "{\n"
                + "  \"id\": \"198141491\",\n"
                + "  \"to\": \"62996020305\",\n"
                + "  \"message\": \"Mensagem de teste PontalTech\",\n"
                + "  \"schedule\": \"2019-02-05T19:46:08Z\",\n"
                + "  \"reference\": \"98486168\",\n"
                + "  \"status\": \"0\",\n"
                + "  \"statusDescription\": \"Ok\"\n"
                + "}"
        );

    PontalTechAPI.SMSRequest req = PontalTechAPI.SMSRequest.builder()
        .para("62996020305")
        .para("62996020309")
        .mensagem("Mensagem de teste PontalTech")
        .codigoInterno("98486168")
        .build();

    List<PontalTechAPI.SMSResponse> resp = PontalTechAPI.preparar(req).enviar();

    assertThat(resp, is(notNullValue()));
    assertThat(resp, not(IsEmptyCollection.empty()));
    assertThat(resp, is(hasSize(2)));
  }

  @Test
  public void testErro400() {
    PontalTechAPI.SMSRequest req = new PontalTechAPI.SMSRequest();
    req.addPara("62996020305");
    req.setCodigoInterno("AAAA");

    List<PontalTechAPI.SMSResponse> resp = PontalTechAPI.preparar(req).enviar();
    assertThat(resp, is(notNullValue()));
    assertThat(resp, not(IsEmptyCollection.empty()));
    assertThat(resp, is(hasSize(1)));
    assertThat(resp.get(0).getStatus(), is("7"));
    assertThat(resp.get(0).getStatusDescription(), is("Blocked"));
    assertThat(resp.get(0).getError(), is(SMSResponseError.builder()
        .code(105)
        .message("Empty message")
        .build()));
  }

  @Test
  public void testConsultaProtocolo() throws IOException {
    SMSRequestCheck requestCheck = SMSRequestCheck.builder()
        .dataInicio(LocalDateTime.now().minusMinutes(5))
        .dataFim(LocalDateTime.now())
        .build();

    List<PontalTechAPI.SMSResponse> reports = PontalTechAPI.preparar(requestCheck)
        .check();

    assertThat(reports, is(notNullValue()));
    assertThat(reports, not(IsEmptyCollection.empty()));
  }

}
