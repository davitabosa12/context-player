package smd.ufc.br.easycontext.persistance.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;

@Dao
public interface DetectedActivityDAO {

    @Query("SELECT * FROM detectedactivitydefinition WHERE uid == :id")
    DetectedActivityDefinition getById(int id);

    @Insert
    void insert(DetectedActivityDefinition definition);

    @Delete
    void delete(DetectedActivityDefinition definition);
}
