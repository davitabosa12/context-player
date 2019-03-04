package br.ufc.great.contextplayer.fragments.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.R;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link PickWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickWeatherFragment extends DialogFragment implements DialogInterface.OnMultiChoiceClickListener {

    public static final String NAME = "PickWeatherFragment";

    SparseBooleanArray mItems;
    OnWeatherPick mListener;
    WeatherDefinition definition;
    public PickWeatherFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PickWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PickWeatherFragment newInstance() {
        PickWeatherFragment fragment = new PickWeatherFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pick_weather, container, false);
//    }

    @Override
    public void onAttach(Context context) {
        if(context instanceof OnWeatherPick){
            super.onAttach(context);
            mListener = (OnWeatherPick) context;
            mItems  = new SparseBooleanArray();
        } else {
            throw new RuntimeException(context.getPackageName() + "must implement OnWeatherPick");
        }

    }

    @Override
    public void onDetach() {
        if (mListener != null) {
            mListener = null;
        }
        super.onDetach();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Weather");
        String[] conditions = getResources().getStringArray(R.array.weather_conditions);
        builder.setMultiChoiceItems(conditions, null, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                definition = new WeatherDefinition();
                for(int index = 0; index < mItems.size(); index++){
                    if(mItems.valueAt(index)){
                        definition.addCondition(index + 1); //value 0 is reserved for UNKNOWN
                    }
                }
                mListener.onWeatherPick(definition);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(NAME, "onClick: canceled");
            }
        });

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        mItems.put(i, b);
    }

    public interface OnWeatherPick {
        void onWeatherPick(WeatherDefinition weatherDefinition);
    }
}
