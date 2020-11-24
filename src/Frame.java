import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    ArrayList<CurveObject> curveObjects = new ArrayList<>();
    CurveObject curveObject = new CurveObject();
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
        if(flag == true){
            curveObject.setCurveExit(Float.parseFloat(s.getOffset()));
            curveObjects.add(curveObject);
            curveObject = new CurveObject();
            flag = false;
            message.setText(curveObject.getType());
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

    public void computeCurve(SensorOutput s){
        double radiusOfCurvature = Double.parseDouble(s.getVehicleSpeed()) / Double.parseDouble(s.getYawRate());
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
    }
}
