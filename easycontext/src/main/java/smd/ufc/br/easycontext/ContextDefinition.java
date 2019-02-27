package smd.ufc.br.easycontext;

import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.places.PlaceLikelihood;

/**
 * ContextDefinition
 */
public class ContextDefinition {

    public ContextDefinition(){

    }

    public static class Maths{
        /**
         * Clamps some float to a ceiling
         * @param value the value to clamp
         * @param ceil the maximum value
         * @return ceil if value > ceil. Otherwise simply returns value.
         */
        public static float clamp(float value, float ceil){
            if(value > ceil)
                return ceil;
            else
                return value;
        }
    }
}
