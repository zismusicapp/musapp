/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Zis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zis.musapp.base.model.provider;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;
import com.mosn.asyncmockwebserver.AsyncMockWebServer;
import com.mosn.asyncmockwebserver.Mock;
import com.mosn.asyncmockwebserver.MockDispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.zis.musapp.base.model.jsr310.ZonedDateTimeJsonConverter;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.ZonedDateTime;

import android.app.Application;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zis{github.com/Zis} on 15/9/6.
 */
@Module
public class ProviderModule {

  @Singleton
  @Provides EventBus provideEventBus(final EventBusConfig config) {
    return EventBus.builder()
        .logNoSubscriberMessages(config.logNoSubscriberMessages())
        .sendNoSubscriberEvent(config.sendNoSubscriberEvent())
        .eventInheritance(config.eventInheritance())
        .throwSubscriberException(config.throwSubscriberException())
        .build();
  }

  @Singleton
  @Provides Gson provideGson(final GsonConfig config) {
    final GsonBuilder builder = new GsonBuilder();
    if (config.autoGsonTypeAdapterFactory() != null) {
      builder.registerTypeAdapterFactory(config.autoGsonTypeAdapterFactory());
    }
    return builder.registerTypeAdapter(ZonedDateTime.class,
        new ZonedDateTimeJsonConverter(config.dateTimeFormatter()))
        .setDateFormat(config.dateFormatString())
        .setPrettyPrinting()
        .create();
  }

  @Singleton
  @Provides Retrofit provideRetrofit(final RetrofitConfig config, final OkHttpClient okHttpClient,
      final Gson gson) {
    return new Retrofit.Builder().baseUrl(config.baseUrl())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(
            RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build();
  }

  @Singleton
  @Named("mocked")
  @Provides Retrofit provideMockedRetrofit(Application application, final RetrofitConfig config, final OkHttpClient okHttpClient,
                                           final Gson gson) {

    AssetManager assetManager = application.getAssets();

    try {
      String mockString = inputStreamToString(assetManager.open("mock.json"));
      AsyncMockWebServer.addMock(Mock.create(
              "/search",
              new MockResponse().setResponseCode(200).setBody(mockString),
              request -> request.getPath().contains("/search")
      ));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new Retrofit.Builder().baseUrl(AsyncMockWebServer.getEndPoint())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(
                    RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build();
  }

  private String inputStreamToString(InputStream inputStream) throws IOException {
    final int bufferSize = 1024;
    final char[] buffer = new char[bufferSize];
    final StringBuilder out = new StringBuilder();
    Reader in = new InputStreamReader(inputStream, "UTF-8");
    for (; ; ) {
      int rsz = in.read(buffer, 0, buffer.length);
      if (rsz < 0)
        break;
      out.append(buffer, 0, rsz);
    }
    return out.toString();
  }


  @Singleton
  @Provides OkHttpClient provideHttpClient(final HttpClientConfig config) {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    if (config.enableLog()) {
      builder.addNetworkInterceptor(new StethoInterceptor())
          .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(final String message) {
              Timber.tag("OkHttp").d(message);
            }
          }).setLevel(HttpLoggingInterceptor.Level.BODY))
          .addInterceptor(new CurlInterceptor(new Loggable() {
            @Override
            public void log(final String message) {
              Timber.tag("Ok2Curl").d(message);
            }
          }));
    }
    return builder.build();
  }

  @Singleton
  @Provides BriteDatabase provideBriteDb(final BriteDbConfig config) {
    final BriteDatabase briteDb =
        SqlBrite.create().wrapDatabaseHelper(config.sqliteOpenHelper(), Schedulers.io());
    briteDb.setLoggingEnabled(config.enableLogging());
    return briteDb;
  }
}
