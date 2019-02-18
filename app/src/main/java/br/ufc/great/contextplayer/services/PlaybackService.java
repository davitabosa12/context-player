package br.ufc.great.contextplayer.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Song;
import br.ufc.great.contextplayer.util.PlayerIntentActions;

public class PlaybackService extends Service
        implements  MediaPlayer.OnPreparedListener,
                    MediaPlayer.OnErrorListener,
                    MediaPlayer.OnCompletionListener {
    private static String TAG = "PlaybackService";
    private static final String NOTIFICATION_ID = "context-player.default";
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs = new ArrayList<>();
    //current position
    private int songPosition;
    private IBinder musicBinder = new PlaybackBinder();
    private Context context;
    private NotificationManager nm;
    private Song currentSong;
    private RemoteViews remoteViews;
    private int notificationId = 985;
    private PlaybackControl control;


    public PlaybackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        this.context = this;
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //set notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_ID, "Padrão", NotificationManager.IMPORTANCE_LOW));
        }

        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_playing);
        control = new PlaybackControl();

        player = new MediaPlayer();
        initPlayer();
        //register the receiver
        registerReceiver(control, new IntentFilter(PlayerIntentActions.CONTROL_PLAYBACK));
        Log.d(TAG, "onCreate: SERVICE STARTED!");
    }

    private void initPlayer(){
        player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes);


        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        player.stop();
        player.release();
        return super.onUnbind(intent);
    }

    public void playSong(){
        player.reset();
        Song playSong = songs.get(songPosition);
        try {
            player.setDataSource(playSong.getData());
            currentSong = playSong;
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public void setSong(int index){
        if(index > songs.size() - 1){
            //out of bounds
            return;
        } else {
            songPosition = index;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
        pushPlayingNotification();
    }

    private void pushPlayingNotification() {
        remoteViews.setTextViewText(R.id.txv_title, currentSong.getTitle());
        remoteViews.setTextViewText(R.id.txv_artist, currentSong.getArtist());
        Intent playpause, stop, next, previous;

        playpause = new Intent(PlayerIntentActions.CONTROL_PLAYBACK);
        //the action extra tells the receiver which action to perform: playing, changing music, etc.
        playpause.putExtra("action", PlayerIntentActions.PLAY_PAUSE);

        stop = new Intent(PlayerIntentActions.CONTROL_PLAYBACK);
        stop.putExtra("id", notificationId); // put notification id to cancel itself on click
        stop.putExtra("action", PlayerIntentActions.STOP);

        next = new Intent(PlayerIntentActions.CONTROL_PLAYBACK);
        next.putExtra("action", PlayerIntentActions.NEXT);

        previous = new Intent(PlayerIntentActions.CONTROL_PLAYBACK);
        previous.putExtra("action", PlayerIntentActions.PREVIOUS);

        //set on click pending intent listeners
        remoteViews.setOnClickPendingIntent(R.id.btn_playpause,
                PendingIntent.getBroadcast(context, 141, playpause, 0)); //play pause
        remoteViews.setImageViewResource(R.id.btn_playpause, R.drawable.ic_pause_black_24dp);

        remoteViews.setOnClickPendingIntent(R.id.btn_stop,
                PendingIntent.getBroadcast(context, 145, stop, 0)); // stop
        remoteViews.setImageViewResource(R.id.btn_stop, R.drawable.ic_close_black_24dp);

        remoteViews.setOnClickPendingIntent(R.id.btn_next,
                PendingIntent.getBroadcast(context, 143, next, 0)); //next
        remoteViews.setImageViewResource(R.id.btn_next, R.drawable.ic_skip_next_black_24dp);

        remoteViews.setOnClickPendingIntent(R.id.btn_previous,
                PendingIntent.getBroadcast(context, 144, previous, 0)); //previous
        remoteViews.setImageViewResource(R.id.btn_previous, R.drawable.ic_skip_previous_black_24dp);

        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setCustomBigContentView(remoteViews);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_ID);
        }

        nm.notify(notificationId, builder.build());

    }

    public void addPlaylist(List<Song> currentPlaylist) {
        songs.clear();
        songs.addAll(currentPlaylist);
    }

    public class PlaybackBinder extends Binder{
        public PlaybackService getService(){
            return PlaybackService.this;
        }
    }

    public class PlaybackControl extends BroadcastReceiver {

        private String TAG = "PlaybackControl";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if(action.equals(PlayerIntentActions.PLAY_PAUSE)){
                Log.d(TAG, "onReceive: PLAYPAUSE");
                playPause();
            } else if(action.equals(PlayerIntentActions.STOP)){
                Log.d(TAG, "onReceive: STOP");
                stop(intent);
            } else if(action.equals(PlayerIntentActions.NEXT)){
                Log.d(TAG, "onReceive: NEXT");
                next();
            } else if(action.equals(PlayerIntentActions.PREVIOUS)){
                Log.d(TAG, "onReceive: PREVIOUS");
                previous();
            } else {
                Log.e(TAG, "onReceive: can't understand action \"" + action + "\".");
            }
        }

        private void previous() {
            //check duration of song
            if(player.getCurrentPosition() < 1000){ //less than a second
                player.seekTo(0);
            } else {
                songPosition--;
                if(songPosition < 0){
                    songPosition = songs.size() - 1; // wrap the list?
                }
                setSong(songPosition);
                playSong();
            }

        }

        private void next() {
            songPosition++;
            if(songPosition >= songs.size()){
                songPosition = 0;
            }
            setSong(songPosition);
            playSong();

        }

        private void stop(Intent intent) {
            int id = intent.getIntExtra("id",Integer.MIN_VALUE);
            player.stop();
            nm.cancel(id);

        }

        private void playPause() {
            if(player.isPlaying()){
                remoteViews.setImageViewResource(R.id.btn_playpause, R.drawable.ic_play_arrow_black_24dp);
                player.pause();
            } else {
                remoteViews.setImageViewResource(R.id.btn_playpause, R.drawable.ic_pause_black_24dp);
                player.start();
            }
        }



    }
}
