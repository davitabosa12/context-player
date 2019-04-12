package smd.ufc.br.easycontext.fence.parameter;

import android.util.Log;

import java.util.ArrayList;

import smd.ufc.br.easycontext.fence.DetectedActivityFence;

/**
 * Created by davitabosa on 18/06/2018.
 */

public class DetectedActivityParameter implements FenceParameter {
    private ArrayList<Integer> activityTypeList = null;



    private DetectedActivityParameter(){
        activityTypeList = new ArrayList<Integer>();
        Log.d("AwarenessLib","DetectedActivityParameter created.");
    }

    public ArrayList<Integer> getActivityTypeList() {
        return activityTypeList;
    }

    public void addActivityType(int activityType){
        activityTypeList.add(activityType);
    }

    public static class Builder{
        ArrayList<Integer> activityTypeList = new ArrayList<Integer>();
        public Builder(){

        }
        public Builder addActivityType(int activityType){
            activityTypeList.add(activityType);
            return this;
        }

        public Builder addActivityType(String activityName){
            if(activityName.equals("IN_VEHICLE")){
                activityTypeList.add(DetectedActivityFence.IN_VEHICLE);
            } else if(activityName.equals("RUNNING")){
                activityTypeList.add(DetectedActivityFence.RUNNING);
            } else if(activityName.equals("ON_FOOT")){
                activityTypeList.add(DetectedActivityFence.ON_FOOT);
            } else if(activityName.equals("ON_BICYCLE")){
                activityTypeList.add(DetectedActivityFence.ON_BICYCLE);
            } else if(activityName.equals("STILL")){
                activityTypeList.add(DetectedActivityFence.STILL);
            } else if(activityName.equals("WALKING")){
                activityTypeList.add(DetectedActivityFence.WALKING);
            } else if(activityName.equals("UNKNOWN")){
                activityTypeList.add(DetectedActivityFence.UNKNOWN);
            }
            return this;
        }

        public DetectedActivityParameter build(){
            DetectedActivityParameter temp = new DetectedActivityParameter();
            for(int at : activityTypeList){
                temp.addActivityType(at);
            }
            return temp;
        }
    }
}
