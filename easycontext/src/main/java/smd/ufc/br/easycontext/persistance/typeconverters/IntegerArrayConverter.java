package smd.ufc.br.easycontext.persistance.typeconverters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Arrays;

public class IntegerArrayConverter {

    @TypeConverter
    public static int[] toIntegerArray(String json){
        Gson gson = new Gson();
        int[] result = gson.fromJson(json, int[].class);
        if(result == null)
            return new int[0];
        return result;
    }
    @TypeConverter
    public static String fromIntegerArray(int[] array){
        Gson gson = new Gson();
        return gson.toJson(array);
    }
}
