package br.ufc.great.contextplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.model.Song;
import br.ufc.great.contextplayer.views.SongView;

class SongViewRecyclerAdapter extends RecyclerView.Adapter<SongViewRecyclerAdapter.ViewHolder> {

    List<Song> songs;
    public SongViewRecyclerAdapter(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_list_element, viewGroup, false);
        ViewHolder vh = new ViewHolder((SongView) v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song s = songs.get(i);
        SongView sv = viewHolder.getSongView();
        sv.setSong(s);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull SongView itemView) {
            super(itemView);
        }
        public SongView getSongView(){
            return (SongView) itemView;
        }
    }
}
