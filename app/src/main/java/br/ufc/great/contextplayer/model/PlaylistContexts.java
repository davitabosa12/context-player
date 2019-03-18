package br.ufc.great.contextplayer.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.math.FloatStatistics;

/**
 * A packet class to calculate the context confidence of the playlist
 */
public class PlaylistContexts implements Serializable {

    private List<ContextDefinition> definitions;


    long playlistId;

    public PlaylistContexts() {
        definitions = new ArrayList<>();
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
        if (definitions == null) {
            return 0;
        }
        FloatStatistics statistics = new FloatStatistics();
        for(ContextDefinition definition : definitions){
            if(definition== null) continue;
            statistics.accept(definition.calculateConfidence(currentContext));
        }
        return statistics.getAverage();
    }
}
