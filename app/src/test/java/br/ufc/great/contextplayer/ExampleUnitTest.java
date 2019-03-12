package br.ufc.great.contextplayer;

import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.PlaylistContexts;
import smd.ufc.br.easycontext.ContextDefinition;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.persistance.entities.DetectedActivityDefinition;
import smd.ufc.br.easycontext.persistance.entities.LocationDefinition;
import smd.ufc.br.easycontext.persistance.entities.TimeIntervalDefinition;
import smd.ufc.br.easycontext.persistance.entities.WeatherDefinition;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void confidenceTest(){
        /*current context = Morning, weekday, rainy */
//        WeatherDefinition weather = new WeatherDefinition();
//        weather.addCondition(Weather.CONDITION_RAINY);
//
//        DetectedActivityDefinition detectedActivity = null;
//        LocationDefinition locationDefinition = null;
//
//        TimeIntervalDefinition timeInterval = new TimeIntervalDefinition();
//        timeInterval.addTimeInterval(TimeFence.TIME_INTERVAL_WEEKDAY);
//        timeInterval.addTimeInterval(TimeFence.TIME_INTERVAL_MORNING);
//
//        CurrentContext morningRainyWeekday = CurrentContext.generateMockCurrentContext(weather, timeInterval, locationDefinition, null, detectedActivity);
//
//
//        Playlist noite, dia;
//        List<ContextDefinition> noiteDefinitions, diaDefinitions;
//        noiteDefinitions = new ArrayList<>();
//        diaDefinitions = new ArrayList<>();
//        noiteDefinitions.add(new TimeIntervalDefinition().addTimeInterval(TimeFence.TIME_INTERVAL_NIGHT));
//        noiteDefinitions.add(new WeatherDefinition().addCondition(Weather.CONDITION_UNKNOWN));
//        noite = new Playlist();
//        noite.setDefinitions(new PlaylistContexts().setDefinitions(noiteDefinitions));
    }
}