package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.snapshot.LocationResponse;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class LocationDefinition implements ContextDefinition {

    Location location;

    @PrimaryKey(autoGenerate = true)
    int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Maximum distance in meters
     */
    @ColumnInfo(name = "max_distance")
    float maxDistance;

    @ColumnInfo(name = "latitude")
    float latitude;

    @ColumnInfo(name = "longitude")
    float longitude;


    public LocationDefinition() {
        this.location = new Location("user-defined");
        this.latitude = 0;
        this.longitude = 0;

        location.setLatitude(latitude);
        location.setLongitude(longitude);
        maxDistance = 50;
    }

    public LocationDefinition(float latitude, float longitude, float maxDistance){
        this.location = new Location("user-defined");
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxDistance = maxDistance;

        location.setLatitude(latitude);
        location.setLongitude(longitude);
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
