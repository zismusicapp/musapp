package com.zis.musapp.gh.model.songs;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("song")
public class Song extends ParseObject {

  ParseFile file;

  String title;

}
