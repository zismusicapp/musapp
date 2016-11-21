package com.zis.musapp.gh.features.choosesong;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AttrRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.annotations.NonNull;
import com.jakewharton.rxbinding.view.RxView;
import com.joanzapata.iconify.widget.IconButton;
import com.pavlospt.rxfile.RxFile;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import com.zis.musapp.gh.model.mediastore.MediaProviderHelper;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rx.Observable;

public class StartRecordWizardBottomSheetDialogFragment extends BottomSheetDialogFragment {

  public final static int LIMIT = 1000;

  public final static int TAKE_PHOTO_CODE = 101;
  public static final String BUNDLE = "bundle";
  public static final String EXTRA = "extra";

  @BindView(R.id.imagesRecycleView) RecyclerView mRecycleView;
  @BindView(R.id.new_clip) IconButton mNewClip;
  @BindView(R.id.new_photo) IconButton mNewPhoto;
  @BindView(R.id.next) IconButton mNext;
  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
      new BottomSheetBehavior.BottomSheetCallback() {

        @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {

          switch (newState) {
            case BottomSheetBehavior.STATE_HIDDEN:
              dismiss();
              break;
            case BottomSheetBehavior.STATE_EXPANDED:
              setStatusBarDim(false);

              break;
            case BottomSheetBehavior.STATE_DRAGGING:
              mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
              break;
            case BottomSheetBehavior.STATE_SETTLING:
              setStatusBarDim(true);
              break;
            case BottomSheetBehavior.STATE_COLLAPSED:
              setStatusBarDim(true);
              break;
          }
        }

        @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
      };
  private BottomSheetBehavior<View> mBehavior;
  private ImageRecyclerViewAdapter mAdapter;
  private ProgressDialog progressDialog;

  private void setStatusBarDim(boolean dim) {
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //  getActivity().getWindow()
    //      .setStatusBarColor(dim ? Color.TRANSPARENT
    //          : ContextCompat.getColor(getActivity(), getThemedResId(R.attr.colorPrimaryDark)));
    //}
  }

  private int getThemedResId(@AttrRes int attr) {
    TypedArray a = getActivity().getTheme().obtainStyledAttributes(new int[] { attr });
    int resId = a.getResourceId(0, 0);
    a.recycle();
    return resId;
  }

  public void showImagesList() {

    mRecycleView.setItemAnimator(null);

    Observable.merge(
        ///
        MediaProviderHelper.getImages(getActivity(), null, null, null,
            MediaStore.MediaColumns.DATE_ADDED
                + " DESC")//we use description to keep video id thumnail
            .filter(image -> !String.valueOf(image.id()).equals(image.description()))
            .onBackpressureBuffer(LIMIT),
        //
        MediaProviderHelper.getVideo(getActivity(), null, null, null,
            MediaStore.MediaColumns.DATE_ADDED + " DESC")
            .limit(LIMIT / 10)
            .doOnNext(this::createThumbnailInImageTable))

        //
        .onBackpressureBuffer(LIMIT + LIMIT / 10)
        .cast(MediaColumns.class)
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .compose(RxUtil.applyProgressDialog(progressDialog))

        .buffer(3, TimeUnit.SECONDS, LIMIT)
        .subscribe(mediaColumns -> mAdapter.addMedias(mediaColumns), RxUtil.ON_ERROR_LOGGER, () -> {
          mAdapter.sort();
          mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

          //bottomSheet.post(() -> {
          //  bottomSheet.requestLayout();
          //  bottomSheet.invalidate();
          //});
        });
  }

  private void createThumbnailInImageTable(Video video) {
    Observable<Image> images = MediaProviderHelper.getImages(getActivity(), null,
        MediaStore.Images.Media.DESCRIPTION + " = ?", new String[] { String.valueOf(video.id()) },
        null);

    boolean thumbExists = images.isEmpty().toBlocking().firstOrDefault(false);
    if (!thumbExists) {
      RxFile.getVideoThumbnail(video.data())
          .map(bitmap1 -> MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
              bitmap1, "", String.valueOf(video.id())))
          .subscribe(url -> {
            Log.d("Thumb", "Thumbnail added: " + url);
          }, RxUtil.ON_ERROR_LOGGER);
    }
  }

  @android.support.annotation.NonNull @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

    View view = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
    ButterKnife.bind(this, view);

    dialog.setContentView(view);

    GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getActivity(), 3);

    mRecycleView.setLayoutManager(staggeredGridLayoutManager);
    mAdapter = new ImageRecyclerViewAdapter(getActivity(), new ArrayList<>());
    mAdapter.setHasStableIds(true);
    mRecycleView.setAdapter(mAdapter);

    progressDialog = new ProgressDialog(getActivity());
    progressDialog.setMessage("Magic");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setCancelable(false);
    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.items_spacing);
    mRecycleView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

    mBehavior = BottomSheetBehavior.from((View) view.getParent());
    mBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);

    RxView.clicks(mNewPhoto)
        .compose(RxPermissions.getInstance(getActivity())
            .ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        .subscribe(aBoolean -> {
          if (aBoolean) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //cameraIntent.putExtra(MediaStore.Extra, outputFileUri);

            getActivity().startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
          }
        });

    RxView.clicks(mNewClip)
        .compose(RxPermissions.getInstance(getActivity())
            .ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        .subscribe(granted -> {
          if (granted) {
            CameraHelper.startRecord(getActivity());
          }
        });

    RxView.clicks(mNext).retry().subscribe(aVoid -> {
      Observable.from(mAdapter.getSelectedItems())
          .map(selectedIndex -> mAdapter.getMedias().get(selectedIndex))
          .toList()
          .cast(ArrayList.class)
          .compose(RxUtil.applyIOToMainThreadSchedulers())
          .compose(RxUtil.applyProgressDialog(progressDialog))
          .subscribe(mediaColumns -> {

            //TimeLineActivity.class
            Intent intent = new Intent(getActivity(), TestEffectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(BUNDLE, mediaColumns);
            intent.putExtra(EXTRA, bundle);
            startActivity(intent);
          });
    });

    return dialog;
  }

  @Override public void onStart() {
    super.onStart();

    // Must be done during an initialization phase like onCreate
    RxPermissions.getInstance(getActivity())
        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .subscribe(granted -> {
          if (granted) { // Always true pre-M
            showImagesList();
            //mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
          } else {
            // Oups permission denied
          }
        });
  }
}