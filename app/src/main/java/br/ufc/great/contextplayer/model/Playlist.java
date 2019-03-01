package br.ufc.great.contextplayer.model;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


import smd.ufc.br.easycontext.ContextDefinition;

public class Playlist {
    private List<Song> songs;
    private String name;

    public List<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }

    public ContextDefinition[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ContextDefinition ...definitions) {
        this.definitions = definitions;
    }

    private ContextDefinition[] definitions;
    private long id;

    private static String TAG = "Playlist";

//    @Deprecated
//    public Playlist(Context context, String name){
//        this.context = context;
//        this.name = name;
//        id = getPlaylistId(context, name);
//        songs = new ArrayList<>();
//        Cursor allSongs = getPlaylistTracks(context, id);
//        for(allSongs.moveToFirst(); !allSongs.isAfterLast(); allSongs.moveToNext()){
//            long songId = allSongs.getInt(allSongs.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
//            Song song = Song.fromAudioId(context, songId);
//            if(song != null)
//                songs.add(song);
//        }
//    }

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
