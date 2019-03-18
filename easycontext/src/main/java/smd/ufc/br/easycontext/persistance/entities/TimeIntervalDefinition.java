package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.state.TimeIntervals;


import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.math.FloatStatistics;
import smd.ufc.br.easycontext.persistance.typeconverters.IntegerArrayConverter;

@Entity
public class TimeIntervalDefinition implements TimeIntervals, ContextDefinition  {


    //constants
    @Ignore
    public static final int TIME_INTERVAL_WEEKDAY = 1;
    @Ignore
    public static final int TIME_INTERVAL_WEEKEND = 2;
    @Ignore
    public static final int TIME_INTERVAL_HOLIDAY = 3;
    @Ignore
    public static final int TIME_INTERVAL_MORNING = 4;
    @Ignore
    public static final int TIME_INTERVAL_AFTERNOON = 5;
    @Ignore
    public static final int TIME_INTERVAL_EVENING = 6;
    @Ignore
    public static final int TIME_INTERVAL_NIGHT = 7;
    @Ignore
    public static final int TIME_INTERVAL_ANY = 8;

    @Ignore
    private boolean isDirty = false;



    @PrimaryKey(autoGenerate = true)
    private int uid;

    @TypeConverters(IntegerArrayConverter.class)
    private int[] timeIntervals;


    @Ignore
    public TimeIntervalDefinition(int[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    /**
     * Default ctor with ANY value
     */
    public TimeIntervalDefinition() {
        this.timeIntervals = new int[]{TIME_INTERVAL_ANY};
    }


    @Override
    public int[] getTimeIntervals() {

        return timeIntervals;
    }

    public TimeIntervalDefinition addTimeInterval(int interval){
        if(!isDirty){
            this.timeIntervals = new int[0];
            isDirty = true;
        }
        this.timeIntervals = Arrays.copyOf(timeIntervals, timeIntervals.length + 1);
        timeIntervals[timeIntervals.length - 1] = interval;
        return this;
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
        if(currentContext == null){
            return 0.0f;
        }
        TimeIntervals currentContextTimeIntervals = currentContext.getTimeIntervals();

        if(currentContextTimeIntervals == null)
            return 0;
        FloatStatistics statistics = new FloatStatistics();


        for(int t : currentContextTimeIntervals.getTimeIntervals()){
            if(this.hasTimeInterval(t)){
                //time intervals match
                statistics.accept(1.0f);
            } else {
                //time intervals dont match
                statistics.accept(0.0f);
            }
        }
        //if defined context doesnt match with current context AND user said that ANY time interval is ok..
        if(statistics.getSum() == 0 && this.hasTimeInterval(TIME_INTERVAL_ANY)){
            //return default value 0.5f
            return 0.5f;
        }
        //else..
        return statistics.getAverage();

    }
}
