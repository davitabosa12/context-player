package smd.ufc.br.easycontext.persistance.databases;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import smd.ufc.br.easycontext.persistance.dao.DetectedActivityDAO;
import smd.ufc.br.easycontext.persistance.dao.LocationDAO;
import smd.ufc.br.easycontext.persistance.dao.TimeIntervalDAO;
import smd.ufc.br.easycontext.persistance.dao.WeatherDefinitionDAO;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;


@Database(entities = {WeatherDefinition.class, DetectedActivityDefinition.class, TimeIntervalDefinition.class, LocationDefinition.class},
        version = 6, exportSchema = true)
public abstract class ContextDb extends RoomDatabase {

    static String TAG = "ContextDb";

    private static ContextDb instance;
    public abstract WeatherDefinitionDAO weatherDAO();
    public abstract DetectedActivityDAO detectedActivityDAO();
    public abstract TimeIntervalDAO timeIntervalDAO();
    public abstract LocationDAO locationDAO();

    static Migration MIGRATION_6_5 = new Migration(6,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `WeatherDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `temperature` REAL NOT NULL, `feelsLikeTemperature` REAL NOT NULL, `dewPoint` REAL NOT NULL, `humidity` INTEGER NOT NULL, `conditions` TEXT, PRIMARY KEY(`uid`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `DetectedActivityDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activityTypes` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `TimeIntervalDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeIntervals` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `LocationDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `maxDistance` REAL NOT NULL)");
        }
    };
    static Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("CREATE TABLE IF NOT EXISTS `WeatherDefinition` (`uid` INTEGER NOT NULL, `temperature` REAL NOT NULL, `feelsLikeTemperature` REAL NOT NULL, `dewPoint` REAL NOT NULL, `humidity` INTEGER NOT NULL, `conditions` TEXT, PRIMARY KEY(`uid`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `DetectedActivityDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activityTypes` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `TimeIntervalDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeIntervals` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `LocationDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `maxDistance` REAL NOT NULL)");
        }
    };
    static Migration MIGRATION_7_6 = new Migration(7,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {


            database.execSQL("CREATE TABLE IF NOT EXISTS `WeatherDefinition` (`uid` INTEGER NOT NULL, `temperature` REAL NOT NULL, `feelsLikeTemperature` REAL NOT NULL, `dewPoint` REAL NOT NULL, `humidity` INTEGER NOT NULL, `conditions` TEXT, PRIMARY KEY(`uid`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `DetectedActivityDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `activityTypes` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `TimeIntervalDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeIntervals` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `LocationDefinition` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `maxDistance` REAL NOT NULL)");
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
                    .addMigrations(MIGRATION_6_5, MIGRATION_5_6, MIGRATION_7_6)
                    .build();

        }



        return instance;
    }
}
