package br.ufc.great.contextplayer.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;

import static br.ufc.great.contextplayer.Main2Activity.DB_NAME;

@Database(entities = {PlaylistContextJoin.class}, version = 7)
public abstract class AppDb extends RoomDatabase {
    private static AppDb instance;

    static Migration MIGRATION_5_6 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `PlaylistContextJoin` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `playlistId` INTEGER NOT NULL, `timeIntervalId` INTEGER NOT NULL, `weatherId` INTEGER NOT NULL, `locationId` INTEGER NOT NULL, `activityId` INTEGER NOT NULL)");

        }
    };

    public abstract PlaylistContextJoinDAO playlistContextJoinDAO();

    public static AppDb getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDb.class, DB_NAME).allowMainThreadQueries()
                    .addMigrations(MIGRATION_5_6)
                    .build();

        }

        return instance;
    }
}
