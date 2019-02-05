package br.ufc.great.contextplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.Permissions;
import java.util.ArrayList;

import br.ufc.great.contextplayer.model.Song;
import br.ufc.great.contextplayer.services.PlaybackService;
import br.ufc.great.contextplayer.views.SongView;
import br.ufc.great.contextplayer.views.SongViewRecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    MusicScanner scanner;
    ArrayList<Song> songs;
    ArrayList<Song> currentPlaylist = new ArrayList<>();


    private Intent playIntent;
    private PlaybackService musicService;
    private boolean musicBound = false;



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
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof SongView){
                    SongView songView = (SongView) v;


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.WAKE_LOCK};
                            requestPermissions(permissions, 1337);
                            return;
                        }
                    }

                    Toast.makeText(getApplicationContext(), songView.getSong().toString(), Toast.LENGTH_SHORT).show();
                    currentPlaylist.clear();
                    currentPlaylist.addAll(songs);

                    musicService.addPlaylist(currentPlaylist);
                    musicService.setSong(currentPlaylist.indexOf(songView.getSong()));
                    musicService.playSong();
                } else {
                    Log.e(TAG, "onClick: v is not an instance of SongView.", new ClassCastException());
                }

            }
        });

        for(Song song: songs){
            Log.d(TAG, "onCreate: " + song.toString());
        }
    }



    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaybackService.PlaybackBinder binder = (PlaybackService.PlaybackBinder) service;
            musicService = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null){
            playIntent = new Intent(this, PlaybackService.class);
            bindService(playIntent, musicConnection, BIND_AUTO_CREATE);
            startService(playIntent);
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
