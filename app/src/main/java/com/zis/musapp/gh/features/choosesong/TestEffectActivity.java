package com.zis.musapp.gh.features.choosesong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.choosesong.EffectAdapter.Type;
import java.util.ArrayList;
import java.util.List;

public class TestEffectActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_effects_main);

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    List<Type> dataSet = new ArrayList<>();
    dataSet.add(Type.Blur);
    dataSet.add(Type.Grayscale);
    dataSet.add(Type.ColorFilter);
    dataSet.add(Type.Mask);
    dataSet.add(Type.NinePatchMask);
    dataSet.add(Type.Pixel);
    dataSet.add(Type.Vignette);
    dataSet.add(Type.Kuawahara);
    dataSet.add(Type.Brightness);
    dataSet.add(Type.Swirl);
    dataSet.add(Type.Sketch);
    dataSet.add(Type.Invert);
    dataSet.add(Type.Contrast);
    dataSet.add(Type.Sepia);
    dataSet.add(Type.Toon);

    recyclerView.setAdapter(new EffectAdapter(this, dataSet));
  }
}
