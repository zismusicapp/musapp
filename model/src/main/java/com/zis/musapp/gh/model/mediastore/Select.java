package com.zis.musapp.gh.model.mediastore;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Select {
  Activity activity = null;
  Context context = null;
  Uri uri = null;
  String[] projection = null;
  String selection = null;
  String[] selectionArgs = null;
  String sortOrder = null;

  public Select(String... projection) {
    projection(projection);
  }

  public Select() {
    projection(null);
  }

  public Select from(Uri uri, Context context) {
    return from(context, uri);
  }

  public Select from(Context context, Uri uri) {
    this.context = context;

    return uri(uri);
  }

  public Select from(Uri uri, Activity activity) {
    return from(activity, uri);
  }

  public Select from(Activity activity, Uri uri) {
    this.activity = activity;

    return from((Context) activity, uri);
  }

  public Select where(String selection, String[] selectionArgs) {
    return selection(selection).selectionArgs(selectionArgs);
  }

  public Select uri(Uri uri) {
    this.uri = uri;
    return this;
  }

  public Select projection(String[] projection) {
    this.projection = projection;
    return this;
  }

  public Select selection(String selection) {
    this.selection = selection;
    return this;
  }

  public Select selectionArgs(String[] selectionArgs) {
    this.selectionArgs = selectionArgs;
    return this;
  }

  public Select sortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
    return this;
  }

  public Uri uri() {
    return uri;
  }

  public String[] projection() {
    return projection;
  }

  public String selection() {
    return selection;
  }

  public String[] selectionArgs() {
    return selectionArgs;
  }

  public String sortOrder() {
    return sortOrder;
  }

  public Cursor query() {
    if (context == null) return null;
    return context.getContentResolver().query(
        uri(),
        projection(),
        selection(),
        selectionArgs(),
        sortOrder());
  }
}