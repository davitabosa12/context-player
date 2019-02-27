package smd.ufc.br.easycontext;

import com.google.android.gms.awareness.fence.AwarenessFence;

public abstract class Fence {
    private String name;
    private FenceType type;
    private FenceMethod method;
    private FenceAction action;
    private FenceParameter params;

    Fence(String name, FenceType type, FenceAction action, FenceParameter params){
		this.name = name;
		this.type = type;
		this.action = action;

		/*switch (type){
                case HEADPHONE:
                    if(!(params instanceof HeadphoneParameter))
                        throw new IllegalArgumentException("Fence parameters of different types, expected a HeadphoneParameter, got " +
                                params.getClass().getSimpleName());
                    break;
                case LOCATION:

                    if(!(params instanceof LocationParameter))
                        throw new IllegalArgumentException("Fence parameters of different types, expected a LocationParameter, got " +
                                params.getClass().getSimpleName());
                    break;
                case DETECTED_ACTIVITY:

                    if(!(params instanceof DetectedActivityParameter))
                        throw new IllegalArgumentException("Fence parameters of different types, expected a DetectedActivityParameter, got " +
                                params.getClass().getSimpleName());
                    break;
                default:
                    break;
        }*/
        //TODO: validar methods
        this.params = params;
        //this.method = method;
        if(!validateMethods()){
            throw new IllegalArgumentException("Params of different methods.");
        }
	}

    private boolean validateMethods(){
        return true;
    }
	public FenceAction getAction() {
		return action;
	}


	//@SuppressLint("MissingPermission")
    public abstract AwarenessFence getMethod();
    /*public AwarenessFence getMethod() {
        DetectedActivityParameter daParams;
        LocationParameter locParams;
        HeadphoneParameter hParams;
		switch (method){
            case DA_DURING:
                int[] types;
            {
                    daParams = (DetectedActivityParameter) params;
                    types = new int[daParams.getActivityTypeList().size()];
                    for (int i = 0; i < daParams.getActivityTypeList().size(); i++) {
                        types[i] = daParams.getActivityTypeList().get(i);
                    }
                }
                return DetectedActivityFence.during(types);
            case DA_STARTING:
                {
                    daParams = (DetectedActivityParameter) params;
                    types = new int[daParams.getActivityTypeList().size()];
                    for (int i = 0; i < daParams.getActivityTypeList().size(); i++) {
                        types[i] = daParams.getActivityTypeList().get(i);
                    }
                }
                return DetectedActivityFence.starting(types);
            case DA_STOPPING:
                {
                    daParams = (DetectedActivityParameter) params;
                    types = new int[daParams.getActivityTypeList().size()];
                    for (int i = 0; i < daParams.getActivityTypeList().size(); i++) {
                        types[i] = daParams.getActivityTypeList().get(i);
                    }
                }
                return DetectedActivityFence.stopping(types);
            case LOCATION_IN:
                locParams = (LocationParameter) params;
                return LocationFence.in(locParams.getLatitude(),locParams.getLongitude(),
                        locParams.getRadius(),locParams.getDwellTimeMillis());
            case LOCATION_EXITING:
                locParams = (LocationParameter) params;
                return LocationFence.exiting(locParams.getLatitude(),locParams.getLongitude(),locParams.getRadius());
            case LOCATION_ENTERING:
                locParams = (LocationParameter) params;
                return LocationFence.entering(locParams.getLatitude(),locParams.getLongitude(),locParams.getRadius());
            case HEADPHONE_DURING:
                hParams = (HeadphoneParameter) params;
                return HeadphoneFence.during(hParams.getHeadphoneState());
            case HEADPHONE_UNPLUGGING:
                return HeadphoneFence.unplugging();
            case HEADPHONE_PLUGGING_IN:
                return HeadphoneFence.pluggingIn();
            default:
                throw new UnsupportedOperationException("Not supported yet.");
        }
	}
*/
	public String getName() {
		return name;
	}

}
