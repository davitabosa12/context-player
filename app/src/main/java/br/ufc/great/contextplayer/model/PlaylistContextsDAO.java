package br.ufc.great.contextplayer.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface PlaylistContextsDAO {

    @Query("SELECT * FROM playlistcontexts WHERE playlistId == :playlistId")
    PlaylistContexts getByPlaylistId(long playlistId);

    @Insert
    void insert(PlaylistContexts definitions);

    @Delete
    void delete(PlaylistContexts definitions);

}
