package smd.ufc.br.easycontext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CurrentContext is the current context that the device is at.
 */
public class CurrentContext implements Serializable {




    private Weather weather;
    private TimeIntervals timeIntervals;
    private Location location;
    private PlaceLikelihood placeLikelihood;
    private DetectedActivity detectedActivity;

    CurrentContext setWeather(Weather weather) {
        this.weather = weather;
        return this;
    }

    CurrentContext setTimeIntervals(TimeIntervals timeIntervals) {
        this.timeIntervals = timeIntervals;
        return this;
    }

    CurrentContext setLocation(Location location) {
        this.location = location;
        return this;
    }

    CurrentContext setPlaceLikelihood(PlaceLikelihood placeLikelihood) {
        this.placeLikelihood = placeLikelihood;
        return this;
    }

    CurrentContext setDetectedActivity(DetectedActivity detectedActivity) {
        this.detectedActivity = detectedActivity;
        return this;
    }

    public Weather getWeather() {
        return weather;
    }

    public TimeIntervals getTimeIntervals() {
        return timeIntervals;
    }

    public Location getLocation() {
        return location;
    }

    public PlaceLikelihood getPlaceLikelihood() {
        return placeLikelihood;
    }

    public DetectedActivity getDetectedActivity() {
        return detectedActivity;
    }

    CurrentContext() {
        weather = null;
        timeIntervals = null;
        location = null;
        placeLikelihood = null;
        detectedActivity = null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Weather: ");
        if(weather == null)
            builder.append("null");
        else
            builder.append(weather.toString());

        builder.append("\nTime Intervals: ");

        if (timeIntervals == null) {
            builder.append("null");
        } else {
            builder.append(timeIntervals.toString());
        }
        builder.append("\nLocation: ");

        if (location == null) {
            builder.append("null");
        } else {
            builder.append(location.toString());
        }
        builder.append("\nDetected Activity: ");

        if (detectedActivity == null) {
            builder.append("null");
        } else {
            builder.append(detectedActivity.toString());
        }
        builder.append("\nPlace Likelihood: ");

        if (placeLikelihood == null) {
            builder.append("null");
        } else {
            builder.append(placeLikelihood.toString());
        }
        return builder.toString();
    }
}
