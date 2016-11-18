package com.zis.musapp.gh.features.choosesong;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joanzapata.iconify.widget.IconTextView;
import com.zis.musapp.base.utils.FileUtils;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import com.zis.musapp.gh.model.mediastore.MediaProviderHelper;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.image.thumbnails.ImageThumbnailsColumns;
import com.zis.musapp.gh.model.mediastore.video.Video;
import com.zis.musapp.gh.model.mediastore.video.thumbnails.VideoThumbnails;
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
    if (media instanceof Image) {
      Image image = (Image) media;
      boolean hasMagic = image.miniThumbMagic() != null && !"0".equals(image.miniThumbMagic());
      String defaultValue = image.getContentUri().toString();
      Observable<String> pathObservable =
          hasMagic ? MediaProviderHelper.getImageThumbnails(context, null, BaseColumns._ID + " = ?",
              new String[] { image.miniThumbMagic() }, null)
              .map(ImageThumbnailsColumns::data)
              .defaultIfEmpty(defaultValue) : Observable.just(defaultValue);

      pathObservable.compose(RxUtil.applyIOToMainThreadSchedulers()).subscribe(path -> {
        holder.imageView.setImageURI(path);
        holder.typeIcon.setText("{fa-camera}");
        holder.infoText.setText(image.width() + "x" + image.height());
      }, RxUtil.ON_ERROR_LOGGER);
      //holder.imageView.setImageURI(image.getContentUri());

    } else if (media instanceof Video) {
      Video video = (Video) media;
      boolean hasMagic = video.miniThumbMagic() != null && !"0".equals(video.miniThumbMagic());
      Observable<Uri> pathObservable =
          hasMagic ? MediaProviderHelper.getVideoThumbnails(context, null, BaseColumns._ID + " = ?",
              new String[] { video.miniThumbMagic() }, null)
              .map(VideoThumbnails::data)
              .map(path -> Uri.fromFile(new File(path)))
              .switchIfEmpty(prepareThumbnail(video)) : prepareThumbnail(video);

      pathObservable.compose(RxUtil.applyIOToMainThreadSchedulers()).subscribe(path -> {
        holder.imageView.setImageURI(path);
        holder.typeIcon.setText("{fa-video-camera}");
        if (video != null) {
          holder.infoText.setText(millisToString(video.duration()));
        }
      }, RxUtil.ON_ERROR_LOGGER);
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

  public void addMedias(List<MediaColumns> image) {
    medias.addAll(image);
    Collections.sort(medias, (o1, o2) -> (int) (o1.dateAdded() - o2.dateAdded()));
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
