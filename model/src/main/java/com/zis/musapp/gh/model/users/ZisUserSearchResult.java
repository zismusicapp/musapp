package com.zis.musapp.gh.model.users;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.util.List;

/**
 * Created by Zis{github.com/Zis} on 15/7/23.
 */
@SuppressWarnings("PMD.MethodNamingConventions")
@AutoValue
public abstract class ZisUserSearchResult implements Parcelable {

  public static TypeAdapter<ZisUserSearchResult> typeAdapter(final Gson gson) {
    return new AutoValue_ZisUserSearchResult.GsonTypeAdapter(gson);
  }

  public abstract int total_count();

  public abstract boolean incomplete_results();

  public abstract List<ZisUser> items();
}
