package com.zis.musapp.gh.features.choosesong;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.joanzapata.iconify.widget.IconTextView;
import com.zis.musapp.base.utils.FileUtils;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;

public class ImageRecyclerViewAdapter
    extends SelectableAdapter<ImageRecyclerViewAdapter.ThumbnailViewHolder> {

  private Context context;
  private List<MediaColumns> medias = new ArrayList<>();

  public ImageRecyclerViewAdapter(Context context, List<MediaColumns> medias) {
    this.context = context;
    this.medias = medias;
    clearSelection();
  }

  @Override public ThumbnailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
    return new ThumbnailViewHolder(view);
  }

  @Override public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
    MediaColumns media = medias.get(position);

    //assume that we have image or video
    Uri uri =
        media instanceof Image ? ((Image) media).getContentUri() : ((Video) media).getContentUri();

    ImageRequest mainRequest = ImageRequestBuilder.newBuilderWithSource(uri)
        .setLocalThumbnailPreviewsEnabled(true)
        .setRequestPriority(Priority.HIGH)
        .setResizeOptions(new ResizeOptions(120, 120))
        .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
        .build();

    PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder =
        Fresco.newDraweeControllerBuilder()
            .setImageRequest(mainRequest)
            .setOldController(holder.imageView.getController());

    holder.imageView.setController(pipelineDraweeControllerBuilder.build());

    if (media instanceof Image) {
      Image image = (Image) media;
      holder.typeIcon.setText("{fa-camera}");
      holder.infoText.setText(image.width() + "x" + image.height());
    } else {
      Video video = (Video) media;
      holder.typeIcon.setText("{fa-video-camera}");
      holder.infoText.setText(millisToString(video.duration()));
    }

    holder.selection.setAlpha(isSelected(position) ? 1 : 0);
    holder.shadow.setAlpha(isSelected(position) ? 1 : 0);

    holder.setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        toggleSelection(position);
      }
    });
  }

  private String millisToString(long millis) {
    return String.format("%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(millis),
        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(millis)));
  }

  private Observable<Uri> prepareThumbnail(Video video) {
    return Observable.defer(() -> {
      Bitmap videoThumbnail =
          ThumbnailUtils.createVideoThumbnail(video.data(), MediaStore.Video.Thumbnails.MINI_KIND);

      String absolutePath = context.getCacheDir().getAbsolutePath() + "/" + video.id() + ".jpg";
      File file = new File(absolutePath);
      Uri uri = Uri.fromFile(file);
      if (file.exists()) {
        return Observable.just(uri);
      }
      FileUtils.saveBitmapToFile(absolutePath, videoThumbnail);

      return Observable.just(uri);
    });
  }

  @Override public int getItemCount() {
    return medias.size();
  }

  public void addMedias(MediaColumns image) {
    medias.add(image);
    //Collections.sort(medias, (o1, o2) -> (int) (o1.dateAdded() - o2.dateAdded()));
    int i = medias.indexOf(image);
    notifyItemInserted(i);
  }

  class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public @BindView(R.id.imageView) SimpleDraweeView imageView;
    public @BindView(R.id.selection) IconTextView selection;
    public @BindView(R.id.shadow) View shadow;
    public @BindView(R.id.typeIcon) IconTextView typeIcon;
    public @BindView(R.id.infoText) IconTextView infoText;
    private OnItemClickListener mItemClickListener;

    public ThumbnailViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View view) {
      if (mItemClickListener != null) {
        mItemClickListener.onItemClick(view, getAdapterPosition());
      }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
      this.mItemClickListener = mItemClickListener;
    }
  }

  public interface OnItemClickListener {
    public void onItemClick(View view, int position);
  }

  @Override public long getItemId(int position) {
    return position;
  }
}
