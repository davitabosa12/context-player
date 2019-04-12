package smd.ufc.br.easycontext.fence;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import smd.ufc.br.easycontext.GeneralReceiver;

/**
 * Created by davitabosa on 08/08/2018.
 */

public class FenceManager {
    private static FenceManager instance;
    private FenceClient client;
    private List<Fence> registeredFences;
    private Context context;


    private FenceManager(Context ctx){
        this.context = ctx;
        this.client = Awareness.getFenceClient(context);
        registeredFences = new ArrayList<>();

    }

    public static FenceManager getInstance(Context ctx){
        if(instance == null){
            instance = new FenceManager(ctx);
        }
        return instance;
    }
    public boolean isFenceRegistered(Fence fence){
        if(registeredFences.indexOf(fence) < 0){
            //fence isn't registered
            return false;
        }
        return true;
    }

    @Nullable
    public Task registerFence(Fence fence){
        //check if fence is already registered.
        if(isFenceRegistered(fence)){

            Log.d("AwarenessLib", "Fence " + fence.getName() + "is already registered.");
            return null;
        }
        else{
            final FenceAction theFenceAction = fence.getAction();
            BroadcastReceiver myReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    theFenceAction.doOperation(context, FenceState.extract(intent));
                }
            };

            String filter = fence.getName();
            Intent i = new Intent(filter);
            PendingIntent pi = PendingIntent.getBroadcast(context, new Random().nextInt(),i,PendingIntent.FLAG_CANCEL_CURRENT);
            context.registerReceiver(myReceiver,new IntentFilter(filter));

            final String fenceName = fence.getName();

            Task t = client.updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(),fence.getMethod(),pi).build());
            return t;
        }

    }

    public Task registerFence2(Fence fence){
        Intent i = new Intent(context, GeneralReceiver.class);
        String actionName = fence.getAction().getClass().getName();
        i.putExtra("actionName", actionName);
        //pending intent to trigger GeneralReceiver
        PendingIntent pi = PendingIntent.getBroadcast(context, new Random().nextInt(), i , PendingIntent.FLAG_CANCEL_CURRENT);
        //register info to Awareness API
        Task t = client.updateFences(new FenceUpdateRequest.Builder().addFence(fence.getName(), fence.getMethod(), pi).build());
        return t;
    }


    @Nullable
    public Task unregisterFence(final Fence fence){
        if(isFenceRegistered(fence)){
            //unregister the fence
            final String fenceName = fence.getName();
            final Task t = Awareness.getFenceClient(context).updateFences(new FenceUpdateRequest.Builder().removeFence(fence.getName()).build());

            t.addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d("AwarenessLib", "Fence removal successful: " + fenceName);
                    registeredFences.remove(fence);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("AwarenessLib", "Fence removal failed for " + fenceName + ": " + t.getException().getMessage());
                }
            });
            return t;
        } else{
            //can't unregister a fence thar is not registered
            Log.d("AwarenessLib","Can't unregister fence " + fence.getName() + " that is not registered");
            return null;
        }
    }

    public void unregisterAll(){

        final Iterator<Fence> it = registeredFences.iterator();
        while(it.hasNext()){
            final Fence f  = it.next();
            final Task t = unregisterFence(f);
            t.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("AwarenessLib", "Error removing fence " + f.getName() + ". " + t.getException().getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    it.remove();
                }
            });

        }
    }


}
