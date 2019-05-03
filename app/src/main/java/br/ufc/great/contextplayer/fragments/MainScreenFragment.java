package br.ufc.great.contextplayer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.List;

import br.ufc.great.contextplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment {


    private static String TAG = "MainScreenFragment";
    private TextView txvContext;
    SnapshotClient snapshot;
    WeatherResponse weatherResponse;
    DetectedActivityResponse daResponse;
    LocationResponse locationResponse;
    TimeIntervalsResponse timeIntervalsResponse;


    public MainScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainScreenFragment newInstance() {
        MainScreenFragment fragment = new MainScreenFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snapshot = Awareness.getSnapshotClient(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_main_screen, container, false);
        txvContext = rootview.findViewById(R.id.txv_context);
        updateContext();
        return rootview;
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {

        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void updateContext() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task weatherTask = snapshot.getWeather().addOnSuccessListener(new OnSuccessListener<WeatherResponse>() {
            @Override
            public void onSuccess(WeatherResponse response) {
                 weatherResponse = response;
            }
        });


        Task detectedActivityTask = snapshot.getDetectedActivity().addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
            @Override
            public void onSuccess(DetectedActivityResponse response) {
                daResponse = response;
            }
        });

        Task locationTask = snapshot.getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
            @Override
            public void onSuccess(LocationResponse response) {
                locationResponse = response;
            }
        });
        Task timeIntervalsTask = snapshot.getTimeIntervals().addOnSuccessListener(new OnSuccessListener<TimeIntervalsResponse>() {
            @Override
            public void onSuccess(TimeIntervalsResponse response) {
                timeIntervalsResponse = response;
            }
        });

        Tasks.whenAllComplete(weatherTask, detectedActivityTask, locationTask, timeIntervalsTask)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        Log.d(TAG, "onComplete: update completed");
                        afterUpdate();
                    }
                });
    }

    private void afterUpdate(){
        Log.d(TAG, "afterUpdate: called");
        StringBuilder sb = new StringBuilder();
        sb.append("Weather: \n").append(weatherResponse.getWeather().toString());
        sb.append("\nDetected activity: \n").append(daResponse.getActivityRecognitionResult().getMostProbableActivity().toString());
        sb.append("\nLocation:\n").append(locationResponse.getLocation().toString());
        sb.append("\nTime intervals:\n").append(timeIntervalsResponse.getTimeIntervals().toString());
        txvContext.setText(sb.toString());
    }

}