package br.ufc.great.contextplayer.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.ufc.great.contextplayer.model.Song;

public class SongViewRecyclerAdapter extends RecyclerView.Adapter<SongViewRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SongViewRecyclerAdapter";
    private ArrayList<Song> songList = new ArrayList<>();
    private View.OnClickListener mClickListener = null;

    public SongViewRecyclerAdapter(ArrayList<Song> songList) {
        this.songList.addAll(songList);
        Log.d(TAG, "SongViewRecyclerAdapter: created, " + this.songList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SongView songView = new SongView(viewGroup.getContext());
        songView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Log.d(TAG, "onCreateViewHolder: created");
        return new ViewHolder(songView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: bound");
        final Song s = songList.get(position);
        viewHolder.getCustomView().setSong(s);
        viewHolder.getCustomView().setOnClickListener(mClickListener);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.mClickListener = listener;
    }
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + songList.size());
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private SongView mCustomView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCustomView = (SongView) itemView;
        }

        public SongView getCustomView() {
            return mCustomView;
        }
    }
}
