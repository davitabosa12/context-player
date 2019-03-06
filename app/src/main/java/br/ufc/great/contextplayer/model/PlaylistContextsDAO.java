package br.ufc.great.contextplayer.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.database.ApplicationDb;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoin;
import br.ufc.great.contextplayer.model.join.PlaylistContextJoinDAO;
import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.persistance.dao.DetectedActivityDAO;
import smd.ufc.br.easycontext.persistance.dao.LocationDAO;
import smd.ufc.br.easycontext.persistance.dao.TimeIntervalDAO;
import smd.ufc.br.easycontext.persistance.dao.WeatherDefinitionDAO;
import smd.ufc.br.easycontext.persistance.databases.EasyContextDatabase;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;


public class PlaylistContextsDAO {


    public static final String TAG = "PlaylistContextsDAO";

    Context context;
    PlaylistContextJoinDAO joinDAO;
    TimeIntervalDAO timeIntervalDAO;
    WeatherDefinitionDAO weatherDAO;
    DetectedActivityDAO activityDAO;
    LocationDAO locationDAO;

    public PlaylistContextsDAO(Context context){
        this.context = context;
        joinDAO = ApplicationDb.getInstance(context).playlistContextJoinDAO();
        EasyContextDatabase database = EasyContextDatabase.getInstance(context, ApplicationDb.DB_NAME);
        timeIntervalDAO = database.timeIntervalDAO();
        weatherDAO = database.weatherDefinitionDAO();
        activityDAO = database.detectedActivityDAO();

        locationDAO = database.locationDAO();

    }


    public PlaylistContexts getByPlaylistId(long playlistId) {
        ArrayList<ContextDefinition> definitions = new ArrayList<>();
        List<PlaylistContextJoin> list = joinDAO.getPlaylistContextPair(playlistId);
        for (PlaylistContextJoin p : list){ //for each context join
            //get each individual context and save to a list.
            if(idValid(p.getTimeIntervalId())){
                definitions.add(timeIntervalDAO.getById(p.getTimeIntervalId()));
            }
            if(idValid(p.getWeatherId())){
                definitions.add(weatherDAO.getById(p.getWeatherId()));
            }
            if(idValid(p.getActivityId())){
                definitions.add(activityDAO.getById(p.getActivityId()));
            }
            if (idValid(p.getLocationId())){
                definitions.add(locationDAO.getById(p.getLocationId()));
            }
        }
        //save new created list to a packet object
        PlaylistContexts pc = new PlaylistContexts();
        pc.setDefinitions(definitions);
        return pc;
    }



    private boolean idValid(long id){
        return (id >= 0);
    }

    public long update(long playlistId, PlaylistContexts definitions) {
        PlaylistContextJoin join = new PlaylistContextJoin();
        join.setPlaylistId(playlistId);
        //delete all records from this playlistID
        joinDAO.deleteAll(playlistId);

        //insert
        for(ContextDefinition definition : definitions.getDefinitions()){
            if(definition instanceof TimeIntervalDefinition){
                long id = timeIntervalDAO.insert((TimeIntervalDefinition) definition);
                join.setTimeIntervalId(id);
            } else if(definition instanceof WeatherDefinition){
                long id = weatherDAO.insert((WeatherDefinition) definition);
                join.setWeatherId(id);
            } else if(definition instanceof LocationDefinition){
                long id = locationDAO.insert((LocationDefinition) definition);
                join.setLocationId(id);
            } else if(definition instanceof DetectedActivityDefinition){

                long id = activityDAO.insert((DetectedActivityDefinition) definition);
                join.setActivityId(id);
            }
        }

        //save to join db



        return joinDAO.insert(join);
    }

    public long delete(long playlistId){
        return joinDAO.deleteAll(playlistId);
    }
}
