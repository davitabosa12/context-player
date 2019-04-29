package br.ufc.great.contextplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufc.great.contextplayer.database.AppDb;
import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.PlaylistConfidence;
import br.ufc.great.contextplayer.model.PlaylistContexts;
import br.ufc.great.contextplayer.model.PlaylistDAO;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.Snapshot;
import smd.ufc.br.easycontext.fence.FenceAction;

public class ShowNotificationAction implements FenceAction, Snapshot.OnContextUpdate {
    private final String TAG = "ShowNotificationAction";
    private Context context;
    @Override
    public void doOperation(Context context, FenceState state) {
        Log.d(TAG, "doOperation: triggered!");
        //TODO: push notification
        this.context = context;
        Snapshot snapshot = Snapshot.getInstance(context);
        snapshot.setCallback(this);
        snapshot.updateContext(Snapshot.TIME_INTERVAL, Snapshot.WEATHER, Snapshot.DETECTED_ACTIVITY);

    }

    @Override
    public void onContextUpdate(CurrentContext currentContext) {
        List<PlaylistConfidence> playlistConfidences = new ArrayList<>();
        playlistConfidences.clear();
        List<Playlist> playlists = new PlaylistDAO(context).getAllPlaylists();
        for(Playlist playlist : playlists){
            PlaylistContexts contexts = playlist.getDefinitions();
            float confidence;
            if (contexts == null) {
                confidence = 0.0f;
            }else{
                confidence = contexts.calculateConfidence(currentContext);
            }
            playlistConfidences.add(new PlaylistConfidence(playlist, confidence));
        }
        //sort it descending (reverse order)
        Collections.sort(playlistConfidences, Collections.<PlaylistConfidence>reverseOrder());
        playlists = new ArrayList<>();
        for(PlaylistConfidence confidence : playlistConfidences){
            playlists.add(confidence.getPlaylist());
        }
        if(playlists.size() > 0)
            pushNotification(playlistConfidences.get(0).getPlaylist());
        else
            Log.d(TAG, "onContextUpdate: no playlists");
    }

    private void pushNotification(Playlist playlist) {
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("FENCE_CHANNEL", "Smart Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            nm.getNotificationChannel("FENCE_CHANNEL");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FENCE_CHANNEL");
        builder.setContentTitle("Headphone Connected");
        builder.setContentTitle("Recommended playlist: " + playlist.getName());
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        nm.notify(414, builder.build());
    }
}
