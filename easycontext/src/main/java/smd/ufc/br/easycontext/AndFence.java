package smd.ufc.br.easycontext;

import com.google.android.gms.awareness.fence.AwarenessFence;


/**
 * Created by davitabosa on 20/08/2018.
 */

public class AndFence extends Fence{

    private Fence a,b;
    public AndFence(String name, FenceAction action, Fence a, Fence b) {
        super(name, FenceType.AND, action, null);
        this.a = a;
        this.b = b;
    }

    @Override
    public AwarenessFence getMethod() {
        return AwarenessFence.and(a.getMethod(),b.getMethod());
    }
}
