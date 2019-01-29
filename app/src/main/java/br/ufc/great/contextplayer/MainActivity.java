package br.ufc.great.contextplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.Permissions;
import java.util.ArrayList;

import br.ufc.great.contextplayer.model.Song;
import br.ufc.great.contextplayer.views.SongViewRecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MusicScanner scanner;
    ArrayList<Song> songs;



    RecyclerView mRecyclerView;
    SongViewRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = new ArrayList<>();

        scanner = new MusicScanner(this);
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 777);
            }
            else{
                songs = (ArrayList<Song>) scanner.scan();

            }

        } else {
            songs = (ArrayList<Song>) scanner.scan();
        }
        configuraRecycler();
        mAdapter.notifyDataSetChanged();

        for(Song song: songs){
            Log.d(TAG, "onCreate: " + song.toString());
        }



    }

    private void configuraRecycler(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SongViewRecyclerAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                songs = (ArrayList<Song>) scanner.scan();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
