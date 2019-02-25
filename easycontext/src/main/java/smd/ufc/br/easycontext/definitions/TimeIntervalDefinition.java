package smd.ufc.br.easycontext.definitions;

import com.google.android.gms.awareness.state.TimeIntervals;

import java.sql.Time;

import smd.ufc.br.easycontext.BuildConfig;
import smd.ufc.br.easycontext.ContextComparator;
import smd.ufc.br.easycontext.ContextDefinition;

public class TimeIntervalDefinition extends ContextDefinition implements TimeIntervals, ContextComparator {

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
    public float compareTo(ContextDefinition otherContext) {
        //check if ContextDefinition is an instance of TimeIntervalDefinition
        if(!(otherContext instanceof TimeIntervalDefinition))
            return 0;

        TimeIntervalDefinition other = (TimeIntervalDefinition) otherContext;
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
