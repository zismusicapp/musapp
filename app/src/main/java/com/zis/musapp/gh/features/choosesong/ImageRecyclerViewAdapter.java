package com.zis.musapp.gh.features.choosesong;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joanzapata.iconify.widget.IconTextView;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.mediastore.image.Image;
import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerViewAdapter
    extends SelectableAdapter<ImageRecyclerViewAdapter.ThumbnailViewHolder> {

  private List<Image> images = new ArrayList<>();

  public ImageRecyclerViewAdapter(List<Image> images) {
    this.images = images;
    clearSelection();
  }

  @Override public ThumbnailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
    return new ThumbnailViewHolder(view);
  }

  @Override public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
    Image image = images.get(position);
    holder.imageView.setImageURI(image.getContentUri());
    holder.selection.setAlpha(isSelected(position) ? 1 : 0);
    holder.shadow.setAlpha(isSelected(position) ? 1 : 0);
    holder.setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        toggleSelection(position);
      }
    });
  }

  @Override public int getItemCount() {
    return images.size();
  }

  public void addImages(List<Image> image) {
    images.addAll(image);
  }

  class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public @BindView(R.id.imageView) SimpleDraweeView imageView;
    public @BindView(R.id.selection) IconTextView selection;
    public @BindView(R.id.shadow) View shadow;
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
