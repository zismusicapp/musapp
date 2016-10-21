package com.zis.musapp.gh.features.tour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zis.musapp.gh.Fonts;
import com.zis.musapp.gh.R;

public class TourFragment extends Fragment {
  private static final String ARG_POSITION = "arg_pos";
  private View rootView;

  public static Fragment getInstance(int position) {
    Fragment fragment = new TourFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_POSITION, position);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    rootView = inflater.inflate(R.layout.tour_fragment, null);

    TextView titleView = (TextView) rootView.findViewById(R.id.title);
    titleView.setTypeface(Fonts.getInstance().medium());
    TextView bodyView = (TextView) rootView.findViewById(R.id.body);
    ImageView imageView = (ImageView) rootView.findViewById(R.id.image);

    Bundle args = getArguments();
    int position = args.getInt(ARG_POSITION);

    switch (position) {
      default:
      case 1:
        titleView.setText(R.string.tour_groups_title);
        bodyView.setText(R.string.tour_groups_text);
        imageView.setImageResource(R.drawable.ic_data);
        break;
      case 2:
        titleView.setText(R.string.tour_everywhere_title);
        bodyView.setText(R.string.tour_everywhere_text);
        imageView.setImageResource(R.drawable.ic_eighth);
        break;
      case 3:
        titleView.setText(R.string.tour_secure_title);
        bodyView.setText(R.string.tour_secure_text);
        imageView.setImageResource(R.drawable.ic_first);
        break;
    }

    return rootView;
  }
}
