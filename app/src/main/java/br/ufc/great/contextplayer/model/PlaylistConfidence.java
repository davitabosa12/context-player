package br.ufc.great.contextplayer.model;


import android.support.annotation.NonNull;

public class PlaylistConfidence implements Comparable<PlaylistConfidence>{
    private float confidence = 0.0f;
    private Playlist playlist;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public PlaylistConfidence(Playlist playlist, float confidence) {
        this.confidence = confidence;
        this.playlist = playlist;
    }

    @Override
    public int compareTo(@NonNull PlaylistConfidence playlistConfidence) {
        if(this.confidence < playlistConfidence.confidence){
            return -1;
        } else if(this.confidence > playlistConfidence.confidence){
            return 1;
        }
        return 0;
    }
}
