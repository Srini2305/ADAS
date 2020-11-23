import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataParser {

    private static final int GPS_START_LINE = 51; // Start line to parse for GPS file
    private static final int GPS_END_LINE = 91; // End line to parse for GPS data
    private static final int CAN_FRAME_START_LINE = 3; // Start line to parse for CAN frame file
    private static final int CAN_FRAME_END_LINE = 7; // End line to parse for CAN frame file
    private static final int CAN_MSG_START_LINE = 14; // Start line to parse for CAN message file
    private static final int CAN_MSG_END_LINE = 81600; // End line to parse for CAN message file

    /**
     * Reads the filename. Based on type reads the file within given line range
     * @param filename is the filename that we want to read
     * @param type defines the type of file we need to read
     * @return line by line content of the file as List<String>
     */
    public static List<String> readFile(String filename, String type){
        int start = 1, end = 1, pos = 1;
        /*
         * Based on the file type we get the start and end line that we need to read
         * We use switch statement to decide the start and line based on "type" parameter
         */
        switch (type) {
            case "GPS":
                start = GPS_START_LINE;
                end = GPS_END_LINE;
                break;
            case "CANFrame":
                start = CAN_FRAME_START_LINE;
                end = CAN_FRAME_END_LINE;
                break;
            case "CANMessage":
                start = CAN_MSG_START_LINE;
                end = CAN_MSG_END_LINE;
                break;
        }
        List<String> content = new ArrayList<>();
        // Reads the file
        try {
            File myObj = new File(filename); //File object of filename
            Scanner myReader = new Scanner(myObj);  // scanner object for File
            while (myReader.hasNextLine()) { // If file has nextLine then read content
                String data = myReader.nextLine(); //Read next line of file
                if(pos>=start && pos<=end) // if the file line is within the range then add it to the content list
                    content.add(data);
                pos++;
            }
            myReader.close();
        } catch (FileNotFoundException e) { // Exception handling if file is not found
            System.out.println("File not Found: " + e.getLocalizedMessage());
        }
        return content;
    }

    /**
     * Reads the given filename and returns gps data as list of pairs of latitude and longitude
     * @param filename is the filename that contains GPS data
     * @return List<Pair<Float, Float>> that contains the pair of latitude and longitude
     */
    public static List<Pair<Float, Float>> getGPSData(String filename){
        List<Pair<Float, Float>> gpsData = new ArrayList<>();
        List<String> content = readFile(filename, "GPS"); // Read the contents of GPS file
        for(String s:content){
            // parse the substring within the '(' and ')' characters
            String str = s.substring(s.indexOf("(")+1,s.indexOf(")"));
            String[] s1 = str.trim().split(",");//contains the longitude and latitude in string array
            // Store the longitude and latitute in the pair of floats
            Pair<Float, Float> gps = new Pair<>(Float.valueOf(s1[0]), Float.valueOf(s1[1]));
            gpsData.add(gps); // Adding pair of longitude and latitude to the list
        }
        return gpsData;
    }

    /**
     * Reads the given filename and returns List of CANFrame objects
     * @param filename is the filename that contains CANFrame data
     * @return List<CANFrame> that contains list of CANFrame object
     */
    public static List<CANFrame> getCANFrameData(String filename){
        List<CANFrame> canFrameList = new ArrayList<>();
        List<String> content = readFile(filename, "CANFrame"); // read the CANFrame file
        for(String s:content){
            //parse the string
            CANFrame canFrame = new CANFrame();
            String[] s1 = s.trim().split(";"); // Split the string by ';'
            canFrame.setFrame(s1[0]);
            String[] dataLocation = s1[1].trim().split("-"); // Split the string by '-'
            int start = (Integer.parseInt(""+dataLocation[0].charAt(2)) - 1) * 2; // Calculate starting byte
            int startBit = 7 - Integer.parseInt(""+dataLocation[0].charAt(5)); // Calculate starting bit
            canFrame.setDataStartByte(start);
            canFrame.setDataStartBit(startBit);
            canFrame.setFieldSize(Integer.parseInt(s1[2].trim().split(" ")[0])); // Parse data field size
            canFrame.setField(s1[3].trim());
            // Get and parse the Low data range value
            String[] dataRange = s1[5].trim().replace(",",".").replace("°","").split(" ");
            canFrame.setLowValue(Float.parseFloat(dataRange[0]));
            // Get and parse the step size
            String[] stepSize = s1[7].trim().replace(",",".").split(" ");
            canFrame.setStepSize(Float.parseFloat(stepSize[0]));
            canFrame.setUnit(stepSize[1]); // Set the units used in CAN Frame
            canFrameList.add(canFrame);
        }
        return canFrameList;
    }

    /**
     * Reads the given filename and returns List of CANMessage objects. Key of the map is Frame ID
     * @param filename is the filename that contains CANMessage data
     * @param frameSet is the list of frame ID for which we need to read CANMessage
     * @return  List<CANMessage> that contains list of CANMessage
     */
    public static List<CANMessage> getCANMessageData(String filename, Set<String> frameSet){
        List<CANMessage> canMessageList = new ArrayList<>();
        List<String> content = readFile(filename, "CANMessage"); // Read the CANMessage file
        for(String s:content){
            String[] s1 = s.trim().split("\\s+"); // Split the content by "\\s+" spaces
            if(frameSet.contains(s1[3].trim())) {
                //parse the string
                CANMessage canMessage = new CANMessage();
                canMessage.setTimeOffset(Float.parseFloat(s1[1])); // Parse the offset value
                canMessage.setFrame(s1[3]); // Get the frame ID
                //combine the data into single string
                StringBuilder data = new StringBuilder();
                for (int i = 5; i < s1.length; i++)
                    data.append(s1[i]);
                canMessage.setData(data.toString());
                // Get the list of CANMessage based on the frame ID
                canMessageList.add(canMessage); // Add the CANMessage to the list
            }
        }
        return canMessageList;
    }

}
