package smd.ufc.br.easycontext.fence.parameter;

import smd.ufc.br.easycontext.fence.Fence;

public class AggregateParameter implements FenceParameter{
    Fence fence1;
    Fence fence2;

    private AggregateParameter(Fence fence1, Fence fence2){
        this.fence1 = fence1;
        this.fence2 = fence2;
    }
    private AggregateParameter(Fence fence){
        this.fence1= fence;
    }

    public Fence getFence1() {
        return fence1;
    }


    public Fence getFence2() {
        return fence2;
    }

    public static class Builder{
        Fence f1;
        Fence f2;

        public Builder(){

        }
        public Builder setFence1(Fence fence){
            f1 = fence;
            return this;
        }
        public Builder setFence2(Fence fence){
            f2 = fence;
            return this;
        }

        public AggregateParameter build(){
            if (f1 == null) {
                throw new NullPointerException("Fence1 cannot be null");
            } else if (f2 == null){

                return new AggregateParameter(f1);
            } else {
                return new AggregateParameter(f1, f2);
            }
        }
    }
}
