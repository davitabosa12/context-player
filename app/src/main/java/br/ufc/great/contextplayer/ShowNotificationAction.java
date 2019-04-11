package br.ufc.great.contextplayer;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import smd.ufc.br.easycontext.FenceAction;

public class ShowNotificationAction implements FenceAction {
    private final String TAG = "ShowNotificationAction";
    @Override
    public void doOperation(Context context, FenceState state) {
        Log.d(TAG, "doOperation: triggered!");
        //TODO: push notification
    }
}
