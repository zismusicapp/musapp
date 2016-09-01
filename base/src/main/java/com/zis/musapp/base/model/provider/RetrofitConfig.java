package com.zis.musapp.base.model.provider;

import com.google.auto.value.AutoValue;

/**
 * Created by Zis{github.com/Zis} on 5/12/16.
 */
@AutoValue
public abstract class RetrofitConfig {
  public static Builder builder() {
    return new AutoValue_RetrofitConfig.Builder();
  }

  public abstract String baseUrl();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder baseUrl(final String baseUrl);

    public abstract RetrofitConfig build();
  }
}
