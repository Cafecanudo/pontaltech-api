package br.com.ilink.pontaltechapi;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

public abstract class TrustConnection {

  private static final SSLContext trustAllSslContext;
  private static final TrustManager[] trustAllCerts;

  static {
    try {
      trustAllCerts = TrustManagerBuild.build();
      trustAllSslContext = SSLContext.getInstance("SSL");
      trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  protected OkHttpClient buildClient() {
    return new OkHttpClient.Builder()
        .sslSocketFactory(trustAllSslContext.getSocketFactory(),
            (X509TrustManager) trustAllCerts[0])
        .connectTimeout(60L, TimeUnit.SECONDS)
        .callTimeout(60L, TimeUnit.SECONDS)
        .hostnameVerifier((host, session) -> true)
        .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
            .header("Authorization", authorization())
            .build()))
        .build();
  }

  protected abstract String authorization();

  public static class TrustManagerBuild {

    public static TrustManager[] build() {
      return new TrustManager[]{
          new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
              return new X509Certificate[]{};
            }
          }
      };
    }
  }
}
