package smd.ufc.br.easycontext.persistance.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;

import smd.ufc.br.easycontext.persistance.dao.DetectedActivityDAO;
import smd.ufc.br.easycontext.persistance.dao.LocationDAO;
import smd.ufc.br.easycontext.persistance.dao.TimeIntervalDAO;
import smd.ufc.br.easycontext.persistance.dao.WeatherDefinitionDAO;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

@Database(entities = {DetectedActivityDefinition.class, WeatherDefinition.class, TimeIntervalDefinition.class, LocationDefinition.class},
        version = 2, exportSchema = false)
public abstract class EasyContextDatabase extends RoomDatabase {

    private static EasyContextDatabase instance;


    public abstract DetectedActivityDAO detectedActivityDAO();
    public abstract WeatherDefinitionDAO weatherDefinitionDAO();
    public abstract TimeIntervalDAO timeIntervalDAO();
    public abstract LocationDAO locationDAO();

    //private EasyContextDatabase(){

    //}

    public static EasyContextDatabase getInstance(Context context, String dbName) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, EasyContextDatabase.class, dbName).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }

        return instance;
    }

}
