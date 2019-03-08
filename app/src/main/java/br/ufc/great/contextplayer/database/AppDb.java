package br.ufc.great.contextplayer.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;

import static br.ufc.great.contextplayer.Main2Activity.DB_NAME;

@Database(entities = {PlaylistContextJoin.class}, version = 6)
public abstract class AppDb extends RoomDatabase {
    private static AppDb instance;

    static Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

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
