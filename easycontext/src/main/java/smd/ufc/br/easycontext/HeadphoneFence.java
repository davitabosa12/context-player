package smd.ufc.br.easycontext;

import com.google.android.gms.awareness.fence.AwarenessFence;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

    public static class Builder{
        private HeadphoneFence headphoneFence;
        public Builder() {
            headphoneFence = new HeadphoneFence(null, null, null, null);
        }

        public Builder setAction(FenceAction action){
            headphoneFence.setAction(action);
            return this;
        }

        public Builder setName(String name){
            headphoneFence.setName(name);
            return this;
        }

        public Builder pluggingIn(){
            headphoneFence.setMethod(FenceMethod.HEADPHONE_PLUGGING_IN);
            headphoneFence.setParams(null);
            return this;
        }

        public Builder unplugging(){
            headphoneFence.setMethod(FenceMethod.HEADPHONE_UNPLUGGING);
            headphoneFence.setParams(null);
            return this;

        }
        public Builder during(int headphoneState){
            headphoneFence.setMethod(FenceMethod.HEADPHONE_DURING);
            headphoneFence.setParams(
                    new HeadphoneParameter.Builder()
                            .setHeadphoneState(headphoneState) //set headphone state
                            .build());
            return this;
        }

        public HeadphoneFence build(){
            if (headphoneFence.getName() == null) {
                throw new IllegalArgumentException("HeadphoneFence name is not set. Please use Builder.setName() to set a valid name.");
            }
            if(headphoneFence.getAction() == null){
                throw new IllegalArgumentException("HeadphoneFence action is not set. Please use Builder.setAction()");
            }
            if(headphoneFence.getMethod() == null){
                StringBuilder sb = new StringBuilder();
                Class<Builder> builderClass = Builder.class;
                for(Method method : builderClass.getDeclaredMethods()){
                    sb.append(method.getName());
                    sb.append("(");
                    int numOfParameters = 0;
                    for(Class c : method.getParameterTypes()){

                        sb.append(c.getName());
                        if(numOfParameters > 0 && numOfParameters < method.getParameterTypes().length - 2)
                            sb.append(", ");
                        numOfParameters++;
                    }
                }


                throw new IllegalArgumentException("HeadphoneFence method is not set. Please use one of the following available methods: " + sb.toString());
            }
            return headphoneFence;
        }

    }
}
