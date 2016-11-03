package com.zis.musapp.gh.features.choosesong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dd.CircularProgressButton;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseFragment;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.songRecord.CameraCaptureActivity;
import com.zis.musapp.gh.model.songs.Song;
import com.zis.musapp.gh.pagination.utils.pagination.PaginationTool;
import java.util.ArrayList;
import rx.parse.ParseObservable;

public class SongsListFragment extends BaseFragment {

  @AutoBundleField
  long position;

  @Override protected boolean hasArgs() {
    return true;
  }

  public static Fragment newInstance(int position) {
    return SongsListFragmentAutoBundle.createFragmentBuilder(position)
        .build();
  }

  @Override protected int getLayoutRes() {
    return R.layout.item_vp_list;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(
            getContext(), LinearLayoutManager.VERTICAL, false
        )
    );

    SongAdapter adapter = new SongAdapter();
    recyclerView.setAdapter(adapter);

    PaginationTool<Song> songPaginationTool =
        PaginationTool.buildPagingObservable(recyclerView, offset -> {
          ParseQuery<Song> songParseQuery = ParseQuery.getQuery(Song.class)
              .setSkip(offset);
          return ParseObservable.find(songParseQuery);
        })
            .setRetryCount(3).build();

    songPaginationTool.getPagingObservable()
        .subscribe(adapter::addSong, RxUtil.ON_ERROR_LOGGER);
  }

  @Override protected void startBusiness() {
    super.startBusiness();
  }

  public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    ArrayList<Song> songs = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
      final View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.txt.setText(String.format("Navigation Item #%d", position));
    }

    @Override
    public int getItemCount() {
      return 20;
    }

    public void addSong(Song song) {
      songs.add(song);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      public TextView txt;

      public ViewHolder(final View itemView) {
        super(itemView);
        txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
        CircularProgressButton circularProgressButton =
            (CircularProgressButton) itemView.findViewById(R.id.circularButton1);

        listenOnClickRxy(circularProgressButton, aVoid -> {
          Song song = songs.get(getAdapterPosition());
          ParseFile audioFile = song.getParseFile("audio");
          ParseObservable.getFile(audioFile, circularProgressButton::setProgress)
              .compose(RxUtil.applyIOToMainThreadSchedulers())
              .subscribe(file -> {
                if (!getActivity().isFinishing()) {
                  startActivity(new Intent(getActivity(), CameraCaptureActivity.class));
                }
              }, RxUtil.ON_ERROR_LOGGER);
        });

        //Intent intent = MyVideoActivity.newIntent(getActivity(),
        //    "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8",
        //    "bipbop basic 400x300 @ 232 kbps");
        //startActivity(intent);
      }
    }
  }
}
