import java.util.Observable;

/**
 * POJO to store the contents of SensorOutput
 * @author srini
 * @since 10/18/2020
 */
public class SensorOutput extends Observable {

    private String offset; // Offset
    private String steeringWheelAngle = "-";
    private String vehicleSpeed = "-";
    private String yawRate = "-";
    private String longitudinalAcceleration = "-";
    private String lateralAcceleration = "-";
    private String gpsLatitude = "-";
    private String gpsLongitude = "-";

    public SensorOutput() {
    }

    public SensorOutput(String offset, String steeringWheelAngle, String vehicleSpeed, String yawRate,
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

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
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
        setChanged();
        notifyObservers();
    }

    public String getYawRate() {
        return yawRate;
    }

    public void setYawRate(String yawRate) {
        this.yawRate = yawRate;
        setChanged();
        notifyObservers();
    }

    public String getLongitudinalAcceleration() {
        return longitudinalAcceleration;
    }

    public void setLongitudinalAcceleration(String longitudinalAcceleration) {
        this.longitudinalAcceleration = longitudinalAcceleration;
        setChanged();
        notifyObservers();
    }

    public String getLateralAcceleration() {
        return lateralAcceleration;
    }

    public void setLateralAcceleration(String lateralAcceleration) {
        this.lateralAcceleration = lateralAcceleration;
        setChanged();
        notifyObservers();
    }

    public String getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
        setChanged();
        notifyObservers();
    }

    public String getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
        setChanged();
        notifyObservers();
    }
}
