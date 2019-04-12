package smd.ufc.br.easycontext;

import org.junit.Test;

import smd.ufc.br.easycontext.fence.HeadphoneFence;

public class HeadphoneFenceTest {
    @Test(expected = Exception.class)
    public void testBuildNull(){
        HeadphoneFence.Builder builder = new HeadphoneFence.Builder();
        builder.build();
    }
}
