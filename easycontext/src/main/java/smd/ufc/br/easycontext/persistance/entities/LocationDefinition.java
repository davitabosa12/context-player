package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.snapshot.LocationResponse;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class LocationDefinition implements ContextDefinition {

    @Ignore
    private Location location;

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private float latitude;

    @ColumnInfo
    private float longitude;

    @ColumnInfo
    private float maxDistance;



    /**
     * Maximum distance in meters
     */

    public LocationDefinition(float latitude, float longitude, float maxDistance){
        this.location = new Location("user-defined");
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxDistance = maxDistance;


    }

    /**
     * Default constructor with invalid coordinates. "wildcard value"
     */
    public LocationDefinition(){
        this.location = new Location("user-defined");
        this.latitude = -200.0f;
        this.longitude = -200.0f;
        this.maxDistance = Float.MIN_VALUE;
    }

    public Location getLocation() {
        if(!isLatitudeValid(latitude) || !isLongitudeValid(longitude))
            return null;

        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public LocationDefinition setLocation(Location location) {
        this.location = location;
        return this;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;

    }
    //GETTERS SETTERS
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


    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        if(isLatitudeValid(this.latitude) || isLongitudeValid(longitude) || this.maxDistance < 0){
            return 0.5f; //the default value for any location.
        }
        Location other = currentContext.getLocation();
        if(other == null)
            return 0;
        float distance = getLocation().distanceTo(other);
        if(distance > maxDistance)
            return 0;
        return Maths.normalize(distance, 0, maxDistance);
    }

    private boolean isLatitudeValid(float latitude){
        return -90 <= latitude && latitude <= 90;
    }
    private boolean isLongitudeValid(float longitude){
        return -180 <= longitude && longitude <= 180;
    }
}
