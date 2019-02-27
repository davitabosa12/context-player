package br.ufc.great.contextplayer.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Song;

public class SongView extends ConstraintLayout {
    Song song;
    TextView txvTitle, txvArtist, txvDuration;
    private boolean inflated = false;
    private static final String TAG = "SongView";

    public SongView(Context context) {
        super(context);
        init(context);
    }

    public SongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SongView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_list_element, this);

        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        txvArtist = findViewById(R.id.txv_artist);
        txvDuration = findViewById(R.id.txv_duration);
        txvTitle = findViewById(R.id.txv_title);
        inflated = true;
        updateUI();
    }

    private void updateUI() {
        if(song == null){
            //Log.e(TAG, "updateUI: song is null", new NullPointerException());
            return;
        }
        //todo: update ui on new song
        txvTitle.setText(song.getTitle());
        txvArtist.setText(song.getArtist());
        String duration = calculateDuration(song.getDuration());
        txvDuration.setText(duration);
    }

    private String calculateDuration(String duration) {
        int minutes, seconds;
        int durationInMillis = Integer.parseInt(duration);
        int totalSeconds = durationInMillis / 1000;
        seconds = totalSeconds % 60;
        minutes = (totalSeconds / 60);


        String secondsString, minutesString, finalString;
        if(seconds < 10){
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds ;
        }

        finalString = minutes + ":" + secondsString;

        return finalString;


    }

    public void setSong(Song song) {
        this.song = song;
        updateUI();
    }

    public Song getSong() {
        return song;
    }
}
