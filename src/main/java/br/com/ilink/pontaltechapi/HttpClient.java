package br.com.ilink.pontaltechapi;

import br.com.ilink.pontaltechapi.exceptions.HttpClientException;
import br.com.ilink.pontaltechapi.exceptions.UnauthorizedException;
import br.com.ilink.pontaltechapi.models.SMSUserConfig;
import java.io.IOException;
import java.util.Properties;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;

public class HttpClient extends TrustConnection {

  private SMSUserConfig smsUserConfig;
  private static Properties prop;
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  static {
    try {
      prop = new Properties();
      prop.load(HttpClient.class.getResourceAsStream("/pontaltech-config.properties"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String POST(RestService restService, String value) {
    return new HttpClient()._POST(restService, value, null);
  }

  public static String POST(RestService restService, String value, SMSUserConfig smsUserConfig) {
    return new HttpClient()._POST(restService, value, smsUserConfig);
  }

  private String _POST(RestService restService, String value, SMSUserConfig smsUserConfig) {
    this.smsUserConfig = smsUserConfig;

    OkHttpClient client = this.buildClient();
    RequestBody body = RequestBody.create(JSON, value);

    Request request = new Request.Builder()
        .url(prop.getProperty("config.pontaltech.url") + restService.path)
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() || response.code() == 400) {
        return response.body().string();
      }
      if (response.code() == 401) {
        throw new UnauthorizedException(response.body().string());
      }
      throw new HttpClientException(response.body().string());
    } catch (IOException e) {
      throw new HttpClientException(e);
    }
  }

  @Override
  protected String authorization() {
    return String.format("Basic %s",
        Base64.encodeBase64String(
            String.format("%s:%s",
                ((smsUserConfig != null && smsUserConfig.getUsuario() != null) ? smsUserConfig
                    .getUsuario() : prop.getProperty("config.pontaltech.user")),
                ((smsUserConfig != null && smsUserConfig.getUsuario() != null) ? smsUserConfig
                    .getUsuario() : prop.getProperty("config.pontaltech.password"))).getBytes()));
  }
}
