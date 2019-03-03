package br.ufc.great.contextplayer.model;


import android.support.annotation.NonNull;

public class PlaylistConfidence implements Comparable<PlaylistConfidence>{
    private float confidence = 0.0f;
    private Playlist playlist;

    public PlaylistConfidence(float confidence, Playlist playlist) {
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
