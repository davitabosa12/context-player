package smd.ufc.br.easycontext;

import com.google.android.gms.awareness.fence.AwarenessFence;

/**
 * Created by davitabosa on 13/08/2018.
 */

public class HeadphoneFence extends Fence {

    private final HeadphoneMethod method;
    private final HeadphoneParameter params;

    public HeadphoneFence(String name, HeadphoneMethod method, FenceAction action, HeadphoneParameter params) {
        super(name, FenceType.HEADPHONE, action, params);
        this.method = method;
        this.params = params;
    }

    @Override
    public AwarenessFence getMethod() {
        switch(method){
            case HEADPHONE_PLUGGING_IN:
                return com.google.android.gms.awareness.fence.HeadphoneFence.pluggingIn();

            case HEADPHONE_UNPLUGGING:
                return com.google.android.gms.awareness.fence.HeadphoneFence.unplugging();

            case HEADPHONE_DURING:
                return com.google.android.gms.awareness.fence.HeadphoneFence.during(params.getHeadphoneState());

            default:
                return null;
        }
    }
}
