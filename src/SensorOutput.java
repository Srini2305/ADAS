import java.util.Observable;

/**
 * POJO to store the contents of SensorOutput
 * @author srini
 * @since 10/18/2020
 */
public class SensorOutput extends Observable {

    private Float offset; // Offset
    private String steeringWheelAngle = "-";
    private String vehicleSpeed = "-";
    private String yawRate = "-";
    private String longitudinalAcceleration = "-";
    private String lateralAcceleration = "-";
    private String gpsLatitude = "-";
    private String gpsLongitude = "-";

    public SensorOutput() {
    }

    public SensorOutput(Float offset, String steeringWheelAngle, String vehicleSpeed, String yawRate,
                        String longitudinalAcceleration, String lateralAcceleration, String gpsLatitude, String gpsLongitude) {
        this.offset = offset;
        this.steeringWheelAngle = steeringWheelAngle;
        this.vehicleSpeed = vehicleSpeed;
        this.yawRate = yawRate;
        this.longitudinalAcceleration = longitudinalAcceleration;
        this.lateralAcceleration = lateralAcceleration;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
    }

    public Float getOffset() {
        return offset;
    }

    public void setOffset(Float offset) {
        this.offset = offset;
    }

    public String getSteeringWheelAngle() {
        return steeringWheelAngle;
    }

    public void setSteeringWheelAngle(String steeringWheelAngle) {
        this.steeringWheelAngle = steeringWheelAngle;
        setChanged();
        notifyObservers();
    }

    public String getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(String vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }

    public String getYawRate() {
        return yawRate;
    }

    public void setYawRate(String yawRate) {
        this.yawRate = yawRate;
    }

    public String getLongitudinalAcceleration() {
        return longitudinalAcceleration;
    }

    public void setLongitudinalAcceleration(String longitudinalAcceleration) {
        this.longitudinalAcceleration = longitudinalAcceleration;
    }

    public String getLateralAcceleration() {
        return lateralAcceleration;
    }

    public void setLateralAcceleration(String lateralAcceleration) {
        this.lateralAcceleration = lateralAcceleration;
    }

    public String getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public String getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }
}
