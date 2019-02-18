package br.ufc.great.contextplayer.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class Playlist {
    public List<Song> getSongs() {
        return songs;
    }

    List<Song> songs;
    Context context;
    String playlistName;
    long id;

    private static String TAG = "Playlist";

    public Playlist(Context context, String playlistName){
        this.context = context;
        this.playlistName = playlistName;
        id = getPlaylistId(context, playlistName);
        songs = new ArrayList<>();
        Cursor allSongs = getPlaylistTracks(context, id);
        for(allSongs.moveToFirst(); !allSongs.isAfterLast(); allSongs.moveToNext()){
            long songId = allSongs.getInt(allSongs.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
            Song song = Song.fromAudioId(context, songId);
            if(song != null)
                songs.add(song);
        }
    }

    public static int getPlaylistId(Context context, String playlistName){
        ContentResolver resolver = context.getContentResolver();
        Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Playlist p = null;
        Cursor c = resolver.query(playlists, new String[] { "*" }, null, null, null);
        if(c.getCount() <=0)
            return -1;
        c.moveToFirst();
        do {
            String currentPlaylistName = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
            if(playlistName.equalsIgnoreCase(currentPlaylistName)){
                int id = c.getInt(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
                return id;

            }
        } while (c.moveToNext());

        return -1;
    }
    public static long createBlankPlaylist(Context context, String playlistName) {
        ContentResolver resolver = context.getContentResolver();

        Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.NAME, playlistName);
        values.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());

        Uri createdPlaylistUri = resolver.insert(playlistUri, values);

        Cursor created = resolver.query(createdPlaylistUri, PROJECTION_PLAYLIST, null, null ,null );

        created.moveToFirst();
        int id = created.getInt(created.getColumnIndex(MediaStore.Audio.Playlists._ID));
        Log.d(TAG, "createBlankPlaylist: created playlist \"" + playlistName + "\" with id " + id);
        return id;

    }

    private Cursor getPlaylistTracks(Context context, Long playlist_id) {
        Uri newuri = MediaStore.Audio.Playlists.Members.getContentUri(
                "external", playlist_id);
        ContentResolver resolver = context.getContentResolver();
        String _id = MediaStore.Audio.Playlists.Members._ID;
        String audio_id = MediaStore.Audio.Playlists.Members.AUDIO_ID;
        String artist = MediaStore.Audio.Playlists.Members.ARTIST;
        String album = MediaStore.Audio.Playlists.Members.ALBUM;
        String album_id = MediaStore.Audio.Playlists.Members.ALBUM_ID;
        String title = MediaStore.Audio.Playlists.Members.TITLE;
        String duration = MediaStore.Audio.Playlists.Members.DURATION;
        String location = MediaStore.Audio.Playlists.Members.DATA;
        String composer = MediaStore.Audio.Playlists.Members.COMPOSER;
        String playorder = MediaStore.Audio.Playlists.Members.PLAY_ORDER;

        String date_modified = MediaStore.Audio.Playlists.Members.DATE_MODIFIED;
        String[] columns = {_id, audio_id, artist, album_id, album, title, duration,
                location, date_modified, playorder, composer};
        return resolver.query(newuri, columns, null, null, null);
    }

    public class Builder{
        private final String TAG = "Playlist.Builder";
        private List<Pair<Song, Integer>> songs;
        private Context context;

        private String playlistName;

        public Builder(Context context, String playlistName){
            this.context = context;

            songs = new ArrayList<>();
            this.playlistName = playlistName;
        }
        public Builder addSong(Song song, int order){
            songs.add(new Pair<Song, Integer>(song, order));
            return this;
        }
        public Builder removeSong(Song song){
            songs.remove(song);
            return this;
        }
        public Builder setPlaylistName(String playlistName){
            this.playlistName = playlistName;
            return this;
        }
        public Playlist build(){
            long playlistId = createBlankPlaylist(playlistName);
            ContentResolver resolver = context.getContentResolver();
            Uri playlistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
            int order = 0;
            for(Pair<Song, Integer> pair : songs){

                Song song = pair.first;


                ContentValues values = song.asContentValues(order++);
                /*values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song.getAudio_id());
                values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, order);
                values.put(MediaStore.Audio.Playlists.Members.DISPLAY_NAME, song.getDisplayName());
                values.put(MediaStore.Audio.Playlists.Members.ARTIST, song.getArtist());
                values.put(MediaStore.Audio.Playlists.Members.DATA, song.getData());*/
                Uri inserted = resolver.insert(playlistUri, values);
                Log.d(TAG, "build: inserted song " + song.getTitle() + " with URI " + inserted.toString());


            }
            return new Playlist(context, playlistName);
        }

        /**
         * Creates a playlist
         * @param playlistName name of the playlist
         * @return the id of created playlist
         */
        private long createBlankPlaylist(String playlistName) {
            ContentResolver resolver = context.getContentResolver();

            Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Playlists.NAME, playlistName);
            values.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());

            Uri createdPlaylistUri = resolver.insert(playlistUri, values);

            Cursor created = resolver.query(createdPlaylistUri, PROJECTION_PLAYLIST, null, null ,null );

            created.moveToFirst();
            int id = created.getInt(created.getColumnIndex(MediaStore.Audio.Playlists._ID));
            Log.d(TAG, "createBlankPlaylist: created playlist \"" + playlistName + "\" with id " + id);
            return id;

        }
    }
    public static final String[] PROJECTION_PLAYLIST = new String[] {
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME,
            MediaStore.Audio.Playlists.DATA
    };
}
