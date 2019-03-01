package smd.ufc.br.easycontext.persistance.entities;

import android.location.Location;

import com.google.android.gms.awareness.snapshot.LocationResponse;

import smd.ufc.br.easycontext.ContextDefinition;

public class LocationDefinition extends ContextDefinition {

    Location location;

    @Override
    public float compareTo(ContextDefinition otherContext) {
        return 0;
    }
}
