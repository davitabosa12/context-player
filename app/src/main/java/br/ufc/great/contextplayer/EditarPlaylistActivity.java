package br.ufc.great.contextplayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.Song;

public class EditarPlaylistActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<Song> allSongs;
    private List<Song> selectedSongs;
    private Button btnUpdatePlaylist;
    private static String TAG = "EditarPlaylistActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_playlist);
        mListView = findViewById(R.id.lv_songlist);
        allSongs = new MusicScanner(this).scan();
        selectedSongs = new ArrayList<>();
        mListView.setOnItemClickListener(this);

        btnUpdatePlaylist = findViewById(R.id.btn_update_playlist);
        btnUpdatePlaylist.setOnClickListener(this);


        ArrayAdapter adapter = new ArrayAdapter<Song>(this,
                android.R.layout.simple_list_item_multiple_choice,
                allSongs.toArray(new Song[allSongs.size()]) );
        mListView.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_update_playlist:
                savePlaylist();
                break;
        }
    }

    private void savePlaylist() {
        ContentResolver resolver = getContentResolver();
        Intent i = getIntent();
        String playlist = i.getStringExtra("playlist");
        Uri uri = getUriFromString(playlist);
        Log.d(TAG, "savePlaylist: uri=" + uri);
        int songOrder = 0;
        for(Song s : selectedSongs){

            Uri x = resolver.insert(uri, s.asContentValues(songOrder++));
            if(x==null)
            {
                Log.v(TAG,"unsuccess");
            }
            else
            {
                Log.v(TAG,"success");
            }
            Log.d(TAG, "savePlaylist: " + s.getTitle() + " added to playlist!");
        }

    }

    private Uri getUriFromString(String playlist) {
        int playlistId = Playlist.getPlaylistId(this, playlist);
        return MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
    }

    private void updateSelectedMusic() {
        selectedSongs.clear();
        SparseBooleanArray booleanArray = mListView.getCheckedItemPositions();
        for(int i = 0; i < booleanArray.size(); i++){
            int position = booleanArray.keyAt(i);
            if(booleanArray.valueAt(position)){
                selectedSongs.add(allSongs.get(position));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        updateSelectedMusic();

    }
}
