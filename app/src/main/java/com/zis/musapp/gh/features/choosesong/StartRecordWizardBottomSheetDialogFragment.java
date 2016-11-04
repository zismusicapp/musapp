package com.zis.musapp.gh.features.choosesong;

import android.app.Dialog;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import com.android.annotations.NonNull;
import com.zis.musapp.gh.R;

public class StartRecordWizardBottomSheetDialogFragment extends BaseBottomSheetDialogFragment {

  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
      new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            dismiss();
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
      };

  @Override
  public void setupDialog(Dialog dialog, int style) {
    super.setupDialog(dialog, style);
    View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
    dialog.setContentView(contentView);

    CoordinatorLayout.LayoutParams params =
        (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
    CoordinatorLayout.Behavior behavior = params.getBehavior();

    if (behavior != null && behavior instanceof BottomSheetBehavior) {
      ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
    }
  }

  @Override protected int getLayoutRes() {
    return R.layout.fragment_bottom_sheet;
  }

  @Override protected int getWidth() {
    return 300;
  }

  @Override protected int getHeight() {
    return 300;
  }

  @Override public boolean isCommitterResumed() {
    return isResumed();
  }
}