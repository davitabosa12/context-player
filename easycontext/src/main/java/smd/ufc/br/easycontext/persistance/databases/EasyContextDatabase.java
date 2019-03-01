package smd.ufc.br.easycontext.persistance.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import smd.ufc.br.easycontext.persistance.dao.DetectedActivityDAO;
import smd.ufc.br.easycontext.persistance.dao.TimeIntervalDAO;
import smd.ufc.br.easycontext.persistance.dao.WeatherDefinitionDAO;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

@Database(entities = {WeatherDefinition.class, TimeIntervalDAO.class, DetectedActivityDAO.class}, version = 1)
public abstract class EasyContextDatabase extends RoomDatabase {

    private static EasyContextDatabase instance;


    public abstract WeatherDefinitionDAO weatherDefinitionDAO();
    public abstract TimeIntervalDAO timeIntervalDAO();
    public abstract DetectedActivityDAO detectedActivityDAO();

    public static EasyContextDatabase getInstance(Context context, String dbName) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, EasyContextDatabase.class, dbName).build();
        }

        return instance;
    }

}
