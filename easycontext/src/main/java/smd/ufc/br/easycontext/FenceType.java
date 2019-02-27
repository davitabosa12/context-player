package smd.ufc.br.easycontext;

import java.util.HashMap;
import java.util.Map;

public enum FenceType {


	DETECTED_ACTIVITY("ActivityDetection"),

	HEADPHONE("Headphone"),

	LOCATION("Location"),
	OR("or"),
	AND("and");
	private static final Map<String, FenceType> map = new HashMap<>();
	static {
		for (FenceType en : values()) {
			map.put(en.value, en);
		}
	}

	public static FenceType valueFor(String name) {
		return map.get(name);
	}
	private String value;
	FenceType(String valueOf){
	    this.value = valueOf;
    }

}
