package com.zis.musapp.base.model.provider;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface ICloud {
  static String serviceProviderHeader = "X-Auth-Service-Provider";
  static String credentialsAuthorizationHeader = "X-Verify-Credentials-Authorization";

  @GET("/verify_credentials?provider=digits")
  Observable<String> verifyCredentials(
      @Header(serviceProviderHeader) String serviceProvider,
      @Header(credentialsAuthorizationHeader) String authorization,
      @Query("id") long id);
}