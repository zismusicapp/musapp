package com.zis.musapp.gh.features.choosesong;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.annotations.NonNull;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.choosesong.EffectAdapter.Type;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class FiltersBottomSheetFragment extends BottomSheetDialogFragment
    implements EffectAdapter.IEffectListener {

  @BindView(R.id.list) RecyclerView recyclerView;

  private BottomSheetBehavior<View> mBehavior;

  BehaviorSubject<Type> onEffect = BehaviorSubject.create();

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

    View view = View.inflate(getContext(), R.layout.fragment_filters_bottom_sheet, null);
    dialog.setContentView(view);
    ButterKnife.bind(this, view);

    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    List<Type> dataSet = new ArrayList<>();
    dataSet.add(Type.Blur);
    dataSet.add(Type.Grayscale);
    dataSet.add(Type.ColorFilter);
    dataSet.add(Type.Mask);
    dataSet.add(Type.NinePatchMask);
    dataSet.add(Type.Pixel);
    dataSet.add(Type.Vignette);
    //dataSet.add(Type.Kuawahara);
    dataSet.add(Type.Brightness);
    dataSet.add(Type.Swirl);
    dataSet.add(Type.Sketch);
    dataSet.add(Type.Invert);
    dataSet.add(Type.Contrast);
    dataSet.add(Type.Sepia);
    dataSet.add(Type.Toon);

    EffectAdapter adapter = new EffectAdapter(getActivity(), dataSet);
    recyclerView.setAdapter(adapter);
    adapter.setOnItemClickListener(this);

    mBehavior = BottomSheetBehavior.from((View) view.getParent());
    mBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);

    mBehavior.setPeekHeight(320);

    return dialog;
  }

  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
      new BottomSheetBehavior.BottomSheetCallback() {

        @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {

          switch (newState) {
            case BottomSheetBehavior.STATE_HIDDEN:
              dismiss();
              break;
            case BottomSheetBehavior.STATE_EXPANDED:
              break;
            case BottomSheetBehavior.STATE_DRAGGING:
             // mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
              break;
            case BottomSheetBehavior.STATE_SETTLING:
              break;
            case BottomSheetBehavior.STATE_COLLAPSED:
              break;
          }
        }

        @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
      };

  @Override public void onEffect(Type type) {
    onEffect.onNext(type);
  }

  public Observable<Type> getOnEffectObservable() {
    return onEffect.asObservable();
  }
}
