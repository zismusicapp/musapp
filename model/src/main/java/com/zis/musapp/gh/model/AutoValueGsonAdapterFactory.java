package com.zis.musapp.gh.model;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * Created by Zis{github.com/Zis} on 7/31/16.
 */
@GsonTypeAdapterFactory
public abstract class AutoValueGsonAdapterFactory implements TypeAdapterFactory {
  public static TypeAdapterFactory create() {
    return new AutoValueGson_AutoValueGsonAdapterFactory();
  }
}
