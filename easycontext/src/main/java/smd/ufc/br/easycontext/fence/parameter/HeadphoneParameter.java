package smd.ufc.br.easycontext.fence.parameter;

import com.google.android.gms.awareness.state.HeadphoneState;

/**
 * Created by davitabosa on 18/06/2018.
 */

public class HeadphoneParameter implements FenceParameter {
    int headphoneState;
    private HeadphoneParameter(int headphoneState){
        this.headphoneState = headphoneState;
    }

    public int getHeadphoneState() {
        return headphoneState;
    }



    public static class Builder{
        private int headphoneState = 0;
        public Builder(){

        }

        public Builder setHeadphoneState(int headphoneState) {
            this.headphoneState = headphoneState;
            return this;
        }
        public Builder setHeadphoneState(String state){
            this.headphoneState = HeadphoneState.PLUGGED_IN;
            if (state.equals("PLUGGED_IN")) {
                this.headphoneState = HeadphoneState.PLUGGED_IN;
            } else if(state.equals("UNPLUGGING")){
                this.headphoneState = HeadphoneState.UNPLUGGED;
            }
            return this;
        }

        public HeadphoneParameter build(){
            return new HeadphoneParameter(headphoneState);
        }


    }
}
