import javafx.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author srini
 * @since 10/18/2020
 */
public class Main {

    private static final String GPS_FILENAME = "19 GPS Track.htm"; // Default filename for GPS
    private static final String CAN_FRAME_FILENAME = "CAN Frames Info.txt"; // Default filename for CAN_FRAME
    private static final String CAN_MESSAGE_FILENAME = "19 CANMessages.trc"; // Default filename for CAN_MESSAGE

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
        List<Pair<Float, Float>> gpsData = DataParser.getGPSData(gpsFile); // Reading and parsing gps file
        List<CANFrame> canFrameList = DataParser.getCANFrameData(canFrameFile); // Reading and parsing CANFrame file
        //Storing unique Frame IDs in Set<String> frameSet
        Set<String> frameSet = new HashSet<>();
        for (CANFrame canFrame:canFrameList)
            frameSet.add(canFrame.getFrame());
        List<CANMessage> canMessageList = DataParser.getCANMessageData(canMessageFile, frameSet); // Reading and parsing CANMessage file
        SensorOutputEstimator.computeSensorOutput(gpsData,canFrameList, canMessageList); // Computing sensor output
    }

}
