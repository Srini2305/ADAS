import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class Frame extends JFrame implements Observer {

    public JLabel currentTime = new JLabel("Current Time");
    public JLabel vehicleSpeed = new JLabel("Vehicle Speed");
    public JLabel steerAngle = new JLabel("Steer Angle");
    public JLabel yawRate = new JLabel("Yaw Rate");
    public JLabel latAccel = new JLabel("LatAccel");
    public JLabel longAccel = new JLabel("LongAccel");
    public JLabel gps = new JLabel("GPS");
    public JLabel message = new JLabel("Message");
    public JButton start = new JButton("Start");
    public JLabel timeText = new JLabel();
    public JLabel vehicleText = new JLabel();
    public JLabel steerText = new JLabel();
    public JLabel yawText = new JLabel();
    public JLabel latText = new JLabel();
    public JLabel longText = new JLabel();
    public JLabel gpsText = new JLabel();
    public JLabel messageText = new JLabel();

    SensorOutputEstimator sensorOutputEstimator;

    ArrayList<CurveObject> curveObjectList = new ArrayList<>();
    CurveObject curveObject = new CurveObject();
    ArrayList<Float> speedList = new ArrayList<>();
    boolean curveStart = false;
    boolean curveEnd = false;
    boolean flag = false;
    String messageValue = null;

    Frame(){
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(8, 2));
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
        add(p1, "North");
        add(p2, "South");
        setVisible(true);
        this.setSize(600, 800);
        this.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        SensorOutput s = (SensorOutput) o;

        if(!s.getSteeringWheelAngle().equals("-")){
            computeCurve(s);
        }
        if(curveStart){
            message.setText(curveObject.getSpeedLevel()+" speed "+ curveObject.getDirection() + " curve detected");
        } else if (!curveStart && curveEnd){
            message.setText("Curve Start: "+curveObject.getEntryLatitude()+" "+curveObject.getEntryLongitude()+"," +
                    " Curve End: "+curveObject.getExitLatitude()+" "+curveObject.getExitLongitude()+ ", Average Speed:"
                    +curveObject.getAverageSpeed() +", Curve Type: "+curveObject.getSpeedLevel()
                    +", Curve Direction"+curveObject.getDirection());
        }
        timeText.setText(s.getOffset());
        vehicleText.setText(s.getVehicleSpeed());
        steerText.setText(s.getSteeringWheelAngle());
        yawText.setText(s.getYawRate());
        latText.setText(s.getLateralAcceleration());
        longText.setText(s.getLongitudinalAcceleration());
        gpsText.setText(s.getGpsLatitude()+" "+ s.getGpsLongitude());
    }

    public void callCompute(){
        Main.thread.stop();
        Main.thread = new Thread(Main::startGUI);
        Main.thread.start();
    }

    public void setSensorOutputEstimator(SensorOutputEstimator sensorOutputEstimator) {
        this.sensorOutputEstimator = sensorOutputEstimator;
    }

    /*public void computeCurve(SensorOutput s){
        double radiusOfCurvature = Double.parseDouble(s.getVehicleSpeed().substring(0,s.getVehicleSpeed().length()-4))
                / Double.parseDouble(s.getYawRate().substring(0,s.getYawRate().length()-3));
        double angle = 1 / Math.cos(1/radiusOfCurvature);
        if(angle >= 30 && angle <= 180){
            Double acceleration = Math.pow(Double.parseDouble(s.getVehicleSpeed()),2) / radiusOfCurvature;
            if(acceleration > Double.parseDouble(s.getVehicleSpeed())){
                curveObject.setType("High");
            }else{
                curveObject.setType("Low");
            }
            if(radiusOfCurvature > 0){
                curveObject.setDirection("Left");
            }else{
                curveObject.setDirection("Right");
            }
            curveObject.setCurveEntry(Float.parseFloat(s.getOffset()));
            flag = true;
            message.setText(curveObject.getType()+" speed"+ curveObject.getDirection() + " curve detected");
        }
    }*/

    public void computeCurve(SensorOutput s){
        String steeringWheelAngle = s.getSteeringWheelAngle().substring(0,s.getSteeringWheelAngle().length()-1);
        float steeringAngle = Float.parseFloat(steeringWheelAngle);
        if(!curveStart && Math.abs(steeringAngle)>12){
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
            curveObject.setAverageSpeed(speed);
            if(speed>50){
                curveObject.setSpeedLevel("High");
            } else {
                curveObject.setSpeedLevel("Low");
            }
            curveStart = true;
            curveEnd = true;
        } else if(curveStart && Math.abs(steeringAngle)<=12){
            curveObject.setExitOffset(Float.parseFloat(s.getOffset()));
            curveObject.setExitLatitude(s.getGpsLatitude());
            curveObject.setExitLongitude(s.getGpsLongitude());
            updateSpeed(s);
            speedList = new ArrayList<>();
            curveStart = false;
            curveObjectList.add(curveObject);
            curveObject = new CurveObject();
        } else if(curveStart && Math.abs(steeringAngle)>12){
            updateSpeed(s);
        }

    }

    public void updateSpeed(SensorOutput s){
        float sp = Float.parseFloat(s.getVehicleSpeed().substring(0, s.getVehicleSpeed().length()-4));
        speedList.add(sp);
        float averageSpeed = 0;
        for(Float speed:speedList)
            averageSpeed+=speed;
        averageSpeed/=speedList.size();
        curveObject.setAverageSpeed(averageSpeed);
        if(averageSpeed>50){
            curveObject.setSpeedLevel("High");
        } else {
            curveObject.setSpeedLevel("Low");
        }
    }
}
