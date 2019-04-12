package smd.ufc.br.easycontext.fence;

import com.google.android.gms.awareness.fence.AwarenessFence;

import smd.ufc.br.easycontext.fence.method.FenceMethod;
import smd.ufc.br.easycontext.fence.parameter.FenceParameter;
import smd.ufc.br.easycontext.fence.type.FenceType;


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
    public void setMethod(FenceMethod method) {
        //does nothing
    }

    @Override
    public void setParams(FenceParameter params) {
         //nothing
    }

    @Override
    public AwarenessFence getMethod() {
        return AwarenessFence.and(a.getMethod(),b.getMethod());
    }
}
