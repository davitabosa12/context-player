package br.ufc.great.contextplayer.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;

import br.ufc.great.contextplayer.R;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;

public class PickActivitiesDialog extends DialogFragment implements DialogInterface.OnMultiChoiceClickListener {

    SparseBooleanArray mItems;
    OnActivitiesPick mListener;
    DetectedActivityDefinition definition;

    public PickActivitiesDialog(){

    }



    public static DialogFragment newInstance() {
        return new PickTimeIntervalDialog();
    }


    @Override
    public void onAttach(Context context) {
        if(context instanceof OnActivitiesPick){
            super.onAttach(context);
            mListener = (OnActivitiesPick) context;
            mItems = new SparseBooleanArray();
            definition = new DetectedActivityDefinition();

        } else {
            throw new RuntimeException(context.getPackageName() + "must implement OnActivitiesPick");

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Time");
        String[] intervals = getContext().getResources().getStringArray(R.array.time_intervals);
        //initialize mItems
        for(int i = 0; i < intervals.length; i++){
            mItems.put(i, false);
        }
        builder.setMultiChoiceItems(R.array.time_intervals, null, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                definition = new DetectedActivityDefinition();
                for(int index = 0; index < mItems.size(); index++){
                    if(mItems.valueAt(index)){
                        definition.addActivityTypes(index + 1);
                    }
                }
                mListener.onActivitiesPick(definition);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("PickTimeIntervalDialog", "onClick: canceled");

                    }
                });
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
        mItems.put(i, b);
    }

    public interface OnActivitiesPick {
        void onActivitiesPick(DetectedActivityDefinition activityDefinition);
    }
}
