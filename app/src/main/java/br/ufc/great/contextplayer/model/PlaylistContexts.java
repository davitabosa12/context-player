package br.ufc.great.contextplayer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class PlaylistContexts {
    @PrimaryKey
    int uid;

    @ColumnInfo(name = "definitions")
    private List<ContextDefinition> definitions;

    @ColumnInfo(name = "playlist_id")
    long playlistId;

    public PlaylistContexts() {
    }

    public long getPlaylistId() {
        return playlistId;
    }


    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public PlaylistContexts setDefinitions(List<ContextDefinition> definitions) {
        this.definitions = definitions;
        return this;
    }

    public List<ContextDefinition> getDefinitions() {

        return definitions;
    }

    public float calculateConfidence(CurrentContext currentContext) {
        for(ContextDefinition definition : definitions){
            definition.calculateConfidence(currentContext);
        }
        return 0;
    }
}
