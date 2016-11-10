package com.zis.musapp.gh.features.choosesong;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import butterknife.BindView;
import com.android.annotations.NonNull;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.MediaProviderHelper;
import com.zis.musapp.gh.pagination.utils.pagination.PaginationTool;
import rx.Observable;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StartRecordWizardBottomSheetDialogFragment extends BottomSheetDialogFragment {

  @BindView(R.id.imagesRecycleView) RecyclerView mRecycleView;
  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
      new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

          switch (newState) {
            case BottomSheetBehavior.STATE_HIDDEN:
              dismiss();
              break;
            case BottomSheetBehavior.STATE_EXPANDED:
              break;
            case BottomSheetBehavior.STATE_DRAGGING:
              break;
            case BottomSheetBehavior.STATE_SETTLING:
              break;
            case BottomSheetBehavior.STATE_COLLAPSED:
              break;
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
      };
  private BottomSheetBehavior<View> mBehavior;

  @Override
  public void setupDialog(Dialog dialog, int style) {
    super.setupDialog(dialog, style);
    View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
    dialog.setContentView(contentView);

    ViewParent parent = contentView.getParent();
    CoordinatorLayout.LayoutParams params =
        (CoordinatorLayout.LayoutParams) ((View) parent).getLayoutParams();
    CoordinatorLayout.Behavior behavior = params.getBehavior();

    if (behavior != null && behavior instanceof BottomSheetBehavior) {
      ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
    }
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
  }

  public void showList() {

    ImageRecyclerViewAdapter simpleSectionedListAdapter =
        new ImageRecyclerViewAdapter();
    mRecycleView.setAdapter(simpleSectionedListAdapter);

    PaginationTool.Builder imageBuilder = PaginationTool.buildPagingObservable(
        mRecycleView, this::getSource).setRetryCount(3);

    PaginationTool build = imageBuilder.build();
    build.getPagingObservable().subscribe();
  }

  Observable getSource(int skip) {
    return Observable.merge(
        MediaProviderHelper.getVideoAll(getActivity(), null, null, null, null).cast(Object.class),
        MediaProviderHelper.getImagesAll(getActivity(), null, null, null, null).cast(Object.class)
    ).distinct().skip(skip);
  }

  @android.support.annotation.NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

    View view = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);

    dialog.setContentView(view);
    mBehavior = BottomSheetBehavior.from((View) view.getParent());
    return dialog;
  }

  @Override
  public void onStart() {
    super.onStart();
    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
  }
}