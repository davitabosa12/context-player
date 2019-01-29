package br.ufc.great.contextplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.model.Song;

public class MusicScanner {
    private static final String TAG = "MusicScanner";
    private Context context;
    ContentResolver cr;
    Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Uri internalUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
    String selection = MediaStore.Audio.Media.IS_MUSIC;
    String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;


    public MusicScanner(Context context) {
        this.context = context;
        cr = context.getContentResolver();



    }

    public List<Song> scan(){
        ArrayList<Song> songList = new ArrayList<>();
        Cursor cursorExternal, cursorInternal;
        Log.d(TAG, "scan: started");
        Log.d(TAG, "scan: scanning external");
        cursorExternal = cr.query(externalUri, null, selection, null, sortOrder);
        Log.d(TAG, "scan: scanning internal");
        cursorInternal = cr.query(internalUri, null, selection, null, sortOrder);
        while(!cursorExternal.isAfterLast()){
            Song song = Song.fromCursor(cursorExternal);
            Log.d(TAG, song.toString());
            songList.add(song);
        }
        while(!cursorInternal.isAfterLast()){
            Song song = Song.fromCursor(cursorExternal);
            Log.d(TAG, song.toString());
            songList.add(song);
        }
        return songList;
    }
}
