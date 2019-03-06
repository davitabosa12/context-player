package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class DetectedActivityDefinition implements ContextDefinition {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private int activityType;



    public DetectedActivityDefinition(int activityType) {
        this.activityType = activityType;
    }

    //GETTERS SETTERS
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        //
        // DetectedActivity other = currentContext.getMostProbableActivity();
        List<DetectedActivity> detectedActivities = currentContext.getDetectedActivities();


        if(detectedActivities == null)
            return 0;


        for(DetectedActivity da : detectedActivities){
                if(da.getType() == activityType){
                    return da.getConfidence() / 100.0f;
                }
        }
        return 0;
    }


}
