package br.ufc.great.contextplayer.model.join;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;



@Dao
public interface PlaylistContextJoinDAO {

    @Query("SELECT * FROM playlistcontextjoin WHERE playlistId == :playlistId")
    List<PlaylistContextJoin> getPlaylistContextPair(long playlistId);

    @Insert
    long insert(PlaylistContextJoin join);

    @Query("DELETE FROM playlistcontextjoin WHERE playlistId == :playlistId")
    int deleteAll(long playlistId);

    @Delete
    void delete(PlaylistContextJoin join);
}
