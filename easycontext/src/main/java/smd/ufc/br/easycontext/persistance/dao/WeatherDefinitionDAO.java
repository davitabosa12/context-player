package smd.ufc.br.easycontext.persistance.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

@Dao
public interface WeatherDefinitionDAO {


    @Query("SELECT * FROM weatherdefinition WHERE uid == :id")
    WeatherDefinition getById(long id);

    @Insert
    long insert(WeatherDefinition definition);

    @Delete
    void delete(WeatherDefinition definition);

}
