package br.ufc.great.contextplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import br.ufc.great.contextplayer.fragments.OnFragmentInteractionListener;
import br.ufc.great.contextplayer.fragments.dialog.CreatePlaylistDialog;
import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.services.PlaybackService;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.Snapshot;


public class Main2Activity extends AppCompatActivity implements OnFragmentInteractionListener, View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainPagerAdapter mainPagerAdapter;
    public static final String DB_NAME = "new_database";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private static final String TAG = "Main2Activity";

    //music player service
    private PlaybackService musicService;
    private FloatingActionButton fabPlaylist;
    private boolean musicBound = false;
    private Intent playIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mTabLayout = findViewById(R.id.tab_layout);



        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mainPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        fabPlaylist = findViewById(R.id.fab_new_playlist);
        fabPlaylist.setOnClickListener(this);

       /* try {
            //snapshot.updateContext(Snapshot.WEATHER, Snapshot.TIME_INTERVAL, Snapshot.DETECTED_ACTIVITY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_playlist) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment createPlaylist = CreatePlaylistDialog.newInstance();
            ((CreatePlaylistDialog) createPlaylist).show(ft, "create_playlist");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Fragment fragment, View view) {
        Playlist p = new Playlist();
//        switch(view.getId()){
//            case R.id.btn_chuvoso:
//                p = new Playlist(getApplicationContext(), "chuvoso");
//                break;
//            case R.id.btn_ensolarado:
//                p = new Playlist(getApplicationContext(), "ensolarado");
//                break;
//            case R.id.btn_no_carro:
//                p = new Playlist(getApplicationContext(), "carro");
//                break;
//            case R.id.btn_nuvens:
//                p = new Playlist(getApplicationContext(), "nuvens");
//                break;
//            case R.id.btn_treino:
//                p = new Playlist(getApplicationContext(), "treino");
//
//                break;
//            default:
//                p = null;
//        }
        musicService.addPlaylist(p.getSongs());
        musicService.setSong(0);
        musicService.playSong();

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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_new_playlist:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment createPlaylist = CreatePlaylistDialog.newInstance();
                ((CreatePlaylistDialog) createPlaylist).show(ft, "create_playlist");
                break;

        }
    }
}
