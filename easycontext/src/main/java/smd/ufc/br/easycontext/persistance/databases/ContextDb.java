package smd.ufc.br.easycontext.persistance.databases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomMasterTable;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import smd.ufc.br.easycontext.persistance.dao.DetectedActivityDAO;
import smd.ufc.br.easycontext.persistance.dao.LocationDAO;
import smd.ufc.br.easycontext.persistance.dao.TimeIntervalDAO;
import smd.ufc.br.easycontext.persistance.dao.WeatherDefinitionDAO;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;


@Database(entities = {WeatherDefinition.class, DetectedActivityDefinition.class, TimeIntervalDefinition.class, LocationDefinition.class},
        version = 5, exportSchema = true)
public abstract class ContextDb extends RoomDatabase {

    static String TAG = "ContextDb";

    private static ContextDb instance;
    public abstract WeatherDefinitionDAO weatherDAO();
    public abstract DetectedActivityDAO detectedActivityDAO();
    public abstract TimeIntervalDAO timeIntervalDAO();
    public abstract LocationDAO locationDAO();

    static Migration MIGRATION_5_6 = new Migration(6,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static ContextDb getInstance(Context applicationContext, String dbName) {
        if (instance == null) {
            instance = Room.databaseBuilder(applicationContext, ContextDb.class, dbName).allowMainThreadQueries()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);

                        }
                    })
                    .addMigrations(MIGRATION_5_6)
                    .build();

        }



        return instance;
    }
}
