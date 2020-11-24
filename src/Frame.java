import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Frame.java - Class to create frame and add panel components. Observer class to gather required data
 */
public class Frame extends JFrame implements Observer {

    private final JLabel currentTime = new JLabel("Current Time(ms)");
    private final JLabel vehicleSpeed = new JLabel("Vehicle Speed");
    private final JLabel steerAngle = new JLabel("Steer Angle");
    private final JLabel yawRate = new JLabel("Yaw Rate");
    private final JLabel latAccel = new JLabel("LatAccel");
    private final JLabel longAccel = new JLabel("LongAccel");
    private final JLabel gps = new JLabel("GPS");
    private final JLabel message = new JLabel("Curve Detection");
    private final JLabel warning = new JLabel("Warning");
    private final JButton start = new JButton("Start");
    private final JLabel timeText = new JLabel("-");
    private final JLabel vehicleText = new JLabel("-");
    private final JLabel steerText = new JLabel("-");
    private final JLabel yawText = new JLabel("-");
    private final JLabel latText = new JLabel("-");
    private final JLabel longText = new JLabel("-");
    private final JLabel gpsText = new JLabel("-");
    private final JLabel messageText = new JLabel("-");
    private final JLabel warningText = new JLabel("-");

    private ArrayList<CurveObject> curveObjectHistory = new ArrayList<>();
    private ArrayList<CurveObject> curveObjectList = new ArrayList<>();
    private CurveObject curveObject = new CurveObject();
    private ArrayList<Float> speedList = new ArrayList<>();
    boolean curveStart = false;
    boolean curveEnd = false;
    int pos = 0;

    /**
     * Frame - Constructor to add panel and panel components
     */
    Frame(){
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(9, 2));
        FlowLayout layout = new FlowLayout();
        JPanel p2 = new JPanel();
        p2.setLayout(layout);
        start.addActionListener(e -> callCompute());
        p2.add(start);
        p1.add(currentTime);
        p1.add(timeText);
        p1.add(vehicleSpeed);
        p1.add(vehicleText);
        p1.add(steerAngle);
        p1.add(steerText);
        p1.add(yawRate);
        p1.add(yawText);
        p1.add(latAccel);
        p1.add(latText);
        p1.add(longAccel);
        p1.add(longText);
        p1.add(gps);
        p1.add(gpsText);
        p1.add(message);
        p1.add(messageText);
        p1.add(warning);
        p1.add(warningText);
        add(p1, "North");
        add(p2, "South");
        setVisible(true);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    /**
     * update - Default function of observer which calls computeWarnings to set values
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        SensorOutput s = (SensorOutput) o;
        if(!s.getSteeringWheelAngle().equals("-")){
            computeCurve(s);
        }
        // Setting message text
        if(curveStart){
            messageText.setText(curveObject.getSpeedLevel()+" speed "+ curveObject.getDirection() + " curve detected");
        } else if (!curveStart && curveEnd){
            messageText.setText("Start: "+curveObject.getEntryLatitude()+" "+curveObject.getEntryLongitude()+
                    " End: "+curveObject.getExitLatitude()+" "+curveObject.getExitLongitude()+
                    " Avg Speed:" +curveObject.getAverageSpeed() +". "+curveObject.getSpeedLevel() +" Speed "
                    +curveObject.getDirection() + " Curve");
        }
        computeWarning(s); // Function to detect the warning
        // Setting values to the labels
        timeText.setText(s.getOffset());
        vehicleText.setText(s.getVehicleSpeed());
        steerText.setText(s.getSteeringWheelAngle());
        yawText.setText(s.getYawRate());
        latText.setText(s.getLateralAcceleration());
        longText.setText(s.getLongitudinalAcceleration());
        gpsText.setText(s.getGpsLatitude()+" "+ s.getGpsLongitude());
    }

    /**
     *callCompute - Action listener for start button
     */
    public void callCompute(){
        Main.thread.stop();
        if(curveStart && !curveEnd){
            curveObjectList.add(curveObject);
        }
        curveObjectHistory = curveObjectList;
        curveObjectList = new ArrayList<>();
        curveObject = new CurveObject();
        speedList = new ArrayList<>();
        curveStart = false;
        curveEnd = false;
        pos = 0;
        timeText.setText("-");
        vehicleText.setText("-");
        steerText.setText("-");
        yawText.setText("-");
        latText.setText("-");
        longText.setText("-");
        gpsText.setText("-");
        messageText.setText("-");
        Main.thread = new Thread(Main::startGUI);
        Main.thread.start();
    }

