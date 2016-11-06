package com.zis.musapp.gh.model.mediastore;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class QueryBuilder {
  Context context = null;
  Uri uri = null;
  String[] projection = null;
  String selection = null;
  String[] selectionArgs = null;
  String sortOrder = null;

  public static QueryBuilder create() {
    return new QueryBuilder();
  }

  public static QueryBuilder create(Context context) {
    return new QueryBuilder(context);
  }

  public QueryBuilder() {
    this(null);
  }

  public QueryBuilder(Context context) {
    this.context = context;
  }

  public QueryBuilder uri(Uri uri) {
    this.uri = uri;
    return this;
  }

  public QueryBuilder projection(String[] projection) {
    this.projection = projection;
    return this;
  }

  public QueryBuilder selection(String selection) {
    this.selection = selection;
    return this;
  }

  public QueryBuilder selectionArgs(String[] selectionArgs) {
    this.selectionArgs = selectionArgs;
    return this;
  }

  public QueryBuilder sortOrder(String sortOrder) {
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

  public Cursor build() {
    if (context == null) return null;
    return context.getContentResolver().query(
        uri(),
        projection(),
        selection(),
        selectionArgs(),
        sortOrder());
  }
}