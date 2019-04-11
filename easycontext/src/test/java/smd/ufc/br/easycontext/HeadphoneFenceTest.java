package smd.ufc.br.easycontext;

import org.junit.Test;

import static org.junit.Assert.*;

public class HeadphoneFenceTest {
    @Test(expected = Exception.class)
    public void testBuildNull(){
        HeadphoneFence.Builder builder = new HeadphoneFence.Builder();
        builder.build();
    }
}
