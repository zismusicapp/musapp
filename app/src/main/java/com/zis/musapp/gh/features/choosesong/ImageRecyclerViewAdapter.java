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
import com.zis.musapp.gh.model.mediastore.video.Video;
import java.util.ArrayList;

public class ImageRecyclerViewAdapter
    extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ThumbnailViewHolder> {

  ArrayList dataList = new ArrayList<>();

  public ImageRecyclerViewAdapter() {
  }

  public void addData(Object object) {
    dataList.add(object);
    notifyDataSetChanged();
  }

  public void addData(ArrayList object) {
    dataList.addAll(object);
    notifyDataSetChanged();
  }

  @Override public ThumbnailViewHolder onCreateViewHolder(ViewGroup viewGroup,
      int viewType) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
    return new ThumbnailViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
    if (dataList.get(position) instanceof Image) {
      Image image = (Image) dataList.get(position);
      holder.imageView.setImageURI(image.getContentUri());
    } else if (dataList.get(position) instanceof Video) {
      Video video = (Video) dataList.get(position);
      holder.imageView.setImageURI(video.getContentUri());
    }
  }

  @Override public int getItemCount() {
    return dataList.size();
  }

  class ThumbnailViewHolder extends RecyclerView.ViewHolder {

    public @BindView(R.id.imageView) SimpleDraweeView imageView;

    public ThumbnailViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
