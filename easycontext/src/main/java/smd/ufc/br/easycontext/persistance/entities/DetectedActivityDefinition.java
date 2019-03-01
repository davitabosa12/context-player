package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;

@Entity
public class DetectedActivityDefinition extends ContextDefinition {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "activityTypes")
    int[] activityTypes;

    public DetectedActivityDefinition setActivityTypes(int... activityTypes) {
        this.activityTypes = activityTypes;
        return this;
    }


    public int[] getActivityTypes() {
        return activityTypes;
    }

    public DetectedActivityDefinition() {
    }


    @Override
    public float compareTo(ContextDefinition otherContext) {
        return 0;
    }
}
