package com.zis.musapp.gh.model.mediastore.audio;

import android.support.annotation.Nullable;
import auto.cursor.AutoCursor;
import com.zis.musapp.gh.model.mediastore.MediaColumns;

import static android.provider.MediaStore.Audio.AudioColumns.ALBUM;
import static android.provider.MediaStore.Audio.AudioColumns.ALBUM_ID;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST_ID;
import static android.provider.MediaStore.Audio.AudioColumns.COMPOSER;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.Audio.AudioColumns.TITLE;
import static android.provider.MediaStore.Audio.AudioColumns.TITLE_KEY;
import static android.provider.MediaStore.Audio.AudioColumns.TRACK;

public interface AudioColumns extends MediaColumns {
  @Nullable
  @AutoCursor.Column(name = ALBUM)
  public String album();

  @Nullable
  @AutoCursor.Column(name = ALBUM_ID)
  public Long albumId();

  @Nullable
  @AutoCursor.Column(name = ARTIST)
  public String artist();

  @Nullable
  @AutoCursor.Column(name = ARTIST_ID)
  public Long artistId();

  @Nullable
  @AutoCursor.Column(name = COMPOSER)
  public String composer();

  @Nullable
  @AutoCursor.Column(name = DURATION)
  public Long duration();

  @Nullable
  @AutoCursor.Column(name = TITLE)
  public String title();

  @Nullable
  @AutoCursor.Column(name = TITLE_KEY)
  public String titleKey();

  @Nullable
  @AutoCursor.Column(name = TRACK)
  public Long track();

}
