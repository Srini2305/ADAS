import java.util.Observable;
import java.util.Observer;

/**
 * DisplayOut.java - Class for displaying output in the console
 */
public class DisplayOutput implements Observer {

    public boolean printed = false;
    /**
     * update - Prints the list of SensorOutput in the console
     */
    @Override
    public void update(Observable o, Object arg) {
        SensorOutput s = (SensorOutput) o;
        if(!printed){
            printed = true;
            System.out.format("%-15s\t%-20s%-20s%-20s%-20s%-20s%-20s%n","Current Time","Vehicle Speed",
                    "SteerAngle","YawRate","LatAccel","LongAccel","GPSLat/Long");
        }
        System.out.format("%15s\t%-20s%-20s%-20s%-20s%-20s%-20s\r",s.getOffset(), s.getVehicleSpeed(),
                s.getSteeringWheelAngle(), s.getYawRate(), s.getLateralAcceleration(), s.getLongitudinalAcceleration(),
                s.getGpsLatitude()+" "+s.getGpsLongitude());
    }
}
