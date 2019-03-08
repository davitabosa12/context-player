/*
package smd.ufc.br.easycontext.persistance.databases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import smd.ufc.br.easycontext.persistance.dao.DetectedActivityDAO;
import smd.ufc.br.easycontext.persistance.dao.LocationDAO;
import smd.ufc.br.easycontext.persistance.dao.TimeIntervalDAO;
import smd.ufc.br.easycontext.persistance.dao.WeatherDefinitionDAO;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

@Deprecated
@Database(entities = {DetectedActivityDefinition.class, WeatherDefinition.class, TimeIntervalDefinition.class, LocationDefinition.class},
        version = 1, exportSchema = true)
public abstract class EasyContextDatabase extends RoomDatabase {

    private static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            //DROP ALL TABLES
//            database.execSQL("DROP TABLE IF EXISTS `DetectedActivityDefinition`");
//            database.execSQL("DROP TABLE IF EXISTS `WeatherDefinition`");
//            database.execSQL("DROP TABLE IF EXISTS `TimeIntervalDefinition`");
//            database.execSQL("DROP TABLE IF EXISTS `LocationDefinition`");
//
//            //RECREATE
//            database.execSQL("CREATE TABLE IF NOT EXISTS `DetectedActivityDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activityType` INTEGER NOT NULL)");
//            database.execSQL("CREATE TABLE IF NOT EXISTS `WeatherDefinition` (`uid` INTEGER NOT NULL, `temperature` REAL NOT NULL, `feelsLikeTemperature` REAL NOT NULL, `dewPoint` REAL NOT NULL, `humidity` INTEGER NOT NULL, `conditions` TEXT, PRIMARY KEY(`uid`))");
//            database.execSQL("CREATE TABLE IF NOT EXISTS `TimeIntervalDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeIntervals` TEXT)");
//            database.execSQL("CREATE TABLE IF NOT EXISTS `LocationDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `maxDistance` REAL NOT NULL)");
//            database.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
//            database.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"81c0ca7fada2e0d7b4371ace677cc52d\")");
        }
    };
    private static EasyContextDatabase instance;


    public abstract DetectedActivityDAO detectedActivityDAO();
    public abstract WeatherDefinitionDAO weatherDefinitionDAO();
    public abstract TimeIntervalDAO timeIntervalDAO();
    public abstract LocationDAO locationDAO();
    
    static String TAG = "EasyContextDatabase";

    //private EasyContextDatabase(){

    //}

    public static EasyContextDatabase getInstance(Context context, final String dbName) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, EasyContextDatabase.class, dbName).allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}*/