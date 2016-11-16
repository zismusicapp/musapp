package com.zis.musapp.gh.features.choosesong;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.image.Image;
import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerViewAdapter
    extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ThumbnailViewHolder> {

  private List<Image> images = new ArrayList<>();

  public ImageRecyclerViewAdapter(List<Image> images) {
    this.images = images;
  }

  @Override public ThumbnailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
    return new ThumbnailViewHolder(view);
  }

  @Override public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
    Image image1 = images.get(position);
    holder.imageView.setImageURI(image1.getContentUri());
  }

  @Override public int getItemCount() {
    return images.size();
  }

  public void addImages(List<Image> image) {
    images.addAll(image);
  }

  class ThumbnailViewHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.imageView) SimpleDraweeView imageView;

    public ThumbnailViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
