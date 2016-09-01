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

package com.zis.musapp.gh.model.errors;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.List;

/**
 * Created by Zis{github.com/Zis} on 15/8/9.
 */
@SuppressWarnings("PMD.MethodNamingConventions")
@AutoValue
public abstract class ApiError implements Parcelable {

  public static TypeAdapter<ApiError> typeAdapter(final Gson gson) {
    return new AutoValue_ApiError.GsonTypeAdapter(gson);
  }

  public abstract String message();

  @Nullable
  public abstract String documentation_url();

  @Nullable
  public abstract List<Detail> errors();

  @AutoValue
  public abstract static class Detail implements Parcelable {

    public static TypeAdapter<Detail> typeAdapter(final Gson gson) {
      return new AutoValue_ApiError_Detail.GsonTypeAdapter(gson);
    }

    public abstract String resource();

    public abstract String field();

    public abstract String code();
  }
}
