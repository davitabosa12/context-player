package br.ufc.great.contextplayer.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


import smd.ufc.br.easycontext.ContextDefinition;

public class Playlist {

    private List<Song> songs;
    private String name;
    private PlaylistContexts definitions;
    private long id;
    private static String TAG = "Playlist";



    public List<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }

    public PlaylistContexts getDefinitions() {
        return definitions;
    }

    public void setDefinitions(@Nullable PlaylistContexts definitions) {
        this.definitions = definitions;
    }

    public Playlist(String name, @Nullable List<Song> songList, @Nullable long id){
        this.name = name;
        if (songList == null) {
            this.songs = new ArrayList<Song>();
        } else {
            this.songs = songList;
        }
        this.id = id;
    }

    public Playlist(){
        this.name = null;
        this.songs = null;
        this.id = -1; //invalid id
    }



    public void addSong(Song song){
        if (song != null) {
            songs.add(song);
        }

    }

    public void addContextDefinition(ContextDefinition definition){
        definitions.getDefinitions().add(definition);
    }

    public boolean removeContextDefinition(ContextDefinition definition){
        return definitions.getDefinitions().remove(definition);
    }



    public boolean removeSong(Song song){
        return songs.remove(song);
    }

    public int indexOfSong(Song song){
        return songs.indexOf(song);
    }

    public Playlist setName(String name) {
        this.name = name;
        return this;
    }

    public long getId() {
        return id;
    }

    public Playlist setId(long id) {
        this.id = id;
        return this;
    }
}
