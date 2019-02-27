package smd.ufc.br.easycontext;

/**
 * Created by davitabosa on 20/06/2018.
 */

public class LocationParameter implements FenceParameter {

    private double latitude;
    private double longitude;
    private double radius;
    private long dwellTimeMillis;
    private LocationParameter(){

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public long getDwellTimeMillis() {
        return dwellTimeMillis;
    }

    public void setDwellTimeMillis(long dwellTimeMillis) {
        this.dwellTimeMillis = dwellTimeMillis;
    }


    static class Builder{
        private double latitude;
        private double longitude;
        private double radius;
        private long dwellTimeMillis;

        public Builder(){

        }
        public Builder setLatitude(double latitude){
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }
        public Builder setRadius(double radius) {
            this.radius = radius;
            return this;
        }
        public Builder setDwellTimeMillis(long dwellTimeMillis) {
            this.dwellTimeMillis = dwellTimeMillis;
            return this;
        }

        public LocationParameter build(){
            LocationParameter temp = new LocationParameter();
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);
            temp.setDwellTimeMillis(dwellTimeMillis);
            temp.setRadius(radius);
            return temp;
        }
    }
}
