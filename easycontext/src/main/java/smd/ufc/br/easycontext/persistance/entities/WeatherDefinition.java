package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.icu.math.MathContext;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;

import com.google.android.gms.awareness.state.Weather;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.math.FloatStatistics;
import smd.ufc.br.easycontext.persistance.typeconverters.IntegerArrayConverter;

@Entity
public class WeatherDefinition implements Weather, ContextDefinition  {



    // --------------------- FIELDS ------------------------ //
    @PrimaryKey
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
            {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION UNKNOWN 0
            {0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION CLEAR   1
            {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f}, //CONDITION CLOUDY  2
            {0.0f, 0.0f, 0.0f, 1.0f, 0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION FOGGY   3
            {0.0f, 0.0f, 0.0f, 0.4f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, //CONDITION HAZY    4
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.6f, 0.0f, 0.0f}, //CONDITION ICY     5
            {0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f}, //CONDITION RAINY   6
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6f, 0.0f, 1.0f, 0.0f, 0.0f}, //CONDITION SNOWY   7
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.3f}, //CONDITION STORMY  8
            {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3f, 1.0f}, //CONDITION WINDY   9
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


    public WeatherDefinition(){
        this.conditions = new int[0];
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

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(float feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public float getDewPoint() {
        return dewPoint;
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

    public float compareWeatherConditions(int[] otherConditions) {
        /**
         * CALCULATIONS: getConditions() will return an array with the best combination of the
         * current weather conditions. We will prioritize the length of the previously defined weather
         * to damper the value of conditions we get right.
         */
        float calculationDamper = Math.max(otherConditions.length, conditions.length); // using the number of conditions as a damper.

        FloatStatistics sum = new FloatStatistics();
        for(int otherCondition : otherConditions ){
            float tempSum = 0.0f;
            for (int myCondition : conditions) {
                tempSum += CONDITIONS_MATRIX[myCondition][otherCondition];
            }
            sum.accept(ContextDefinition.Maths.clamp(tempSum, 1.0f));
        }
        return ContextDefinition.Maths.clamp(sum.getAverage(), 1.0f);
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


    public void addCondition(int condition){

        conditions = Arrays.copyOf(conditions, conditions.length + 1);
        conditions[conditions.length -1] = condition;
    }

    @Override
    public float calculateConfidence(CurrentContext currentContext) {
        Weather other = currentContext.getWeather();
        if (other == null) {
            return 0;
        }
        //return 0 if different types

        //check every field and compare each other
        //TODO: compare with ALL fields.
        //HACK: only comparing the weather conditions;
        float conditions = compareWeatherConditions((other.getConditions()));


        return conditions;
    }
}