    /**
     * computeCurve - Function to compute the direction and type of curve
     * @param s - SensorOutput Object passed as parameter
     */
    public void computeCurve(SensorOutput s){
        String steeringWheelAngle = s.getSteeringWheelAngle().substring(0,s.getSteeringWheelAngle().length()-1);
        float steeringAngle = Float.parseFloat(steeringWheelAngle);
        if(!curveStart && Math.abs(steeringAngle)>12){
            curveObject = new CurveObject();
            curveObject.setEntryOffset(Float.parseFloat(s.getOffset()));
            curveObject.setEntryLatitude(s.getGpsLatitude());
            curveObject.setEntryLongitude(s.getGpsLongitude());
            if(steeringAngle<0){
                curveObject.setDirection("Left");
            } else{
                curveObject.setDirection("Right");
            }
            speedList = new ArrayList<>();
            float speed = Float.parseFloat(s.getVehicleSpeed().substring(0, s.getVehicleSpeed().length()-4));
            speedList.add(speed);
            speed = (float) (Math.round(speed*10)/10.0);
            curveObject.setAverageSpeed(speed);
            if(speed>50){
                curveObject.setSpeedLevel("High");
            } else {
                curveObject.setSpeedLevel("Low");
            }
            curveStart = true;
            curveEnd = false;
        } else if(curveStart && Math.abs(steeringAngle)<=12){
            curveObject.setExitOffset(Float.parseFloat(s.getOffset()));
            curveObject.setExitLatitude(s.getGpsLatitude());
            curveObject.setExitLongitude(s.getGpsLongitude());
            updateSpeed(s);
            speedList = new ArrayList<>();
            curveStart = false;
            curveEnd = true;
            curveObjectList.add(curveObject);
        } else if(curveStart && Math.abs(steeringAngle)>12){
            updateSpeed(s);
        }
    }

    /**
     * updateSpeed - function to compute the average speed of the present curve
     * @param s - SensorOutput Object passed as parameter
     */
    public void updateSpeed(SensorOutput s){
        float sp = Float.parseFloat(s.getVehicleSpeed().substring(0, s.getVehicleSpeed().length()-4));
        speedList.add(sp);
        float averageSpeed = 0;
        for(Float speed:speedList)
            averageSpeed+=speed;
        averageSpeed/=speedList.size();
        averageSpeed = (float) (Math.round(averageSpeed*10)/10.0);
        curveObject.setAverageSpeed(averageSpeed);
        if(averageSpeed>50){
            curveObject.setSpeedLevel("High");
        } else {
            curveObject.setSpeedLevel("Low");
        }
    }

    /**
     * computeWarning - Function to compute distance from particular point to curve and set the warning
     * @param s - SensorOutput Object passed as parameter
     */
    public void computeWarning(SensorOutput s){
        if(pos<curveObjectHistory.size() &&
                Float.parseFloat(s.getOffset())>=curveObjectHistory.get(pos).getEntryOffset()-3000){
            CurveObject curveObject = curveObjectHistory.get(pos);
            double dist = distance(s.getGpsLatitude(),curveObject.getEntryLatitude(),
                    s.getGpsLongitude(), curveObject.getEntryLongitude());
            warningText.setText(curveObject.getDirection()+" Curve ahead within a distance(meters) of "+ dist
                    +". Average Speed: "+ curveObject.getAverageSpeed()+"km/h");
        } else {
            warningText.setText("-");
        }
        if(pos<curveObjectHistory.size() && Float.parseFloat(s.getOffset())>curveObjectHistory.get(pos).getEntryOffset()){
            pos++;
        }
    }

    /**
     * distance - function to compute the distance between two points using latitude and longitude position
     * @param la1 - latitude1
     * @param la2 - latitude2
     * @param lo1 - longitude1
     * @param lo2 - longitude2
     * @return
     */
    private double distance(String la1, String la2, String lo1, String lo2){
        double lon1 = Math.toRadians(Double.parseDouble(lo1.substring(0,lo1.length()-1)));
        double lon2 = Math.toRadians(Double.parseDouble(lo2.substring(0,lo2.length()-1)));
        double lat1 = Math.toRadians(Double.parseDouble(la1.substring(0,la1.length()-1)));
        double lat2 = Math.toRadians(Double.parseDouble(la2.substring(0,la2.length()-1)));
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        // Computing the distance based on latitude and longitude
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371000;
        return Math.round(c * r * 10)/10.0;
    }

}
