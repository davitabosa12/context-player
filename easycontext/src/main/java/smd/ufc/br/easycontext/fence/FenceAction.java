package smd.ufc.br.easycontext.fence;

import android.content.Context;

import com.google.android.gms.awareness.fence.FenceState;

public interface FenceAction {

	public void doOperation(Context context, FenceState state);

}
