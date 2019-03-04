package br.ufc.great.contextplayer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.PlaylistDAO;
import br.ufc.great.contextplayer.model.Song;

public class EditarPlaylistActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<Song> allSongs;
    private List<Song> selectedSongs;
    private Button btnUpdatePlaylist;
    private ArrayAdapter mAdapter;
    private CardView mCardContext;

    private static final int REQUEST_CODE_CONTEXT = 304;

    private static String TAG = "EditarPlaylistActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_playlist);
        setCorrectTitle();

        mListView = findViewById(R.id.lv_songlist);
        allSongs = new MusicScanner(this).scan();
        selectedSongs = new ArrayList<>();
        mListView.setOnItemClickListener(this);
        mCardContext = findViewById(R.id.card_context);
        mCardContext.setOnClickListener(this);


        btnUpdatePlaylist = findViewById(R.id.btn_update_playlist);
        btnUpdatePlaylist.setOnClickListener(this);




        mAdapter = new ArrayAdapter<Song>(this,
                android.R.layout.simple_list_item_multiple_choice,
                allSongs.toArray(new Song[allSongs.size()]) );
        mListView.setAdapter(mAdapter);


        checkSongsFromPlaylist();
    }

    private void checkSongsFromPlaylist() {
        ContentResolver resolver = getContentResolver();
        Intent i = getIntent();
        String playlist = i.getStringExtra("playlist");
        Uri uri = getUriFromString(playlist);

        Cursor songsInPlaylist = resolver.query(uri, null, null, null, null);

        for(songsInPlaylist.moveToFirst(); !songsInPlaylist.isAfterLast(); songsInPlaylist.moveToNext()){
            Song s= Song.fromCursor(songsInPlaylist);
            int songIndex = allSongs.indexOf(s);
            if(songIndex < 0)
                continue;
            mListView.setItemChecked(songIndex, true);
        }
        updateSelectedMusic();

    }

    private void setCorrectTitle() {
        String playlist = getIntent().getStringExtra("playlist");
        if(playlist.equalsIgnoreCase("chuvoso")){
            setTitle("Editando \"chuvoso\"");
        }   else if(playlist.equalsIgnoreCase("ensolarado")){
            setTitle("Editando \"ensolarado\"");
        }   else if(playlist.equalsIgnoreCase("no_carro")){
            setTitle("Editando \"no carro\"");
        }   else if(playlist.equalsIgnoreCase("nuvens")){
            setTitle("Editando \"chuvoso\"");
        }   else if(playlist.equalsIgnoreCase("treino")){
            setTitle("Editando \"treino\"" );
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_update_playlist:
                savePlaylist();
                break;
            case R.id.card_context:
                Intent i = new Intent(this, SelectContextActivity.class);
                startActivityForResult(i, REQUEST_CODE_CONTEXT);
        }
    }

    private void savePlaylist() {
        ContentResolver resolver = getContentResolver();
        Intent i = getIntent();
        String playlist = i.getStringExtra("playlist");
        Uri uri = getUriFromString(playlist);
        Log.d(TAG, "savePlaylist: uri=" + uri);
        int songOrder = 0;

        //first, delete all rows from the current playlist
        int deleteResult = resolver.delete(uri, null, null);
        if(deleteResult <= 0){
            Log.d(TAG, "savePlaylist: delete failed, or the playlist was already empty");
        }

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
        PlaylistDAO dao = new PlaylistDAO(this);

        long playlistId = dao.getPlaylistId(playlist);
        return MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
    }

    private void updateSelectedMusic() {
        selectedSongs.clear();
        SparseBooleanArray booleanArray = mListView.getCheckedItemPositions();
        Log.d(TAG, "updateSelectedMusic: " + booleanArray.size() + " songs selected.");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CONTEXT){
            if(resultCode == RESULT_OK){

            }
        }
    }
}
