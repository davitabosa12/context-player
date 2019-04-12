package smd.ufc.br.easycontext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import smd.ufc.br.easycontext.fence.FenceAction;

public class GeneralReceiver extends BroadcastReceiver {

    private final String TAG = "GeneralReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        FenceState state = FenceState.extract(intent);
        String fenceName = state.getFenceKey();
        if(intent.getBooleanExtra("survivor", false)){
            Log.d(TAG, "onReceive: SURVIVED");
        } else {
            Log.d(TAG, "onReceive: is ded");

        }
        Log.d(TAG, "onReceive: received update from fence \"" + fenceName +  "\"");

        //TODO: Redirect behavior to registered Actions

        String className = intent.getStringExtra("actionName");
        try {
            FenceAction action = (FenceAction) Class.forName(className).newInstance();
            action.doOperation(context, state);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
