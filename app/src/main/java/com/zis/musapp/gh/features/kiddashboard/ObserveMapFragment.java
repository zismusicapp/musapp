package com.zis.musapp.gh.features.kiddashboard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zis.musapp.base.android.BaseMvpFragment;
import com.zis.musapp.gh.BootstrapApp;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.users.ZisUser;
import javax.inject.Inject;

/**
 * Created by mikhail on 26/08/16.
 */
public class ObserveMapFragment
    extends BaseMvpFragment<ObserveMapView, ObserveMapPresenter, ObserveMapComponent>
    implements ObserveMapView, OnMapReadyCallback {

  @Inject ObserveMapPresenter mObserveMapPresenter;

  private ObserveMapComponent mObserveMapComponent;
  private GoogleMap mGoogleMap;

  @Override protected void inject() {
    getComponent().inject(this);
  }

  @Override protected int getLayoutRes() {
    return R.layout.observe_map_fragment_layout;
  }

  @Override protected boolean isUseActivityComponent() {
    return false;
  }

  @Override protected ObserveMapComponent getComponent() {
    if (mObserveMapComponent == null) {
      mObserveMapComponent = BootstrapApp.get().appComponent().plus(new ObserveMapModule());
    }

    return mObserveMapComponent;
  }

  @Override protected void startBusiness() {
    super.startBusiness();

    final FragmentManager fm = getChildFragmentManager();
    SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
    if (fragment == null) {
      fragment = SupportMapFragment.newInstance();
      fm.beginTransaction().replace(R.id.map_container, fragment, "map").commit();
    }

    fragment.getMapAsync(this);
  }

  @Override public void showPositionLabel(final ZisUser ZisUser) {

    final LatLng latLng = new LatLng(37.4038143, -122.1162865);

    final View marker = LayoutInflater.from(getActivity()).inflate(R.layout.marker_layout, null);
    final TextView numTxt = (TextView) marker.findViewById(R.id.marker_label);
    numTxt.setText(ZisUser.login());

    mGoogleMap.addMarker(new MarkerOptions().position(latLng)
        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(marker))));

    final View mapView = getChildFragmentManager().findFragmentByTag("map").getView();
    if (mapView != null && mapView.getViewTreeObserver().isAlive()) {
      mapView.getViewTreeObserver()
          .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi") @Override public void onGlobalLayout() {
              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
              } else {
                mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
              }
              gotoTo(latLng, 10f);
            }
          });
    }
  }

  private void gotoTo(final LatLng latLng, final float zoom) {
    final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
    mGoogleMap.animateCamera(cameraUpdate);
  }

  // Convert a view to bitmap
  private Bitmap createDrawableFromView(final View view) {
    final DisplayMetrics displayMetrics = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    //view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
    //view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
    view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

    view.buildDrawingCache(true);

    final Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
        Bitmap.Config.ARGB_8888);

    final Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);

    return bitmap;
  }

  @Override public void onMapReady(final GoogleMap map) {
    mGoogleMap = map;

    //final ZisUser ZisUser = ZisUser.builder()
    //    .id(1l)
    //    .avatar_url("")
    //    .created_at(ZonedDateTime.now())
    //    .login("Dad")
    //    .type("Parent")
    //    .build();

    showPositionLabel(null);
  }
}
