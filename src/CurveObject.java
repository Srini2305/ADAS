/**
 * CurveObject.java - Model class for Curve Object
 */
public class CurveObject {

    private String entryLatitude;
    private String entryLongitude;
    private String exitLatitude;
    private String exitLongitude;
    private float entryOffset;
    private float exitOffset;
    private String speedLevel;
    private float averageSpeed;
    private String direction;

    public String getEntryLatitude() {
        return entryLatitude;
    }

    public void setEntryLatitude(String entryLatitude) {
        this.entryLatitude = entryLatitude;
    }

    public String getEntryLongitude() {
        return entryLongitude;
    }

    public void setEntryLongitude(String entryLongitude) {
        this.entryLongitude = entryLongitude;
    }

    public String getExitLatitude() {
        return exitLatitude;
    }

    public void setExitLatitude(String exitLatitude) {
        this.exitLatitude = exitLatitude;
    }

    public String getExitLongitude() {
        return exitLongitude;
    }

    public void setExitLongitude(String exitLongitude) {
        this.exitLongitude = exitLongitude;
    }

    public float getEntryOffset() {
        return entryOffset;
    }

    public void setEntryOffset(float entryOffset) {
        this.entryOffset = entryOffset;
    }

    public float getExitOffset() {
        return exitOffset;
    }

    public void setExitOffset(float exitOffset) {
        this.exitOffset = exitOffset;
    }

    public String getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(String speedLevel) {
        this.speedLevel = speedLevel;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
