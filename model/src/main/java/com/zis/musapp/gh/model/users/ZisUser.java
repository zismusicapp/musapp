package com.zis.musapp.gh.model.users;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;
import com.zis.musapp.base.model.jsr310.ZonedDateTimeDelightAdapter;
import com.zis.musapp.gh.model.DateTimeFormatterProvider;
import org.threeten.bp.ZonedDateTime;

/**
 * Created by Zis{github.com/Zis} on 15/7/23.
 */
@SuppressWarnings("PMD.MethodNamingConventions")
@AutoValue
public abstract class ZisUser implements ZisUserModel, Parcelable {

  public static final String GITHUB_USER_TYPE_USER = "User";
  public static final String GITHUB_USER_TYPE_ORGANIZATION = "Organization";

  public static final String ICONIFY_ICONS_USER = "{md-person}";
  public static final String ICONIFY_ICONS_ORG = "{md-people}";

  static final Factory<ZisUser> FACTORY = new Factory<>(AutoValue_ZisUser::new,
      new ZonedDateTimeDelightAdapter(DateTimeFormatterProvider.provideDateTimeFormatter()));

  static final RowMapper<ZisUser> MAPPER = FACTORY.get_allMapper();

  public static TypeAdapter<ZisUser> typeAdapter(final Gson gson) {
    return new AutoValue_ZisUser.GsonTypeAdapter(gson);
  }

  @NonNull
  public static Builder builder() {
    return new AutoValue_ZisUser.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder id(@Nullable Long id);

    public abstract Builder login(String login);

    public abstract Builder avatar_url(String avatarUrl);

    public abstract Builder type(String type);

    public abstract Builder created_at(@Nullable ZonedDateTime createdAt);

    public abstract ZisUser build();
  }
}
