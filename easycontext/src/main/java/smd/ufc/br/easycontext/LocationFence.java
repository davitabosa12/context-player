package smd.ufc.br.easycontext;

import android.annotation.SuppressLint;

import com.google.android.gms.awareness.fence.AwarenessFence;

/**
 * Created by davitabosa on 13/08/2018.
 */

public class LocationFence extends Fence {

    private final LocationMethod method;
    private final LocationParameter params;

    public LocationFence(String name, LocationMethod method, FenceAction action, LocationParameter params) {
        super(name, FenceType.LOCATION, action, params);
        this.params = params;
        this.method = method;

    }

    @SuppressLint("MissingPermission")
    @Override
    public AwarenessFence getMethod() {
        //TODO: Check if params are passed
        switch(method){
            case LOCATION_ENTERING:
                return com.google.android.gms.awareness.fence.LocationFence.entering(params.getLatitude(),params.getLongitude(),params.getRadius());
            case LOCATION_EXITING:
                return com.google.android.gms.awareness.fence.LocationFence.exiting(params.getLatitude(),params.getLongitude(),params.getRadius());
            case LOCATION_IN:
                return com.google.android.gms.awareness.fence.LocationFence.in(params.getLatitude(),params.getLongitude(),params.getRadius(),params.getDwellTimeMillis());
            default:
                return null;
        }
    }
}
