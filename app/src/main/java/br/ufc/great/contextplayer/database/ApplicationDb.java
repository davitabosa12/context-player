package br.ufc.great.contextplayer.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufc.great.contextplayer.model.PlaylistContexts;
import br.ufc.great.contextplayer.model.PlaylistContextsDAO;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;

@Database(entities = {PlaylistContexts.class}, version = 1)
public abstract class ApplicationDb extends RoomDatabase {
    private static ApplicationDb instance;
    public static final String DB_NAME = "context_player";

    public abstract PlaylistContextsDAO playlistContextsDAO();
    public abstract PlaylistContextJoinDAO playlistContextJoinDAO();

    private ApplicationDb(){

    }

    public static ApplicationDb getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ApplicationDb.class, DB_NAME).build();
        }
        return instance;
    }
}
