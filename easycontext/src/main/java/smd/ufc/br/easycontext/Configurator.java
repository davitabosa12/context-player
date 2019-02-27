package smd.ufc.br.easycontext;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Configurator {
    private Activity activity;
    private Service service;
    private static Context context;
    private static Configurator instance;
    private static String activityName;
    private static List<AwarenessActivity> activities;
    private static FenceManager fenceManager;

    private Configurator(Activity activity) {
        this.activity = activity;
        context = activity.getApplicationContext();
        fenceManager = FenceManager.getInstance(context);

        activityName = activity.getClass().getName();


        Log.d("AwarenessLib", activityName);

        try {
            activities = new JSONParser(context).readJSON();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Configurator(Service service) {
        this.service = service;
        context = service.getApplicationContext();
        fenceManager = FenceManager.getInstance(context);
        activityName = service.getClass().getName();
        JSONParser parser = new JSONParser(context);
        Log.d("AwarenessLib", activityName);

        try {
            activities = new JSONParser(context).readJSON();
        } catch (IOException e) {
            Log.e("AwarenessLib", "Error reading JSON");
            e.printStackTrace();
        }
    }

    /**
     * Getter for the Activity which this Configurator is bound to
     *
     * @return null if it's bound by a service, otherwise the bound activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Getter for the Service which this Configurator is bound to
     *
     * @return null if it's bound to an activity, otherwise the bound service
     */
    public Service getService() {
        return service;
    }

    public static Future<Configurator> init(final Service service, Map<String, FenceAction> actions) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Callable<Configurator>() {

            @Override
            public Configurator call() throws Exception {
                if (instance == null) {
                    Log.d("AwarenessLib", service.getClass().getName());
                    instance = new Configurator(service);
                } else {
                    activityName = service.getClass().getName();
                }

                //unregister fences
                fenceManager.unregisterAll();
                for (AwarenessActivity a : activities) {
                    if (a.getName().equals(activityName)) {
                        ArrayList<Fence> fs = a.getFences();
                        for (Fence f : fs) {
                            fenceManager.registerFence(f);
                        }
                    }
                }

                return instance;
            }
        });

    }

    public static Future<Configurator> init(final Activity activity) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Callable<Configurator>() {

            @Override
            public Configurator call() throws Exception {
                if (instance == null) {
                    Log.d("AwarenessLib", activity.getClass().getName());
                    instance = new Configurator(activity);
                } else {
                    activityName = activity.getClass().getName();
                    context = activity.getApplicationContext();

                }
                //unregister fences
                fenceManager.unregisterAll();
                for (AwarenessActivity a : activities) {
                    String name = a.getName();
                    if (name.equals(activityName)) {
                        ArrayList<Fence> fs = a.getFences();
                        for (Fence f : fs) {
                            fenceManager.registerFence(f);
                        }
                    }
                }
                return instance;

            }


        });
    }
}