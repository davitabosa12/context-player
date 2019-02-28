package smd.ufc.br.easycontext.definitions;

import com.google.android.gms.location.DetectedActivity;

import smd.ufc.br.easycontext.ContextDefinition;

public class DetectedActivityDefinition extends ContextDefinition {

    public int[] getActivityTypes() {
        return activityTypes;
    }

    public DetectedActivityDefinition setActivityTypes(int... activityTypes) {
        this.activityTypes = activityTypes;
        return this;
    }

    int[] activityTypes;

    public DetectedActivityDefinition() {
    }


    @Override
    public float compareTo(ContextDefinition otherContext) {
        return 0;
    }
}
