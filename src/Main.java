import javafx.util.Pair;

import java.util.*;

/**
 * Main.java - Main class for invocation
 */
public class Main {

    private static final String GPS_FILENAME = "19 GPS Track.htm"; // Default filename for GPS
    private static final String CAN_FRAME_FILENAME = "CAN Frames Info.txt"; // Default filename for CAN_FRAME
    private static final String CAN_MESSAGE_FILENAME = "19 CANMessages.trc"; // Default filename for CAN_MESSAGE

    static Thread thread = new Thread(Main::startGUI);
    static Frame frame = new Frame();
    static List<Pair<Float, Float>> gpsData;
    static List<CANFrame> canFrameList;
    static List<CANMessage> canMessageList;
    static List<Observer> observerList = new ArrayList<>();

    public static void main(String[] args) {
        String gpsFile = GPS_FILENAME;
        String canFrameFile = CAN_FRAME_FILENAME;
        String canMessageFile = CAN_MESSAGE_FILENAME;
        // If filename are provided in the command line arg then that filenames are used
        if(args.length>0){
            gpsFile = args[0]; //First filename is GPS file
            canFrameFile = args[1]; //Second filename is CAN Frame file
            canMessageFile = args[2]; // Third filename is CAN Message file
        }
        gpsData = DataParser.getGPSData(gpsFile); // Reading and parsing gps file
        canFrameList = DataParser.getCANFrameData(canFrameFile); // Reading and parsing CANFrame file
        //Storing unique Frame IDs in Set<String> frameSet
        Set<String> frameSet = new HashSet<>();
        for (CANFrame canFrame:canFrameList)
            frameSet.add(canFrame.getFrame());
        canMessageList = DataParser.getCANMessageData(canMessageFile, frameSet); // Reading and parsing CANMessage file
        observerList = new ArrayList<>();
        observerList.add(new DisplayOutput());
        observerList.add(frame);
    }

    public static void startGUI(){
        SensorOutputEstimator sensorOutputEstimator = new SensorOutputEstimator(gpsData,canFrameList, canMessageList,
                observerList);
        sensorOutputEstimator.computeSensorOutput(); // Computing sensor output
    }

}
