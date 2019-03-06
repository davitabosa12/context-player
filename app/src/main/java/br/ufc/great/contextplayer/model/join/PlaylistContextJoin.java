package br.ufc.great.contextplayer.model.join;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Saves a list of context bound to a playlist (ids)
 */
@Entity
public class PlaylistContextJoin {
    @PrimaryKey(autoGenerate = true)
    int uid;

    @ColumnInfo
    long playlistId;

    public PlaylistContextJoin() {
        this.timeIntervalId = -1;
        this.weatherId = -1;
        this.locationId = -1;
        this.activityId = -1;
    }

    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    @ForeignKey(entity = TimeIntervalDefinition.class, parentColumns = "uid", childColumns = "timeIntervalId", onDelete = CASCADE)
    private long timeIntervalId;

    @ForeignKey(entity = WeatherDefinition.class, parentColumns = "uid", childColumns = "weatherId", onDelete = CASCADE)
    private long weatherId;

    @ForeignKey(entity = LocationDefinition.class, parentColumns = "uid", childColumns = "locationId", onDelete = CASCADE)
    private long locationId;

    @ForeignKey(entity = DetectedActivityDefinition.class, parentColumns = "uid", childColumns = "activityId", onDelete = CASCADE)
    private long activityId;

    public long getTimeIntervalId() {
        return timeIntervalId;
    }

    public void setTimeIntervalId(long timeIntervalId) {
        this.timeIntervalId = timeIntervalId;
    }

    public long getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(long weatherId) {
        this.weatherId = weatherId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }
}
