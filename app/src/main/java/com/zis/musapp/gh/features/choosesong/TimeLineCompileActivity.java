package com.zis.musapp.gh.features.choosesong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.joanzapata.iconify.widget.IconButton;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.editVideo.TrimmerActivity;
import com.zis.musapp.gh.features.editVideo.TrimmerActivityAutoBundle;
import com.zis.musapp.gh.model.clip.Clip;
import com.zis.musapp.gh.model.clip.Part;
import com.zis.musapp.gh.model.mediastore.MediaColumns;
import com.zis.musapp.gh.model.mediastore.image.Image;
import com.zis.musapp.gh.model.mediastore.video.Video;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Observable;
import rx_activity_result.RxActivityResult;

public class TimeLineCompileActivity extends BaseActivity {

  public static final String BUNDLE = "bundle";
  public static final String EXTRA = "extra";

  @BindView(R.id.sourcesList) RecyclerView sourcesList;
  @BindView(R.id.preview) IconButton preview;
  private ImageRecyclerViewAdapter mAdapter;
  private List<MediaColumns> mediaColumnses;

  @Override protected boolean hasArgs() {
    return true;
  }

  @Override protected void initializeInjector() {

  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ArrayList<Parcelable> parcelableArrayList =
        getIntent().getBundleExtra(EXTRA).getParcelableArrayList(BUNDLE);

    mediaColumnses = getMediaColumnses(parcelableArrayList);

    setContentView(R.layout.time_line_compile);

    mAdapter = new ImageRecyclerViewAdapter(this, mediaColumnses);

    RxView.clicks(preview).retry().subscribe(aVoid -> {

      Observable.from(mediaColumnses).scan(new Clip(), (clip, mediaColumns) -> {
        Part part = new Part();
        part.media = mediaColumns;
        clip.partLinkedList.add(part);
        return clip;
      }).subscribe(clip -> {
        Intent intent = PreviewLocalActivityAutoBundle.createIntentBuilder(clip)
            .build(TimeLineCompileActivity.this);
        startActivity(intent);
      }, RxUtil.ON_ERROR_LOGGER);
    });

    mAdapter.setEditListener(new IEditListener() {
      @Override public void onEditPhoto(Image image) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;

        Intent imageIntent =
            CropFilterImageActivityAutoBundle.createIntentBuilder(image.getContentUri(),
                cropImageOptions).build(TimeLineCompileActivity.this);

        RxActivityResult.on(TimeLineCompileActivity.this)
            .startIntent(imageIntent)
            .subscribe(result -> {
              Intent data = result.data();
              int resultCode = result.resultCode();
              if (resultCode == RESULT_OK) {

              } else {

              }
            });

        // startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
      }

      @Override public void onEditVideo(Video video) {

        File cacheDir = getCacheDir();

        File file = new File(cacheDir, "temp.mp4");
        file.mkdirs();

        Intent videoIntent = TrimmerActivityAutoBundle.createIntentBuilder(video.getContentUri(),
            file.getAbsolutePath()).build(TimeLineCompileActivity.this);
        RxActivityResult.on(TimeLineCompileActivity.this)
            .startIntent(videoIntent)
            .subscribe(result -> {
              Intent data = result.data();
              int resultCode = result.resultCode();
              if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra(TrimmerActivity.DATA);
                //result.targetUI().showImage(data);
              } else {
                //result.targetUI().printUserCanceled();
              }
            });
      }
    });

    sourcesList.setLayoutManager(new GridLayoutManager(this, 3));
    sourcesList.setAdapter(mAdapter);

    addDragAndDrop();
  }

  private void addDragAndDrop() {

    // Extend the Callback class
    ItemTouchHelper.Callback ithCallback = new ItemTouchHelper.Callback() {
      //and in your imlpementaion of
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
          RecyclerView.ViewHolder target) {
        // get the viewHolder's and target's positions in your adapter data, swap them
        Collections.swap(mediaColumnses/*RecyclerView.Adapter's data collection*/,
            viewHolder.getAdapterPosition(), target.getAdapterPosition());
        // and notify the adapter that its dataset has changed
        mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
      }

      @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //TODO
      }

      //defines the enabled move directions in each state (idle, swiping, dragging).
      @Override public int getMovementFlags(RecyclerView recyclerView,
          RecyclerView.ViewHolder viewHolder) {
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN
            | ItemTouchHelper.UP
            | ItemTouchHelper.START
            | ItemTouchHelper.END);
      }
    };

    // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
    ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
    ith.attachToRecyclerView(sourcesList);
  }

  private List<MediaColumns> getMediaColumnses(ArrayList<Parcelable> parcelableArrayList) {
    return Observable.from(parcelableArrayList)
        .cast(MediaColumns.class)
        .toList()
        .toBlocking()
        .firstOrDefault(new ArrayList<>());
  }
}
