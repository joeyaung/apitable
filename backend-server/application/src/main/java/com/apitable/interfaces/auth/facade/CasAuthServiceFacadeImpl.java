package com.apitable.interfaces.auth.facade;

import com.apitable.interfaces.auth.model.AuthParam;
import com.apitable.interfaces.auth.model.UserAuth;
import com.apitable.interfaces.auth.model.UserLogout;
import com.apitable.user.entity.UserEntity;
import com.apitable.user.service.IUserService;
import com.google.gson.Gson;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CasAuthServiceFacadeImpl implements AuthServiceFacade {
  private static final Logger log = LoggerFactory.getLogger(CasAuthServiceFacadeImpl.class);

  private static final Gson gson = new Gson();

  private static final String CAS_EXTERNAL_ID = "CAS";

  private static final String REGISTER_TOKEN_URL =
      "https://dev-account.alpos.tech/klaw/unauthApi/v1/register/token";

  private static final String CAS_LOGOUT_URL =
      "https://localhost:8443/cas/logout?service=http://localhost:3000";

  @Resource private IUserService userService;

  /**
   * CAS SSO Login
   *
   * @param param login param
   * @return
   */
  @Override
  public UserAuth ssoLogin(AuthParam param) {
    UserAuth result = null;
    Response response = null;
    // 這邊的 username 是 token
    String token = param.getUsername();
    try {
      TrustManager[] trustAllCerts =
          new TrustManager[] {
            new X509TrustManager() {
              @Override
              public void checkClientTrusted(X509Certificate[] chain, String authType)
                  throws CertificateException {}

              @Override
              public void checkServerTrusted(X509Certificate[] chain, String authType)
                  throws CertificateException {}

              @Override
              public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
              }
            }
          };
      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new SecureRandom());
      OkHttpClient httpClient =
          new Builder()
              .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
              .hostnameVerifier((hostname, session) -> true)
              .build();
      Headers defaultHeader =
          new Headers.Builder()
              .add(
                  "User-Agent",
                  "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
              .add("Authorization", token)
              .build();
      Request request =
          new Request.Builder()
              .url(REGISTER_TOKEN_URL)
              .headers(defaultHeader)
              .method("GET", null)
              .build();
      Call call = httpClient.newCall(request);
      response = call.execute();
      boolean successful = response.isSuccessful();
      if (!successful) {
        return result;
      }
      ResponseBody body = response.body();
      if (Objects.isNull(body)) {
        throw new Exception("Response Body is Empty.");
      }
      String bodyString = new String(body.bytes());
      Map<String, Object> data = gson.fromJson(bodyString, Map.class);
      List<Map<String, Object>> resultList = (List<Map<String, Object>>) data.getOrDefault("result", new ArrayList<>());
      Map<String, Object> resultMap = CollectionUtils.isEmpty(resultList) ? new HashMap<>() : resultList.get(0);
      Map<String, Object> me = (Map<String, Object>) resultMap.getOrDefault("me", new HashMap<>());
      String email = "joe.yu@alp.com.tw";
      String name = MapUtils.getString(me, "name");
      UserEntity userEntity = userService.getByEmail(email);
      if (Objects.isNull(userEntity)) {
        Long userId = userService.createByExternalSystem(CAS_EXTERNAL_ID, name, null, email, null);
        result = new UserAuth(userId);
      } else {
        Long userId = userEntity.getId();
        result = new UserAuth(userId);
        userService.updateLoginTime(userId);
      }
    } catch (Exception e) {
      log.error("message: {}.", e.getMessage(), e);
    } finally {
      if (Objects.nonNull(response)) {
        response.close();
      }
    }
    return result;
  }

  @Override
  public UserLogout logout(UserAuth userAuth) {
    return new UserLogout(true, CAS_LOGOUT_URL);
  }
}
