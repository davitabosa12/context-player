package br.ufc.great.contextplayer.model;

import android.arch.persistence.room.Room;
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

import br.ufc.great.contextplayer.database.ApplicationDb;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;
import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.persistance.databases.EasyContextDatabase;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

public class PlaylistDAO {

    private Context context;
    private ContentResolver resolver;
    private PlaylistContextsDAO definitionsDAO;
    private PlaylistContextJoinDAO joinDAO;
    private EasyContextDatabase contextDatabase;
    private static final String TAG = "PlaylistDAO";
    private static final String[] SELECTION_PLAYLIST = new String[] {MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME};


    public PlaylistDAO(@NonNull Context context) {
        this.context = context;
        resolver = context.getContentResolver();
        definitionsDAO = new PlaylistContextsDAO(context);
        joinDAO = ApplicationDb.getInstance(context).playlistContextJoinDAO();
        contextDatabase = EasyContextDatabase.getInstance(context, ApplicationDb.DB_NAME);


    }

    public boolean addPlaylist(Playlist playlist){
        long playlistId = createBlankPlaylist(playlist.getName());

        //save information about definitions in Easycontext
        PlaylistContextJoin join = new PlaylistContextJoin();
        join.setPlaylistId(playlistId);

        for (ContextDefinition d :playlist.getDefinitions().getDefinitions()){
            if(d instanceof TimeIntervalDefinition){
                long ctxId = contextDatabase.timeIntervalDAO().insert((TimeIntervalDefinition) d);
                join.setTimeIntervalId(ctxId);
            } else if(d instanceof WeatherDefinition){
                long id = contextDatabase.weatherDefinitionDAO().insert((WeatherDefinition) d);
                join.setWeatherId(id);
            } else if(d instanceof LocationDefinition){
                //TODO: Implement this
                Log.d(TAG, "Tried to save to Location");
            } else if(d instanceof DetectedActivityDefinition){

                long id = contextDatabase.detectedActivityDAO().insert((DetectedActivityDefinition) d);
                join.setActivityId(id);
            }
        }
        //save information about PlaylistContexts definitions
        long joinId = joinDAO.insert(join);


        //save playlist in MediaStore
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
            if (inserted == null) {
                Log.e(TAG, "addPlaylist: failed inserting song "+ song.getTitle(), new Exception("yikes!"));
            } else{

                Log.d(TAG, "build: inserted song " + song.getTitle() + " with URI " + inserted.toString());
            }


        }

        //TODO: return result of task
        return true;
    }


    public Playlist getPlaylist(long playlistId){
        Playlist p = new Playlist();

        Uri memberUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        Uri allPlaylistsUri = MediaStore.Audio.Playlists.getContentUri("external");

        //query for getting the name..
        Cursor allPlaylists = resolver.query(allPlaylistsUri, new String[] {"*"}, null, null, null);

        for(allPlaylists.moveToFirst(); !allPlaylists.isAfterLast(); allPlaylists.moveToNext()){
            long idFound = allPlaylists.getLong(allPlaylists.getColumnIndex(MediaStore.Audio.Playlists._ID));
            if(idFound == playlistId){
                p.setName(allPlaylists.getString(allPlaylists.getColumnIndex(MediaStore.Audio.Playlists.NAME)));
                p.setId(playlistId);
                break;
            }
        }

        Cursor allSongs = resolver.query(memberUri, null, null, null, null);
        if (allSongs == null) {
            return null;
        }


        for(allSongs.moveToFirst(); !allSongs.isAfterLast(); allSongs.moveToNext()){
            long songId = allSongs.getInt(allSongs.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID));
            Song song = Song.fromAudioId(context, songId);
            if(song != null)
                p.addSong(song);
        }
        allSongs.close();
        allPlaylists.close();
        //get definitions data from Room persistance

        PlaylistContexts contexts = definitionsDAO.getByPlaylistId(playlistId);
        p.setDefinitions(contexts);
        return p;


    }

    public boolean deletePlaylist(long playlistId){
        Uri uri = MediaStore.Audio.Playlists.getContentUri("external");
        String[] selection = {MediaStore.Audio.Playlists._ID};

        String where = playlistId + " = " + MediaStore.Audio.Playlists._ID;
        int result = resolver.delete(uri, where, null);
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
        int result1 = resolver.update(playlistsUri, playlistValues, where, null);

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

        //third: update context definitions into ApplicationDb

        definitionsDAO.update(playlistId, newPlaylist.getDefinitions());
        return (result1 > 0) && !problems; //true if name has changed and no problems inserting songs.
    }


    /**
     * Utility to find an ID number from playlist name
     * @param playlistName name of the playlist
     * @return the id of the first playlist found
     */
    public long getPlaylistId(String playlistName){
        ContentResolver resolver = context.getContentResolver();
        Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor c = resolver.query(playlists, new String[] { "*" }, null, null, null);

        if (c == null) {

            return -1;
        }

        if(c.getCount() <=0){
            c.close();
            return -1;
        }
        c.moveToFirst();
        do {
            String currentPlaylistName = c.getString(c.getColumnIndex(MediaStore.Audio.Playlists.NAME));
            if(playlistName.equalsIgnoreCase(currentPlaylistName)){
                int id = c.getInt(c.getColumnIndex(MediaStore.Audio.Playlists._ID));
                c.close();
                return id;

            }
        } while (c.moveToNext());
        c.close();
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
        if(createdPlaylistUri == null){
            return -1;
        }

        Cursor created = resolver.query(createdPlaylistUri, PROJECTION_PLAYLIST, null, null ,null );

        if (created == null) {
            return -1;
        }

        created.moveToFirst();
        int id = created.getInt(created.getColumnIndex(MediaStore.Audio.Playlists._ID));
        Log.d(TAG, "createBlankPlaylist: created playlist \"" + playlistName + "\" with id " + id);
        created.close();
        return id;

    }
    private static final String[] PROJECTION_PLAYLIST = new String[] {
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
        String display_name = MediaStore.Audio.Media.DISPLAY_NAME;
        String date_added = MediaStore.Audio.Media.DATE_ADDED;
        String size = MediaStore.Audio.Media.SIZE;
        String track = MediaStore.Audio.Media.TRACK;
        String year = MediaStore.Audio.Media.YEAR;
        String date_modified = MediaStore.Audio.Playlists.Members.DATE_MODIFIED;

        String[] columns = {_id, audio_id, artist, album_id, album, title, duration,
                location, date_modified, date_added, size, display_name, track, year, playorder, composer};
        return resolver.query(newuri, columns, null, null, null);
    }

    public List<Playlist> getAllPlaylists(){
        ContentResolver resolver = context.getContentResolver();
        Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(playlistUri, PROJECTION_PLAYLIST, null, null, null);
        List<Playlist> allPlaylists = new ArrayList<>();

        if (cursor == null) {
            return null;
        }
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Playlist p = new Playlist();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));
            Cursor songList = getPlaylistTracks(id);
            for(songList.moveToFirst(); !songList.isAfterLast(); songList.moveToNext()){
                p.addSong(Song.fromCursor(songList));
            }
            p.setId(id);
            p.setName(name);
            allPlaylists.add(p);
            ApplicationDb db = ApplicationDb.getInstance(context);
            PlaylistContexts ctxs = definitionsDAO.getByPlaylistId(p.getId());
            p.setDefinitions(ctxs);

        }
        cursor.close();
        return allPlaylists;
    }
}
