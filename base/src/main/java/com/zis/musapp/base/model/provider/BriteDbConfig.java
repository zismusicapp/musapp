package com.zis.musapp.base.model.provider;

import android.database.sqlite.SQLiteOpenHelper;
import com.google.auto.value.AutoValue;

/**
 * Created by Zis{github.com/Zis} on 5/12/16.
 */
@AutoValue
public abstract class BriteDbConfig {
  public static Builder builder() {
    return new AutoValue_BriteDbConfig.Builder();
  }

  public abstract boolean enableLogging();

  public abstract SQLiteOpenHelper sqliteOpenHelper();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder enableLogging(final boolean enableLogging);

    public abstract Builder sqliteOpenHelper(final SQLiteOpenHelper sqliteOpenHelper);

    public abstract BriteDbConfig build();
  }
}
