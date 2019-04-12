package smd.ufc.br.easycontext;

import java.util.ArrayList;

import smd.ufc.br.easycontext.fence.Fence;

/**
 * Created by davitabosa on 13/06/2018.
 */

public class AwarenessActivity {
    private String name;
    private ArrayList<Fence> fences;

    public AwarenessActivity(String name, ArrayList<Fence> fences){
        this.name = name;
        this.fences = fences;
    }


    public String getName() {
        return name;
    }

    public ArrayList<Fence> getFences() {
        return fences;
    }
}
