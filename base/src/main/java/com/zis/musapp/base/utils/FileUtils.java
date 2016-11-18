package com.zis.musapp.base.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import rx.Observable;
import rx.Subscriber;

public class FileUtils {

  public static Observable<File> copyAssetsToExternalFilesDir(Context context, String relPath) {

    return Observable.defer(() -> Observable.create(new Observable.OnSubscribe<File>() {
      @Override public void call(Subscriber<? super File> subscriber) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
          files = assetManager.list(relPath);
        } catch (IOException e) {
          Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) {
          for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
              in = assetManager.open(relPath + "/" + filename);
              File outFile = new File(context.getExternalFilesDir(null), filename);
              out = new FileOutputStream(outFile);
              copyFile(in, out);
              subscriber.onNext(outFile);
            } catch (IOException e) {
              subscriber.onError(e);
            } finally {
              if (in != null) {
                try {
                  in.close();
                } catch (IOException e) {
                  subscriber.onError(e);
                }
              }
              if (out != null) {
                try {
                  out.close();
                } catch (IOException e) {
                  subscriber.onError(e);
                }
              }
            }
          }subscriber.onCompleted();
        }
      }
    }));
  }

  private static void copyFile(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024];
    int read;
    while ((read = in.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
  }

  public static byte[] read(File file) throws IOException {

    ByteArrayOutputStream ous = null;
    InputStream ios = null;
    try {
      byte[] buffer = new byte[4096];
      ous = new ByteArrayOutputStream();
      ios = new FileInputStream(file);
      int read = 0;
      while ((read = ios.read(buffer)) != -1) {
        ous.write(buffer, 0, read);
      }
    } finally {
      try {
        if (ous != null) {
          ous.close();
        }
      } catch (IOException e) {
      }

      try {
        if (ios != null) {
          ios.close();
        }
      } catch (IOException e) {
      }
    }
    return ous.toByteArray();
  }

  public static String saveBitmapToFile(String filename, Bitmap bmp) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(filename);
      bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
      // PNG is a lossless format, the compression factor (100) is ignored
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }

    return filename;
  }
}
