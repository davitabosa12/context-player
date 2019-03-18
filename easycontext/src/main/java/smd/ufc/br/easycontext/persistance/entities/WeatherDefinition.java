package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.Weather;

import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.math.FloatStatistics;
import smd.ufc.br.easycontext.persistance.typeconverters.IntegerArrayConverter;

@Entity
public class WeatherDefinition implements Weather, ContextDefinition  {



    //constants
    @Ignore
    public static final int CONDITION_ANY = 0;
    @Ignore
    public static final int CONDITION_UNKNOWN = 0;
    @Ignore
    public static final int CONDITION_CLEAR = 1;
    @Ignore
    public static final int CONDITION_CLOUDY = 2;
    @Ignore
    public static final int CONDITION_FOGGY = 3;
    @Ignore
    public static final int CONDITION_HAZY = 4;
    @Ignore
    public static final int CONDITION_ICY = 5;
    @Ignore
    public static final int CONDITION_RAINY = 6;
    @Ignore
    public static final int CONDITION_SNOWY = 7;
    @Ignore
    public static final int CONDITION_STORMY = 8;
    @Ignore
    public static final int CONDITION_WINDY = 9;
    @Ignore
    private boolean isDirty = false;

    // --------------------- FIELDS ------------------------ //
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    private float temperature;

    @ColumnInfo
    private float feelsLikeTemperature;

    @ColumnInfo
    private float dewPoint;

    @ColumnInfo
    private int humidity;

    @ColumnInfo
    @TypeConverters(IntegerArrayConverter.class)
    private int[] conditions;

    // -------------------- LOOK-UP MATRICES ---------------- //

    @Ignore
    private static float[][] CONDITIONS_MATRIX = {
    //         0    1     2     3     4     5     6     7      8     9
            {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f}, //CONDITION UNKNOWN 0
            {0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION CLEAR   1
            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f}, //CONDITION CLOUDY  2
            {0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION FOGGY   3
            {0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION HAZY    4
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.6f, 0.0f, 0.0f}, //CONDITION ICY     5
            {0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f}, //CONDITION RAINY   6
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 0.0f, 1.0f, 0.0f, 0.0f}, //CONDITION SNOWY   7
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.3f}, //CONDITION STORMY  8
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 1.0f}  //CONDITION WINDY   9

    };

    @Ignore
    public WeatherDefinition(@Nullable float temperature,
                             @Nullable float feelsLikeTemperature,
                             @Nullable float dewPoint,
                             @Nullable int humidity,
                             @Nullable int[] conditions) {
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.dewPoint = dewPoint;
        this.humidity = humidity;
        this.conditions = conditions;
    }


    /**
     * Default ctor with ANY value.
     */
    public WeatherDefinition(){

        this.conditions = new int[] {CONDITION_ANY};
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getTemperature() {
        return temperature;
    }



    public float getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }


    public float getDewPoint() {
        return dewPoint;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setFeelsLikeTemperature(float feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public void setDewPoint(float dewPoint) {
        this.dewPoint = dewPoint;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setConditions(int[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public float getTemperature(int i) {
        return temperature;
    }

    @Override
    public float getFeelsLikeTemperature(int i) {
        return feelsLikeTemperature;
    }

    @Override
    public float getDewPoint(int i) {
        return dewPoint;
    }

    @Override
    public int getHumidity() {
        return humidity;
    }

    @Override
    public int[] getConditions() {
        return conditions;
    }




    public WeatherDefinition addCondition(int condition){
        if(!isDirty){
            isDirty = true;
            this.conditions = new int[0];
        }

        conditions = Arrays.copyOf(conditions, conditions.length + 1);
        conditions[conditions.length -1] = condition;
        return this;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        if(currentContext == null) return 0;

        Weather currentWeather = currentContext.getWeather();
        if (currentWeather == null) {
            return 0;
        }
        //return 0 if different types

        //check every field and compare each other
        //TODO: compare with ALL fields.
        //HACK: only comparing the weather conditions;
        /**
         * CALCULATIONS: getConditions() will return an array with the best combination of the
         * current weather conditions.
         */

        FloatStatistics sum = new FloatStatistics();
        for(int otherCondition : currentWeather.getConditions()){
            for(int myCondition : conditions){
                sum.accept(CONDITIONS_MATRIX[myCondition][otherCondition]);
            }
        }
        float avg = sum.getAverage();

        return avg > 1 ? 1 : avg;
    }
}
