package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.android.gms.awareness.state.TimeIntervals;


import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.persistance.typeconverters.IntegerArrayConverter;

@Entity
public class TimeIntervalDefinition implements TimeIntervals, ContextDefinition  {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @TypeConverters(IntegerArrayConverter.class)
    private int[] timeIntervals;


    @Ignore
    public TimeIntervalDefinition(int[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    public TimeIntervalDefinition() {
        this.timeIntervals = new int[0];
    }


    @Override
    public int[] getTimeIntervals() {

        return timeIntervals;
    }

    public void addTimeInterval(int interval){
        this.timeIntervals = Arrays.copyOf(timeIntervals, timeIntervals.length + 1);
        timeIntervals[timeIntervals.length - 1] = interval;
    }

    @Override
    public boolean hasTimeInterval(int i) {
        for (int t: timeIntervals) {
            if(t == i)
                return true;
        }
        return false;
    }

    //GETTERS SETTERS


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setTimeIntervals(int[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        TimeIntervals other = currentContext.getTimeIntervals();

        if(other == null)
            return 0;

        float damper = 1.0f / other.getTimeIntervals().length;
        int matching = 0; // number of time intervals that match each other
        for(int t : timeIntervals){
            if(other.hasTimeInterval(t)){
                matching++;
            }
        }
        return matching * damper;
    }
}
