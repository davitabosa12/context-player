package br.ufc.great.contextplayer;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.fragments.dialog.PickTimeIntervalDialog;
import br.ufc.great.contextplayer.fragments.dialog.PickWeatherFragment;
import br.ufc.great.contextplayer.model.PlaylistContexts;
import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

public class SelectContextActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        PickWeatherFragment.OnWeatherPick, PickTimeIntervalDialog.OnTimeIntervalPick, View.OnClickListener {

    ListView lvProviders;
    String[] mProviders;
    PlaylistContexts mContexts;
    Button btnUpdate, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_context);
        lvProviders = findViewById(R.id.lv_providers);
        lvProviders.setOnItemClickListener(this);
        mContexts = new PlaylistContexts();
        mProviders = getResources().getStringArray(R.array.providers);
        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment fragment;
        switch (i){
            case 1: //Weather
                fragment = PickWeatherFragment.newInstance();
                fragment.show(ft, "weather_dialog");
                break;
            case 2: // Activities
                Toast.makeText(this, "Activities not supported yet", Toast.LENGTH_SHORT).show();
                break;
            case 3: // Time of day
                fragment = PickTimeIntervalDialog.newInstance();
                fragment.show(ft, "ti_dialog");
                break;
            case 4: //Location
                Toast.makeText(this, "Location not supported yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onTimeIntervalPick(TimeIntervalDefinition timeInterval) {
        List<ContextDefinition> defs = mContexts.getDefinitions();
        if (defs == null) {
            defs = new ArrayList<>();
        }
        defs.add(timeInterval);
        mContexts.setDefinitions(defs);
    }

    @Override
    public void onWeatherPick(WeatherDefinition weatherDefinition) {
        List<ContextDefinition> defs = mContexts.getDefinitions();
        if (defs == null) {
            defs = new ArrayList<>();
        }
        defs.add(weatherDefinition);
        mContexts.setDefinitions(defs);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_update:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                for(ContextDefinition definition : mContexts.getDefinitions()){
                    if(definition instanceof WeatherDefinition){
                        bundle.putSerializable("weather", definition);
                    } else if(definition instanceof TimeIntervalDefinition){
                        bundle.putSerializable("time_interval", definition);
                    } else if(definition instanceof LocationDefinition){
                        bundle.putSerializable("location", definition);
                    } else if(definition instanceof DetectedActivityDefinition){
                        bundle.putSerializable("detected_activity", definition);
                    } else {
                        Log.w("SelectContextActivity", "Selected context is not mapped");
                    }
                }
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);

                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);

                break;
        }
        finish();
    }
}
