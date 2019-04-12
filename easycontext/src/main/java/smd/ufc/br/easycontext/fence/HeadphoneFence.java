package smd.ufc.br.easycontext.fence;

import com.google.android.gms.awareness.fence.AwarenessFence;

import java.lang.reflect.Method;

import smd.ufc.br.easycontext.exception.ReflectionUtil;
import smd.ufc.br.easycontext.exception.RuleNotProvidedException;
import smd.ufc.br.easycontext.fence.method.FenceMethod;
import smd.ufc.br.easycontext.fence.parameter.FenceParameter;
import smd.ufc.br.easycontext.fence.type.FenceType;
import smd.ufc.br.easycontext.fence.method.HeadphoneMethod;
import smd.ufc.br.easycontext.fence.parameter.HeadphoneParameter;
import smd.ufc.br.easycontext.exception.MethodMismatchException;

/**
 * Created by davitabosa on 13/08/2018.
 */

public class HeadphoneFence extends Fence {

    private HeadphoneMethod method;
    private HeadphoneParameter params;

    public HeadphoneFence(String name, HeadphoneMethod method, FenceAction action, HeadphoneParameter params) {
        super(name, FenceType.HEADPHONE, action, params);
        this.method = method;
        this.params = params;
    }

    @Override
    public void setMethod(FenceMethod method) {
        if(method instanceof HeadphoneMethod){
            this.method = (HeadphoneMethod) method;
        } else {
            throw new MethodMismatchException("Provided method is not of type HeadphoneMethod.");
        }
    }

    @Override
    public HeadphoneParameter getParams() {
        return params;
    }

    @Override
    public void setParams(FenceParameter params) {
        if (params == null) {
            this.params = null;
        } else {

            this.params = (HeadphoneParameter) params;
        }
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
            headphoneFence.setMethod(HeadphoneMethod.HEADPHONE_PLUGGING_IN);
            headphoneFence.setParams(null);
            return this;
        }

        public Builder unplugging(){
            headphoneFence.setMethod(HeadphoneMethod.HEADPHONE_UNPLUGGING);
            headphoneFence.setParams(null);
            return this;

        }
        public Builder during(int headphoneState){
            headphoneFence.setMethod(HeadphoneMethod.HEADPHONE_DURING);
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
                String methods = ReflectionUtil.getMethodsFromClass(Builder.class);
                throw new RuleNotProvidedException("HeadphoneFence rule is not set. Please use one of the following available methods: " + methods);
            }
            return headphoneFence;
        }

    }
}
