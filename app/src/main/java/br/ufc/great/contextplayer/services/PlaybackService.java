package br.ufc.great.contextplayer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;

import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.PlayerCommands;
import br.ufc.great.contextplayer.model.Song;

public class PlaybackService extends Service
        implements  MediaPlayer.OnPreparedListener,
                    MediaPlayer.OnErrorListener,
                    MediaPlayer.OnCompletionListener {
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs = new ArrayList<>();
    //current position
    private int songPosition;
    private IBinder musicBinder = new PlaybackBinder();
    private static String CONTROL_PLAYER = "contextplayer.command";



    //NOTIFICATION
    private RemoteViews remoteViews;
    private Notification notification;
    private NotificationManager notificationManager;
    //broadcast music player controls
    BroadcastReceiver controlReceiver;
    private static final int NOTIFICATION_ID = 979;
    //button intents
    Intent iNext, iPrevious, iPlaypause, iStop;

    public PlaybackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        player = new MediaPlayer();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("contextplayer", "Default", NotificationManager.IMPORTANCE_DEFAULT));
        }
        iNext = new Intent(CONTROL_PLAYER);
        iNext.putExtra("command", PlayerCommands.NEXT);
        iPlaypause = new Intent(CONTROL_PLAYER);
        iPlaypause.putExtra("command", PlayerCommands.PLAYPAUSE);
        iPrevious = new Intent(CONTROL_PLAYER);
        iPrevious.putExtra("command", PlayerCommands.PREVIOUS);
        iStop = new Intent(CONTROL_PLAYER);
        iStop.putExtra("command", PlayerCommands.STOP);

        controlReceiver = new PlaybackControls();
        registerReceiver(controlReceiver, new IntentFilter(CONTROL_PLAYER));
        initPlayer();
    }

    private void initPlayer(){
        player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes);

        //init notification
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_playing);
        //stop button
        remoteViews.setOnClickPendingIntent(R.id.btn_close, PendingIntent.getBroadcast(this, 143, iStop, 0));
        //previous button
        remoteViews.setOnClickPendingIntent(R.id.btn_previous, PendingIntent.getBroadcast(this, 143, iPrevious, 0));
        //playpause button
        remoteViews.setOnClickPendingIntent(R.id.btn_playpause, PendingIntent.getBroadcast(this, 143, iPlaypause, 0));
        //next button
        remoteViews.setOnClickPendingIntent(R.id.btn_next, PendingIntent.getBroadcast(this, 143, iNext, 0));



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
            //set notification texts
            remoteViews.setTextViewText(R.id.txv_title, playSong.getTitle());
            remoteViews.setTextViewText(R.id.txv_artist, playSong.getArtist());




            notification = new Notification.Builder(this)
                    .setCustomBigContentView(remoteViews)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setOngoing(true)

                    .build();
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
    public void nextSong(){
        songPosition++;
        if(songPosition > songs.size()){
            songPosition = 0;
        }
        playSong();
    }
    public void previousSong(){
        //check player for repeat
        if(player.getCurrentPosition() < 1000){
            playSong();
        } else {
            songPosition--;
            if (songPosition < 0) {
                songPosition = songs.size() - 1;
            }
            playSong();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
        pushNotificationPlayer();
    }

    private void pushNotificationPlayer() {
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void addPlaylist(ArrayList<Song> currentPlaylist) {
        songs.clear();
        songs.addAll(currentPlaylist);
    }

    public class PlaybackBinder extends Binder{
        public PlaybackService getService(){
            return PlaybackService.this;
        }
    }

    public class PlaybackControls extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int command = intent.getIntExtra("command",0);
                switch (command){
                    case PlayerCommands.PLAYPAUSE:
                        if(player.isPlaying()) {
                            player.pause();
                            remoteViews.setImageViewResource(R.id.btn_playpause, R.drawable.ic_play_arrow_black_24dp);
                        }
                        else {
                            player.start();
                            remoteViews.setImageViewResource(R.id.btn_playpause, R.drawable.ic_pause_black_24dp);
                        }
                        break;
                    case PlayerCommands.NEXT:
                        nextSong();
                        break;
                    case  PlayerCommands.PREVIOUS:
                        previousSong();
                        break;
                    case PlayerCommands.STOP:
                        //stop player
                        player.stop();
                        //dismiss notification
                        notificationManager.cancel(NOTIFICATION_ID);

                        break;
                    default:
                        //log error
                        break;
                }

        }
    }

}
