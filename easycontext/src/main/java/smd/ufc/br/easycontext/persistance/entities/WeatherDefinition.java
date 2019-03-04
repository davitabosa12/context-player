package smd.ufc.br.easycontext.persistance.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.Weather;

import java.util.Arrays;

import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;

@Entity
public class WeatherDefinition implements Weather, ContextDefinition  {

    // --------------------- FIELDS ------------------------ //
    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "temperature")
    private float _temperature;

    @ColumnInfo(name = "feels_like_temperature")
    private float _feelsLikeTemperature;

    @ColumnInfo(name = "dew_point")
    private float _dewPoint;

    @ColumnInfo(name = "humidity")
    private int _humidity;

    @ColumnInfo(name = "conditions")
    private int[] _conditions;

    // -------------------- LOOK-UP MATRICES ---------------- //
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

    public WeatherDefinition(@Nullable float temperature,
                             @Nullable float feelsLikeTemperature,
                             @Nullable float dewPoint,
                             @Nullable int humidity,
                             @Nullable int[] conditions) {
        this._temperature = temperature;
        this._feelsLikeTemperature = feelsLikeTemperature;
        this._dewPoint = dewPoint;
        this._humidity = humidity;
        this._conditions = conditions;
    }

    public WeatherDefinition(){
        this._conditions = new int[0];
    }



    private float compareWeatherConditions(int[] otherConditions) {
        /**
         * CALCULATIONS: getConditions() will return an array with the best combination of the
         * current weather conditions. We will prioritize the length of the previously defined weather
         * to damper the value of conditions we get right.
         */
        float calculationDamper = _conditions.length + 0.0f; // using the number of conditions as a damper.
        float sum = 0.0f;
        for(int otherCondition : otherConditions ){
            float tempSum = 0.0f;
            for (int myCondition : _conditions) {
                tempSum += CONDITIONS_MATRIX[myCondition][otherCondition];
            }
            sum += ContextDefinition.Maths.clamp(tempSum, 1.0f);
        }
        return ContextDefinition.Maths.clamp(sum/calculationDamper, 1.0f);
    }

    @Override
    public float getTemperature(int i) {
        return _temperature;
    }

    @Override
    public float getFeelsLikeTemperature(int i) {
        return _feelsLikeTemperature;
    }

    @Override
    public float getDewPoint(int i) {
        return _dewPoint;
    }

    @Override
    public int getHumidity() {
        return _humidity;
    }

    @Override
    public int[] getConditions() {
        return _conditions;
    }

    public void setTemperature(float _temperature) {
        this._temperature = _temperature;
    }

    public void setFeelsLikeTemperature(float _feelsLikeTemperature) {
        this._feelsLikeTemperature = _feelsLikeTemperature;
    }

    public void setDewPoint(float _dewPoint) {
        this._dewPoint = _dewPoint;
    }

    public void setHumidity(int _humidity) {
        this._humidity = _humidity;
    }

    public void setConditions(int ...conditions) {
        this._conditions = conditions;
    }
    public void addCondition(int condition){

        _conditions = Arrays.copyOf(_conditions, _conditions.length + 1);
        _conditions[_conditions.length -1] = condition;
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
