package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.awareness.state.TimeIntervals;


import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class TimeIntervalDefinition implements TimeIntervals, ContextDefinition  {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "time_intervals")
    private int[] _timeIntervals;


    public TimeIntervalDefinition(int[] _timeIntervals) {
        this._timeIntervals = _timeIntervals;
    }

    public TimeIntervalDefinition() {
        this._timeIntervals = new int[0];
    }

    @Override
    public int[] getTimeIntervals() {

        return _timeIntervals;
    }

    public void addTimeInterval(int interval){
        this._timeIntervals = Arrays.copyOf(_timeIntervals, _timeIntervals.length + 1);
        _timeIntervals[_timeIntervals.length - 1] = interval;
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
