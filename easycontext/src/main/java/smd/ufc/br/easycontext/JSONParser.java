package smd.ufc.br.easycontext;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davitabosa on 08/08/2018.
 */

public class JSONParser {
    private static final String TAG = "JSONParser";

    Context context;

    public JSONParser(Context context) {

        this.context = context;

    }

    public List<AwarenessActivity> readJSON() throws IOException {
        Gson g = new Gson();
        List<AwarenessActivity> activities = null;
        //Reader r = new FileReader(new File("res/configuration.json")); will throw FileNotFoundException
        InputStream is = context.getResources().openRawResource(context.getResources().getIdentifier("configuration",
                "raw", context.getPackageName()));
        Reader r = new InputStreamReader(is);


        JsonReader jsonReader = g.newJsonReader(r); //fazer um novo reader de json

        jsonReader.beginObject(); //espera um inicio de objeto
        String peekTagName = jsonReader.nextName();
        if(peekTagName.equals("activities")){
            //jsonReader.nextName();
            activities = parseActivitiesList(jsonReader);
        }

        Log.d("AwarenessHelper", "JSON lido..");
        Log.d(TAG, "readJSON: " + activities.toString());
        return activities;



        /*Fence headphoneFence = new Fence("headphoneFence",FenceType.HEADPHONE, new MyCustomAction());
        fences.add(headphoneFence);*/
    }
    //TODO: Criar parser externo
    private ArrayList<AwarenessActivity> parseActivitiesList(JsonReader jsonReader) throws IOException {
        ArrayList<AwarenessActivity> activitiesList = new ArrayList<AwarenessActivity>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            activitiesList.add(parseActivity(jsonReader));
        }
        jsonReader.endArray();
        return activitiesList;
    }


    private AwarenessActivity parseActivity(JsonReader jsonReader) throws IOException {
        Log.d(TAG, "parseActivity: parsing");
        jsonReader.beginObject();
        jsonReader.nextName(); // "name" tag
        String activityName = jsonReader.nextString();
        Log.d(TAG, "parseActivity: activityName = " +activityName);
        jsonReader.nextName(); // "packet" tag
        String activityPacket = jsonReader.nextString();
        Log.d(TAG, "parseActivity: activityPacket= " +activityPacket);
        jsonReader.nextName(); // "fences" tag
        ArrayList<Fence> fence = parseFenceList(jsonReader);
        //ler snapshots
        jsonReader.nextName(); // snapshots
        jsonReader.beginArray(); //[
        jsonReader.endArray(); // ]
        jsonReader.endObject();

        AwarenessActivity act = new AwarenessActivity(activityPacket + "." + activityName,fence);
        return act;

    }

    private ArrayList<Fence> parseFenceList(JsonReader jsonReader) throws IOException {
        Log.d(TAG, "parseFenceList: parsing");
        ArrayList<Fence> fencesList = new ArrayList<Fence>();
        jsonReader.beginArray(); //inicio "fences" array
        while(jsonReader.hasNext()){
            try {
                fencesList.add(parseFence(jsonReader));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        jsonReader.endArray();
        Log.d(TAG, "parseFenceList: finished parsing fence list");
        return fencesList;


    }

    private Fence parseFence(JsonReader jsonReader) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Log.d(TAG, "parseFence: parsing...");

        String fenceName = null, fenceAction = null;
        FenceType fenceType = null;
        FenceParameter params = null;
        String fenceMethod = null;
        FenceAction action = null;
        jsonReader.beginObject(); //inicio objeto fence
        while(jsonReader.hasNext()){
            String tag = jsonReader.nextName();
            switch(tag){
                case "fenceName":
                    fenceName = jsonReader.nextString();
                    Log.d(TAG, "parseFence: parsed Fence name: " + fenceName);
                    break;
                case "fenceAction":

                    fenceAction = jsonReader.nextString();
                    action = (FenceAction) Class.forName(fenceAction).newInstance();
                    if(action == null)
                        Log.e("AwarenessLib", "Action " + fenceAction + " not found in Action Map. Did you spell it correctly?");
                    Log.d(TAG, "parseFence: parsed action: " + fenceAction);
                    Log.d(TAG, "parseFence: the action: " + action.toString());
                    break;
                case "fenceMethod":
                    //fenceMethod = FenceMethod.valueOf(jsonReader.nextString());
                    String onJson = jsonReader.nextString();
                    String[] methodAndType = onJson.split("\\."); //thx regex
                    Log.d(TAG, "parseFence: " + methodAndType[0] + "  " + methodAndType[1]);
                    fenceType = FenceType.valueFor(methodAndType[0]);
                    Log.d(TAG, "parseFence: parsed type: " + fenceType);
                    fenceMethod = methodAndType[1];
                    Log.d(TAG, "parseFence: parsed method: " + fenceMethod);
                    break;
                case "fenceType":
                    /*
                    String type = jsonReader.nextString();
                    try{
                        fenceType = FenceType.valueOf(type);
                    } catch (IllegalArgumentException e){
                        Log.e("AwarenessLib", "FenceType not supported: " + type);
                        e.printStackTrace();
                    }*/
                    //IGNORE AND GET THE NUMBER
                    jsonReader.nextString();
                    break;
                case "params":
                    switch(fenceType){
                        case DETECTED_ACTIVITY:
                            params = parseDetectedActivityParams(jsonReader);
                            break;
                        case LOCATION:
                            params = parseLocationParams(jsonReader);
                            break;
                        case HEADPHONE:
                            params = parseHeadphoneParams(jsonReader);
                            break;
                        default:
                            Log.e("AwarenessLib","Fence type unknown.");
                            break;
                    }

                    break;
                default:
                    Log.w("AwarenessLib","Unknown tag while reading fence object: " + tag);
                    break;
            }

        }
        jsonReader.endObject();
        switch(fenceType){
            case DETECTED_ACTIVITY:
                Log.d(TAG, "parseFence: returning DetectedActivity");
                return new DetectedActivityFence(fenceName,DAMethod.valueFor(fenceMethod),action,(DetectedActivityParameter)params);
            case LOCATION:
                Log.d(TAG, "parseFence: returning Location");
                return new LocationFence(fenceName,LocationMethod.valueFor(fenceMethod),action,(LocationParameter)params);
            case HEADPHONE:
                Log.d(TAG, "parseFence: returning Headphone");
                return new HeadphoneFence(fenceName,HeadphoneMethod.valueFor(fenceMethod),action,(HeadphoneParameter)params);
            default:
                return null;
        }
    }

    private FenceParameter parseDetectedActivityParams(JsonReader jsonReader) throws IOException {
        Log.d(TAG, "parseDetectedActivityParams: parsing");
        DetectedActivityParameter.Builder builder = new DetectedActivityParameter.Builder();
        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String tag = jsonReader.nextName();
            switch (tag){
                case "activityTypes":
                    jsonReader.beginArray();
                        while(jsonReader.hasNext()){
                            String actType = jsonReader.nextString();
                            Log.d(TAG, "parseDetectedActivityParams: type: " + actType);
                            builder.addActivityType(actType);
                        }
                    jsonReader.endArray();
                        break;
                default:
                    Log.w("AwarenessLib", "Unknown tag while reading Headphone Fence parameters: " + tag);
                    break;
            }
        }
        jsonReader.endObject();
        return builder.build();
    }

    private HeadphoneParameter parseHeadphoneParams(JsonReader jsonReader) throws IOException {
        HeadphoneParameter.Builder paramsBuilder = new HeadphoneParameter.Builder();
        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String tag = jsonReader.nextName();
            switch(tag){
                case "headphoneState":
                    String state = jsonReader.nextString();
                    paramsBuilder = paramsBuilder.setHeadphoneState(state);
                    break;
                default:
                    Log.w("AwarenessLib", "Unknown tag while reading Headphone Fence parameters: " + tag);
                    break;
            }
        }
        jsonReader.endObject();

        return paramsBuilder.build();
    }
    private LocationParameter parseLocationParams(JsonReader jsonReader) throws IOException {
        LocationParameter.Builder builder = new LocationParameter.Builder();
        double latitude = 0,longitude = 0,radius = 0;
        long dwellTimeMillis = 1000;
        jsonReader.beginObject(); //
        while(jsonReader.hasNext()){
            String tag = jsonReader.nextName();
            switch(tag){
                case "latitude":
                    latitude = jsonReader.nextDouble();
                    break;
                case "longitude":
                    longitude = jsonReader.nextDouble();
                    break;
                case "radius":
                    radius = jsonReader.nextDouble();
                    break;
                case "dwellTimeMillis":
                    dwellTimeMillis = jsonReader.nextLong();
                    break;
                default:
                    Log.w("AwarenessLib","Unknown tag while reading Location Fence parameters: " + tag);
                    break;
            }
        }
        jsonReader.endObject();

        return builder.setLatitude(latitude).setLongitude(longitude).setRadius(radius).setDwellTimeMillis(dwellTimeMillis).build();
    }

}
