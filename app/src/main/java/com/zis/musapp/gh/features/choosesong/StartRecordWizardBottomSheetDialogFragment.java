package com.zis.musapp.gh.features.choosesong;

import android.Manifest;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AttrRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.annotations.NonNull;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import com.zis.musapp.gh.model.mediastore.MediaProviderHelper;
import com.zis.musapp.gh.pagination.utils.pagination.PaginationTool;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import rx.functions.Actions;

public class StartRecordWizardBottomSheetDialogFragment extends BottomSheetDialogFragment {

  int LIMIT = 50;

  @BindView(R.id.imagesRecycleView) RecyclerView mRecycleView;
  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
      new BottomSheetBehavior.BottomSheetCallback() {

        @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {

          switch (newState) {
            case BottomSheetBehavior.STATE_HIDDEN:
              dismiss();
              break;
            case BottomSheetBehavior.STATE_EXPANDED:
              setStatusBarDim(false);
              bottomSheet.post(() -> {
                bottomSheet.requestLayout();
                bottomSheet.invalidate();
                showImagesList();
              });

              break;
            case BottomSheetBehavior.STATE_DRAGGING:
              setStatusBarDim(true);
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

  private void setStatusBarDim(boolean dim) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getActivity().getWindow()
          .setStatusBarColor(dim ? Color.TRANSPARENT
              : ContextCompat.getColor(getActivity(), getThemedResId(R.attr.colorPrimaryDark)));
    }
  }

  private int getThemedResId(@AttrRes int attr) {
    TypedArray a = getActivity().getTheme().obtainStyledAttributes(new int[] { attr });
    int resId = a.getResourceId(0, 0);
    a.recycle();
    return resId;
  }

  public void showImagesList() {

    PaginationTool.Builder<MediaColumns> paginationBuilder =
        PaginationTool.buildPagingObservable(mRecycleView,
            offset -> MediaProviderHelper.getImages(getActivity(), null, null, null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC LIMIT " + LIMIT + " OFFSET " + offset)
                .cast(MediaColumns.class)).setLimit(LIMIT);//.setRetryCount(3);

    mRecycleView.setItemAnimator(null);

    PaginationTool<MediaColumns> paginationTool = paginationBuilder.build();
    //
    //MediaProviderHelper.getVideo(getActivity(), null, null, null,
    //    MediaStore.MediaColumns.DATE_ADDED + " DESC")
    //    .cast(MediaColumns.class)
    //    .toList()
    //    //don't do mAdapter.notifyDataSetChanged(); , we wait until images
    //    .subscribe(mediaColumns -> mAdapter.addMedias(mediaColumns), RxUtil.ON_ERROR_LOGGER);

    paginationTool.getPagingObservable()
        .compose(RxUtil.applyIOToMainThreadSchedulers())
        .doOnNext(images -> {
          mAdapter.addMedias(images);
        })
        //.buffer(10000, TimeUnit.MILLISECONDS, LIMIT)
        .subscribe(Actions.empty(), RxUtil.ON_ERROR_LOGGER);
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

    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.items_spacing);
    mRecycleView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

    mBehavior = BottomSheetBehavior.from((View) view.getParent());
    mBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);

    return dialog;
  }

  @Override public void onStart() {
    super.onStart();
    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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