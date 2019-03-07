package br.ufc.great.contextplayer.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import br.ufc.great.contextplayer.model.PlaylistContexts;
import br.ufc.great.contextplayer.model.PlaylistContextsDAO;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;

@Database(entities = {PlaylistContextJoin.class}, version = 1)
public abstract class ApplicationDb extends RoomDatabase {
    private static ApplicationDb instance;
    public static final String DB_NAME = "new_db";

    public abstract PlaylistContextJoinDAO playlistContextJoinDAO();

    public static ApplicationDb getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, ApplicationDb.class, DB_NAME).allowMainThreadQueries().addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            }).build();
        }
        return instance;
    }
}
