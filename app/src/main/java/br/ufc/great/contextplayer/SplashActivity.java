package br.ufc.great.contextplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomOpenHelper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import br.ufc.great.contextplayer.model.Playlist;
import smd.ufc.br.easycontext.persistance.databases.ContextDb;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private static final String TAG = "SplashActivity";

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_splash);
        ContextDb database = ContextDb.getInstance(this, "new_database");
        database.detectedActivityDAO().getById(3);
        database.locationDAO().getById(0);
        database.timeIntervalDAO().getById(0);
        database.weatherDAO().getById(0);
//        ContextDb db2 = Room.databaseBuilder(this, ContextDb.class, "new_database").build();
//        //Cursor c = db2.query("SELECT * FROM " + Room.MASTER_TABLE_NAME, null);
//        Cursor c = db2.query("SELECT * FROM " + Room.MASTER_TABLE_NAME, null);
//        if (c == null) {
//            Log.i(TAG, "onCreate: c is null");
//        }
//        Log.d(TAG, "onCreate: count = " + c.getCount());
//        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
//            for (String col:
//            c.getColumnNames()    ) {
//                Log.d(TAG, "onCreate: column " + col);
//
//            }
//        }
        Log.d(TAG, "onCreate: data directory -> " + getDataDir());
        String [] list = databaseList();
//        for(String database : list){
//
//            Log.d(TAG, "database: " + database);
//            Log.d(TAG, "onCreate: database path ->>>" + getDatabasePath(database));
//            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(database), null);
//            Log.d(TAG, "onCreate: path = " + db.getPath());
//            Log.d(TAG, "onCreate: version = " + db.getVersion());
//            Log.d(TAG, "onCreate: integrity ok? " + db.isDatabaseIntegrityOk());
//
//
//
//        }

        //todo: thread de 3 segundos

        boolean granted = true;
        String[] permissions = {Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for(String permission : permissions){
            if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
                granted = false;
            }
        }
        if(granted){
            checkAndCreateDefaultPlaylists();
            startActivity(new Intent(this, Main2Activity.class));
        } else {
            requestPermissions(permissions, 1337);
        }




        finish();

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result : grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                finish();
                return;

            }
        }
        checkAndCreateDefaultPlaylists();
        startActivity(new Intent(this, Main2Activity.class));
    }

    private void checkAndCreateDefaultPlaylists() {
//        for(String playlistName : DEFAULT_PLAYLISTS) {
//            if (Playlist.getPlaylistId(this, playlistName) == -1) {
//                //playlist doesn't exist, create a new one
//
//                long id = Playlist.createBlankPlaylist(this, playlistName);
//                Log.d(TAG, "checkAndCreateDefaultPlaylists: created " + playlistName + " with id = " + id);
//
//            }
//        }
    }

    public static String[] DEFAULT_PLAYLISTS = {"chuvoso", "ensolarado", "carro", "treino", "nuvens"};

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
