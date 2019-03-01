package br.ufc.great.contextplayer.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    private Context context;
    private ContentResolver resolver;
    private static final String TAG = "PlaylistDAO";
    public final Uri PLAYLIST_ROOT_URI = MediaStore.Audio.Playlists.getContentUri("external");
    private static final String[] SELECTION_PLAYLIST = new String[] {MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME};


    public PlaylistDAO(@NonNull Context context) {
        this.context = context;
        resolver = context.getContentResolver();


    }

    public boolean addPlaylist(Playlist playlist){
        long playlistId = createBlankPlaylist(playlist.getName());
        ContentResolver resolver = context.getContentResolver();
        Uri playlistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        int order = 0;
        //TODO: Do this in a threadpool
        for(Song song : playlist.getSongs()){




            ContentValues values = song.asContentValues(order++);
                /*values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song.getAudio_id());
                values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, order);
                values.put(MediaStore.Audio.Playlists.Members.DISPLAY_NAME, song.getDisplayName());
                values.put(MediaStore.Audio.Playlists.Members.ARTIST, song.getArtist());
                values.put(MediaStore.Audio.Playlists.Members.DATA, song.getData());*/
            Uri inserted = resolver.insert(playlistUri, values);
            Log.d(TAG, "build: inserted song " + song.getTitle() + " with URI " + inserted.toString());


        }
        //TODO: return result of task
        return true;
    }


    public Playlist getPlaylist(long playlistId){
        Playlist p = new Playlist();
        Uri playlistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        Cursor allSongs = resolver.query(playlistUri, PROJECTION_PLAYLIST, null, null, null);


            for(allSongs.moveToFirst(); !allSongs.isAfterLast(); allSongs.moveToNext()){
                long songId = allSongs.getInt(allSongs.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
                Song song = Song.fromAudioId(context, songId);
                if(song != null)
                    p.addSong(song);
            }
            return p;


    }

    public List<Playlist> getAllPlaylists(){

    }

    public boolean deletePlaylist(String playlistName){
        Uri uri = MediaStore.Audio.Playlists.getContentUri("external");
        String[] selection = {MediaStore.Audio.Playlists._ID};
        long id = getPlaylistId(playlistName);
        String where = id + " = " + MediaStore.Audio.Playlists._ID;
        int result = resolver.delete(uri, where, selection);
        return result > 0;
    }

    public boolean updatePlaylist(Playlist newPlaylist, long playlistId){
        Uri playlistsUri = MediaStore.Audio.Playlists.getContentUri("external");
        Uri membersUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        boolean problems = false; //checks problems while inserting songs
        //first: update name at playlists Uri:
        ContentValues playlistValues = new ContentValues();
        playlistValues.put(MediaStore.Audio.Playlists.NAME, newPlaylist.getName());
        String where = MediaStore.Audio.Playlists._ID + "=" + playlistId; // WHERE _id = playlistId
        int result1 = resolver.update(playlistsUri, playlistValues, where, SELECTION_PLAYLIST);

        //second: update song list at membersUri:
        int deletedRows = resolver.delete(membersUri, null, null); //clear playlist songs
            Log.d(TAG, "updatePlaylist: deleted " + deletedRows + " rows from playlist " + playlistId);
        int order = 0;

        for (Song s : newPlaylist.getSongs()) {

            ContentValues songValue = s.asContentValues(order++);
            Uri resultInsert = resolver.insert(membersUri, songValue);
            if(resultInsert == null){
                Log.d(TAG, "updatePlaylist: failure inserting song " + s.getTitle());
                problems = true;
            }
        }
        return (result1 > 0) && !problems; //true if name has changed and no problems inserting songs.
    }


    /**
     * Utility to find an ID number from playlist name
     * @param playlistName
     * @return the id of the first playlist found
     */
    public long getPlaylistId(String playlistName){
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
    /**
     * Creates a blank playlist
     * @param playlistName name of the playlist
     * @return the id of created playlist
     */
    public long createBlankPlaylist(String playlistName) {
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
    public static final String[] PROJECTION_PLAYLIST = new String[] {
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME,
            MediaStore.Audio.Playlists.DATA
    };


    private Cursor getPlaylistTracks(Long playlist_id) {
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

    public List<Playlist> getAllPlaylists(){
        ContentResolver resolver = context.getContentResolver();
        Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(playlistUri, PROJECTION_PLAYLIST, null, null, null);
        List<Playlist> allPlaylists = new ArrayList<Playlist>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Playlist p = new Playlist();
            List<Song> songs = new ArrayList<>();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));
            Cursor songList = getPlaylistTracks(id);
            for(songList.moveToFirst(); !songList.isAfterLast(); songList.moveToNext()){
                p.addSong(Song.fromCursor(songList));
            }
            p.setId(id);
            p.setName(name);


        }
    }
}
