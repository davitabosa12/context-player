package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.awareness.state.TimeIntervals;


import smd.ufc.br.easycontext.ContextComparator;
import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class TimeIntervalDefinition extends ContextDefinition implements TimeIntervals, ContextComparator {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "time_intervals")
    private int[] _timeIntervals;


    public TimeIntervalDefinition(int[] _timeIntervals) {
        this._timeIntervals = _timeIntervals;
    }

    public TimeIntervalDefinition() {
    }

    @Override
    public int[] getTimeIntervals() {

        return _timeIntervals;
    }

    @Override
    public boolean hasTimeInterval(int i) {
        for (int t: _timeIntervals) {
            if(t == i)
                return true;
        }
        return false;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        TimeIntervals other = currentContext.getTimeIntervals();

        if(other == null)
            return 0;

        float damper = 1.0f / _timeIntervals.length;
        int matching = 0; // number of time intervals that match each other
        for(int t : _timeIntervals){
            if(other.hasTimeInterval(t)){
                matching++;
            }
        }
        return matching * damper;
    }
}
