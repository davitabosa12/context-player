package smd.ufc.br.easycontext.persistance.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM locationdefinition WHERE uid == :id")
    LocationDefinition getById(long id);

    @Insert
    long insert(LocationDefinition definition);

    @Delete
    void delete(LocationDefinition definition);

}
