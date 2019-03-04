package smd.ufc.br.easycontext.persistance.entities;

import android.location.Location;

import com.google.android.gms.awareness.snapshot.LocationResponse;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

public class LocationDefinition implements ContextDefinition {

    Location location;

    /**
     * Maximum distance in meters
     */
    float maxDistance;

    public LocationDefinition() {
        maxDistance = 50;
    }

    public Location getLocation() {
        return location;
    }

    public LocationDefinition setLocation(Location location) {
        this.location = location;
        return this;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public LocationDefinition setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        Location other = currentContext.getLocation();
        if(other == null)
            return 0;
        float distance = location.distanceTo(other);
        if(distance > maxDistance)
            return 0;
        return Maths.normalize(distance, 0, maxDistance);
    }
}
