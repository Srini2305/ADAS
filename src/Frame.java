import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Frame extends JFrame implements Observer {
    public static Panel panel = new Panel();
    public JLabel currentTime = new JLabel("Current Time");
    public JLabel vehicleSpeed = new JLabel("Vehicle Speed");
    public JLabel steerAngle = new JLabel("Steer Angle");
    public JLabel yawRate = new JLabel("Yaw Rate");
    public JLabel latAccel = new JLabel("LatAccel");
    public JLabel longAccel = new JLabel("LongAccel");
    public JLabel gps = new JLabel("GPS");
    public JLabel message = new JLabel("Message");
    public JTextField timeText = new JTextField(20);
    public JTextField vehicleText = new JTextField(20);
    public JTextField steerText = new JTextField(20);
    public JTextField yawText = new JTextField(20);
    public JTextField latText = new JTextField(20);
    public JTextField longText = new JTextField(20);
    public JTextField gpsText = new JTextField(20);
    public JTextField messageText = new JTextField(30);
    Frame(){
        /*
        int frameHeight = 400;
        int frameWidth = 100;
        this.setTitle("Can Data");
        this.setBackground(Color.LIGHT_GRAY);
        this.setSize(new Dimension(frameWidth, frameHeight));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.getContentPane().add(panel, "North");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
         */
        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(8, 2));
        FlowLayout layout = new FlowLayout();
        JPanel p2 = new JPanel();
        p2.setLayout(layout);

        JButton buttonSave, buttonExit;
        buttonSave = new JButton("START");
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
        p2.add(buttonSave);
        add(p1, "North");
        add(p2, "South");
        setVisible(true);
        this.setSize(600, 800);
    }

    @Override
    public void update(Observable o, Object arg) {
        SensorOutput s = (SensorOutput) o;
        timeText.setText(s.getOffset()+"");
        vehicleText.setText(s.getVehicleSpeed());
        steerText.setText(s.getSteeringWheelAngle());
        yawText.setText(s.getYawRate());
        latText.setText(s.getLateralAcceleration());
        longText.setText(s.getLongitudinalAcceleration());
        gpsText.setText(s.getGpsLatitude()+" "+ s.getGpsLongitude());
    }
}
