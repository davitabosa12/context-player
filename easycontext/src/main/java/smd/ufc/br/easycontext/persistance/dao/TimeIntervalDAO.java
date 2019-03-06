package smd.ufc.br.easycontext.persistance.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;

@Dao
public interface TimeIntervalDAO {

    @Query("SELECT * FROM timeintervaldefinition WHERE uid == :id")
    TimeIntervalDefinition getById(long id);

    @Insert
    long insert(TimeIntervalDefinition definition);

    @Delete
    void delete(TimeIntervalDefinition definition);
}
