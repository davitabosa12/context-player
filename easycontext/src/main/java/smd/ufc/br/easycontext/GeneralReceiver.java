package smd.ufc.br.easycontext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

public class GeneralReceiver extends BroadcastReceiver {

    private final String TAG = "GeneralReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        FenceState state = FenceState.extract(intent);
        String fenceName = state.getFenceKey();
        Log.d(TAG, "onReceive: received update from fence \"" + fenceName +  "\"");

        //TODO: Redirect behavior to registered Actions
    }
}
