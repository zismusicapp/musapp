package com.zis.musapp.gh.features.choosesong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.joanzapata.iconify.widget.IconButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import java.io.File;
import java.io.IOException;

public class CropFilterImageActivity extends BaseActivity
    implements CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener {

  @AutoBundleField Uri source;
  @AutoBundleField CropImageOptions mOptions;

  @BindView(R.id.showFilters) IconButton showFilters;

  @Override protected boolean hasArgs() {
    return true;
  }

  private CropImageView mCropImageView;
  private FiltersBottomSheetFragment filtersBottomSheetFragment;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.crop_filter_image_activity);

    mCropImageView = (CropImageView) findViewById(R.id.cropImageView);

    if (savedInstanceState == null) {
      mCropImageView.setImageUriAsync(source);
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      String title = mOptions.activityTitle != null && !mOptions.activityTitle.isEmpty()
          ? mOptions.activityTitle : getResources().getString(
          com.theartofdev.edmodo.cropper.R.string.crop_image_activity_title);
      actionBar.setTitle(title);
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    filtersBottomSheetFragment = new FiltersBottomSheetFragment();

    final BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {
        Log.e("onStateChanged", "onStateChanged:" + newState);
      }

      @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        Log.e("onSlide", "onSlide");
      }
    });
  }

  @Override public void startBussinies() {
    RxView.clicks(showFilters).subscribe(aVoid -> {
      filtersBottomSheetFragment.show(getSupportFragmentManager(), "filters");
    });
  }

  @Override protected void initializeInjector() {

  }

  @Override protected void onStart() {
    super.onStart();
    mCropImageView.setOnSetImageUriCompleteListener(this);
    mCropImageView.setOnCropImageCompleteListener(this);
  }

  @Override protected void onStop() {
    super.onStop();
    mCropImageView.setOnSetImageUriCompleteListener(null);
    mCropImageView.setOnCropImageCompleteListener(null);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(com.theartofdev.edmodo.cropper.R.menu.crop_image_menu, menu);

    if (!mOptions.allowRotation) {
      menu.removeItem(com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_left);
      menu.removeItem(com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_right);
    } else if (mOptions.allowCounterRotation) {
      menu.findItem(com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_left)
          .setVisible(true);
    }

    Drawable cropIcon = null;
    try {
      cropIcon = ContextCompat.getDrawable(this,
          com.theartofdev.edmodo.cropper.R.drawable.crop_image_menu_crop);
      if (cropIcon != null) {
        menu.findItem(com.theartofdev.edmodo.cropper.R.id.crop_image_menu_crop).setIcon(cropIcon);
      }
    } catch (Exception e) {
    }

    if (mOptions.activityMenuIconColor != 0) {
      updateMenuItemIconColor(menu, com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_left,
          mOptions.activityMenuIconColor);
      updateMenuItemIconColor(menu,
          com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_right,
          mOptions.activityMenuIconColor);
      if (cropIcon != null) {
        updateMenuItemIconColor(menu, com.theartofdev.edmodo.cropper.R.id.crop_image_menu_crop,
            mOptions.activityMenuIconColor);
      }
    }

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == com.theartofdev.edmodo.cropper.R.id.crop_image_menu_crop) {
      cropImage();
      return true;
    }
    if (item.getItemId() == com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_left) {
      rotateImage(-mOptions.rotationDegrees);
      return true;
    }
    if (item.getItemId() == com.theartofdev.edmodo.cropper.R.id.crop_image_menu_rotate_right) {
      rotateImage(mOptions.rotationDegrees);
      return true;
    }
    if (item.getItemId() == android.R.id.home) {
      setResultCancel();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    setResultCancel();
  }

  @Override public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
    if (error == null) {
      if (mOptions.initialCropWindowRectangle != null) {
        mCropImageView.setCropRect(mOptions.initialCropWindowRectangle);
      }
      if (mOptions.initialRotation > -1) {
        mCropImageView.setRotatedDegrees(mOptions.initialRotation);
      }
    } else {
      setResult(null, error, 1);
    }
  }

  @Override public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
    setResult(result.getUri(), result.getError(), result.getSampleSize());
  }

  //region: Private methods

  /**
   * Execute crop image and save the result tou output uri.
   */
  protected void cropImage() {
    if (mOptions.noOutputImage) {
      setResult(null, null, 1);
    } else {
      Uri outputUri = getOutputUri();
      mCropImageView.saveCroppedImageAsync(outputUri, mOptions.outputCompressFormat,
          mOptions.outputCompressQuality, mOptions.outputRequestWidth, mOptions.outputRequestHeight,
          mOptions.outputRequestSizeOptions);
    }
  }

  /**
   * Rotate the image in the crop image view.
   */
  protected void rotateImage(int degrees) {
    mCropImageView.rotateImage(degrees);
  }

  /**
   * Get Android uri to save the cropped image into.<br>
   * Use the given in options or create a temp file.
   */
  protected Uri getOutputUri() {
    Uri outputUri = mOptions.outputUri;
    if (outputUri.equals(Uri.EMPTY)) {
      try {
        String ext = mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG ? ".jpg"
            : mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
        outputUri = Uri.fromFile(File.createTempFile("cropped", ext, getCacheDir()));
      } catch (IOException e) {
        throw new RuntimeException("Failed to create temp file for output image", e);
      }
    }
    return outputUri;
  }

  /**
   * Result with cropped image data or error if failed.
   */
  protected void setResult(Uri uri, Exception error, int sampleSize) {
    int resultCode = error == null ? RESULT_OK : CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
    setResult(resultCode, getResultIntent(uri, error, sampleSize));
    finish();
  }

  /**
   * Cancel of cropping activity.
   */
  protected void setResultCancel() {
    setResult(RESULT_CANCELED);
    finish();
  }

  /**
   * Get intent instance to be used for the result of this activity.
   */
  protected Intent getResultIntent(Uri uri, Exception error, int sampleSize) {
    CropImage.ActivityResult result =
        new CropImage.ActivityResult(null, uri, error, mCropImageView.getCropPoints(),
            mCropImageView.getCropRect(), mCropImageView.getRotatedDegrees(), sampleSize);
    Intent intent = new Intent();
    intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result);
    return intent;
  }

  /**
   * Update the color of a specific menu item to the given color.
   */
  private void updateMenuItemIconColor(Menu menu, int itemId, int color) {
    MenuItem menuItem = menu.findItem(itemId);
    if (menuItem != null) {
      Drawable menuItemIcon = menuItem.getIcon();
      if (menuItemIcon != null) {
        try {
          menuItemIcon.mutate();
          menuItemIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
          menuItem.setIcon(menuItemIcon);
        } catch (Exception e) {
        }
      }
    }
  }
  //endregion
}


