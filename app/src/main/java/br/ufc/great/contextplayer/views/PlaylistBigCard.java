package br.ufc.great.contextplayer.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.PlaylistContexts;
import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

public class PlaylistBigCard extends CardView {

    TextView mTitle;
    int[] mContexts;
    ImageView[] imageContexts;
    private Playlist playlist;

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public PlaylistBigCard setTitle(TextView title) {
        this.mTitle = mTitle;
        return this;
    }

    public int[] getContexts() {
        return mContexts;
    }

    public PlaylistBigCard setContexts(int[] contexts) {
        this.mContexts = contexts;
        return this;
    }





    public static final int CONTEXT_LOCATION = 305;
    public static final int CONTEXT_DETECTED_ACTIVITY = 938;
    public static final int CONTEXT_WEATHER = 655;
    public static final int CONTEXT_TIME_INTERVALS = 222;
    private static final int CONTEXT_PLACEHOLDER = 267;

    public PlaylistBigCard(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlaylistBigCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlaylistBigCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.big_cardview, this);

        onFinishInflate();

        imageContexts = new ImageView[3];
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = findViewById(R.id.txv_title);
    }

    public void setPlaylist(Playlist playlist){
        this.playlist = playlist;
        mTitle.setText(playlist.getName());
        PlaylistContexts definitions = playlist.getDefinitions();
        setContextImages(definitions);

    }

    private void setContextImages(PlaylistContexts definitions) {
        int loopTimes = Math.min(imageContexts.length, definitions.size());
        int imageIndex = 0;
        for(int i = 0; i < loopTimes; i++){
            ContextDefinition d = definitions.get(i);
            if(d instanceof WeatherDefinition){
                imageContexts[imageIndex].setAlpha(1.0f);
                imageContexts[imageIndex].setImageDrawable(getContext().getDrawable(R.drawable.ic_weather_black_24dp));
                imageIndex++;
            } else if(d instanceof TimeIntervalDefinition){
                imageContexts[imageIndex].setAlpha(1.0f);
                imageContexts[imageIndex].setImageDrawable(getContext().getDrawable(R.drawable.ic_time_black_24dp));
                imageIndex++;
            }
        }
    }
}
