package com.zis.musapp.gh.features.choosesong;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.zis.musapp.gh.R;
import java.util.List;
import jp.wasabeef.fresco.processors.BlurPostprocessor;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;
import jp.wasabeef.fresco.processors.GrayscalePostprocessor;
import jp.wasabeef.fresco.processors.MaskPostprocessor;
import jp.wasabeef.fresco.processors.gpu.BrightnessFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.ContrastFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.InvertFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.KuawaharaFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.PixelationFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.SepiaFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.SketchFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.SwirlFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.ToonFilterPostprocessor;
import jp.wasabeef.fresco.processors.gpu.VignetteFilterPostprocessor;

public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.ViewHolder> {

  private Context context;
  private List<Type> dataSet;
  private IEffectListener filtersBottomSheetFragment;

  public void setOnItemClickListener(IEffectListener filtersBottomSheetFragment) {
    this.filtersBottomSheetFragment = filtersBottomSheetFragment;
  }

  enum Type {
    Mask,
    NinePatchMask,
    ColorFilter,
    Grayscale,
    Blur,
    Toon,
    Sepia,
    Contrast,
    Invert,
    Pixel,
    Sketch,
    Swirl,
    Brightness,
    Kuawahara,
    Vignette
  }

  public EffectAdapter(Context context, List<Type> dataSet) {
    this.context = context;
    this.dataSet = dataSet;
  }

  @Override public EffectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false);
    return new ViewHolder(v);
  }

  @Override public void onBindViewHolder(EffectAdapter.ViewHolder holder, int position) {
    Context context = holder.itemView.getContext();
    Postprocessor processor = null;

    holder.drawee.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);

    switch (dataSet.get(position)) {
      case Mask: {
        processor = new MaskPostprocessor(context, R.drawable.mask_starfish);
        holder.drawee.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        break;
      }
      case NinePatchMask: {
        processor = new MaskPostprocessor(context, R.drawable.mask_chat_right);
        holder.drawee.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        break;
      }
      case ColorFilter:
        processor = new ColorFilterPostprocessor(Color.argb(80, 255, 0, 0));
        break;
      case Grayscale:
        processor = new GrayscalePostprocessor();
        break;
      case Blur:
        processor = new BlurPostprocessor(context, 25);
        break;
      case Toon:
        processor = new ToonFilterPostprocessor(context);
        break;
      case Sepia:
        processor = new SepiaFilterPostprocessor(context);
        break;
      case Contrast:
        processor = new ContrastFilterPostprocessor(context, 2.0f);
        break;
      case Invert:
        processor = new InvertFilterPostprocessor(context);
        break;
      case Pixel:
        processor = new PixelationFilterPostprocessor(context, 30f);
        break;
      case Sketch:
        processor = new SketchFilterPostprocessor(context);
        break;
      case Swirl:
        processor = new SwirlFilterPostprocessor(context, 0.5f, 1.0f, new PointF(0.5f, 0.5f));
        break;
      case Brightness:
        processor = new BrightnessFilterPostprocessor(context, 0.5f);
        break;
      case Kuawahara:
        processor = new KuawaharaFilterPostprocessor(context, 25);
        break;
      case Vignette:
        processor = new VignetteFilterPostprocessor(context, new PointF(0.5f, 0.5f),
            new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f);
        break;
    }
    ImageRequest request = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.demo)
        .setPostprocessor(processor)
        .build();

    PipelineDraweeController controller =
        (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(holder.drawee.getController())
            .build();
    holder.drawee.setController(controller);
    holder.title.setText(dataSet.get(position).name());
  }

  @Override public int getItemCount() {
    return dataSet.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView drawee;
    public TextView title;

    ViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(v -> {
        if (filtersBottomSheetFragment != null) {
          filtersBottomSheetFragment.onEffect(dataSet.get(getAdapterPosition()));
        } else {
          Log.w("EffectAdapter", "listener does not set");
        }
      });
      drawee = (SimpleDraweeView) itemView.findViewById(R.id.image);
      title = (TextView) itemView.findViewById(R.id.title);
    }
  }

  public interface IEffectListener {
    void onEffect(Type type);
  }
}
