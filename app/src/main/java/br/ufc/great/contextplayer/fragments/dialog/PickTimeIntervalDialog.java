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
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

public class PickTimeIntervalDialog extends DialogFragment implements DialogInterface.OnMultiChoiceClickListener {

    SparseBooleanArray mItems;
    OnTimeIntervalPick mListener;
    TimeIntervalDefinition definition;

    public PickTimeIntervalDialog(){

    }

    public static DialogFragment newInstance() {
        return new PickTimeIntervalDialog();
    }


    @Override
    public void onAttach(Context context) {
        if(context instanceof OnTimeIntervalPick){
            super.onAttach(context);
            mListener = (OnTimeIntervalPick) context;
            mItems = new SparseBooleanArray();
            definition = new TimeIntervalDefinition();

        } else {
            throw new RuntimeException(context.getPackageName() + "must implement OnTimeIntervalPick");

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Time");
        builder.setMultiChoiceItems(R.array.time_intervals, null, this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                definition = new TimeIntervalDefinition();
                for(int index = 0; index < mItems.size(); index++){
                    if(mItems.valueAt(index)){
                        definition.addTimeInterval(index);
                    }
                }
                mListener.onTimeIntervalPick(definition);
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

    public interface OnTimeIntervalPick {
        void onTimeIntervalPick(TimeIntervalDefinition timeInterval);
    }
}
