package smd.ufc.br.easycontext.fence;

import com.google.android.gms.awareness.fence.AwarenessFence;

import java.lang.reflect.Method;

import smd.ufc.br.easycontext.exception.MethodMismatchException;
import smd.ufc.br.easycontext.exception.ReflectionUtil;
import smd.ufc.br.easycontext.exception.RuleNotProvidedException;
import smd.ufc.br.easycontext.fence.method.AggregateMethod;
import smd.ufc.br.easycontext.fence.method.FenceMethod;
import smd.ufc.br.easycontext.fence.parameter.AggregateParameter;
import smd.ufc.br.easycontext.fence.parameter.FenceParameter;
import smd.ufc.br.easycontext.fence.type.FenceType;

public class AggregateFence extends Fence {

    private AggregateParameter params;
    private AggregateMethod method;

    AggregateFence(String name, AggregateMethod method, FenceAction action, AggregateParameter params) {
        super(name, FenceType.AGGREGATE, action, params);
        this.params = params;
        this.method = method;
    }

    @Override
    public void setMethod(FenceMethod method) {
        if(method instanceof AggregateMethod){
            this.method = (AggregateMethod) method;
        } else {
            throw new MethodMismatchException("Expected a AggregateMethod");
        }
    }

    @Override
    public void setParams(FenceParameter params) {

    }

    @Override
    public AwarenessFence getMethod() {
        return null;
    }

    public static class Builder{
        String name;
        AggregateMethod method;
        FenceAction action;
        AggregateParameter params;


        public Builder(){

        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAction(FenceAction action) {
            this.action = action;
            return this;
        }

        public Builder and(Fence a, Fence b){
            this.method = AggregateMethod.AGGREGATE_AND;
            this.params = new AggregateParameter.Builder()
                    .setFence1(a)
                    .setFence2(b)
                    .build();
            return this;
        }
        public Builder or(Fence a, Fence b){
            this.method = AggregateMethod.AGGREGATE_OR;
            this.params = new AggregateParameter.Builder()
                    .setFence1(a)
                    .setFence2(b)
                    .build();
            return this;
        }
        public Builder not(Fence fence){
            this.method = AggregateMethod.AGGREGATE_NOT;
            this.params = new AggregateParameter.Builder()
                    .setFence1(fence)
                    .build();
            return this;
        }

        public AggregateFence build(){
            if(name == null || name.isEmpty()){
                throw new IllegalArgumentException("name must be provided");
            }
            if(action == null){
                throw new IllegalArgumentException("action must not be null");
            }

            if(method == null){
                String methods = ReflectionUtil.getMethodsFromClass(Builder.class);
                throw new RuleNotProvidedException("AggregateFence rule is not set. Please use one of the following available methods: " + methods);
            }

            return new AggregateFence(name, method, action, params);
        }
    }
}
