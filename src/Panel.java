import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Panel extends JPanel implements Observer {

    //JTable table = new JTable();
    public JLabel currentTime = new JLabel("Current Time");
    public JLabel vehicleSpeed = new JLabel("Vehicle Speed");
    public JLabel steerAngle = new JLabel("Steer Angle");
    public JLabel yawRate = new JLabel("Yaw Rate");
    public JLabel latAccel = new JLabel("LatAccel");
    public JLabel longAccel = new JLabel("LongAccel");
    public JLabel gps = new JLabel("GPS");
    public JTextField timeText = new JTextField(20);
    public JTextField vehicleText = new JTextField(20);
    public JTextField steerText = new JTextField(20);
    public JTextField yawText = new JTextField(20);
    public JTextField latText = new JTextField(20);
    public JTextField longText = new JTextField(20);
    public JTextField gpsText = new JTextField(20);
    Panel(){
        //this.add(new JScrollPane(table));
        this.setLayout(new GridLayout(6, 2));
        this.add(currentTime);
        this.add(timeText);
        this.add(vehicleSpeed);
        this.add(vehicleText);
        this.add(steerAngle);
        this.add(steerText);
        this.add(yawRate);
        this.add(yawText);
        this.add(latAccel);
        this.add(latText);
        this.add(longAccel);
        this.add(longText);
        this.add(gps);
        this.add(gpsText);
    }

    @Override
    public void update(Observable o, Object arg) {
        SensorOutput s = (SensorOutput) o;
        timeText.setText(s.getOffset()+"");

        /*
        List<String> headers = new ArrayList<>();
        headers.add("Current Time");
        headers.add("Vehicle Speed");
        headers.add("SteerAngle");
        headers.add("YawRate");
        headers.add("LatAccel");
        headers.add("LongAccel");
        headers.add("GPS");
        String headerArray[]=  new String[headers.size()];
        int index =0;
        for(String header:headers){
            headerArray[index] = header;
            index += 1;
        }
        SensorOutput s = (SensorOutput) o;
        String dataArray[][] = getData(s);
        table.setModel(new DefaultTableModel(dataArray, headerArray));
//        if(dataArray[1].length == 7){
//            System.out.println("hi");
//        }
            //table.setPreferredScrollableViewportSize(table.getPreferredSize());
            //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            //this.setPreferredSize(new Dimension(2400,1000));
            //SwingUtilities.updateComponentTreeUI(this);

//        SensorOutput s = (SensorOutput) o;
//        l.setText(s.getVehicleSpeed());
//        //this.add(l);
//        SwingUtilities.updateComponentTreeUI(this);
         */
    }

    private JButton startButton(){
        JButton start = new JButton("start");
        start.setBounds(0,1,10,10);
        start.addActionListener(e->onStart());
        return start;
    }

    public static void onStart(){

    }

    public static String[][] getData(SensorOutput s){
        String[][] sensorData = new String[1][7];
        sensorData[0][0] = (s.getOffset() + "");
        sensorData[0][1] = (s.getVehicleSpeed());
        sensorData[0][2] = (s.getSteeringWheelAngle());
        sensorData[0][3] = (s.getYawRate());
        sensorData[0][4] = (s.getLateralAcceleration());
        sensorData[0][5] = (s.getLongitudinalAcceleration());
        sensorData[0][6] = (s.getGpsLatitude() +" "+ s.getGpsLongitude());
        return sensorData;
    }


}
