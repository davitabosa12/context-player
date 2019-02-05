package br.ufc.great.contextplayer.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;
import java.util.ArrayList;

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

    public PlaybackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        player = new MediaPlayer();
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
        pushNotificationPlayer();
    }

    private void pushNotificationPlayer() {

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
}
