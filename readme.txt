Input files can be provided by two ways:
    * Changing the constants in the Main.java
        Constants that needs to be changed are provided below:
        - private static final String GPS_FILENAME = "19 GPS Track.htm"; // Default filename for GPS
        - private static final String CAN_FRAME_FILENAME = "CAN Frames Info.txt"; // Default filename for CAN_FRAME
        - private static final String CAN_MESSAGE_FILENAME = "19 CANMessages.trc"; // Default filename for CAN_MESSAGE
    * Provide filenames in the command line
        - Order in which filenames should be provided is GPS_file, CANFrame_file, CANMessage_file
        - Filename should not contain spaces.

If the structure of the file changes,i.e., the starting line of data and ending line of data gets changed,
then we need to update its corresponding constants in the Main.java file.
Constants are given below:
    - private static final int GPS_START_LINE = 51; // Start line to parse for GPS file
    - private static final int GPS_END_LINE = 91; // End line to parse for GPS data
    - private static final int CAN_FRAME_START_LINE = 3; // Start line to parse for CAN frame file
    - private static final int CAN_FRAME_END_LINE = 7; // End line to parse for CAN frame file
    - private static final int CAN_MSG_START_LINE = 14; // Start line to parse for CAN message file
    - private static final int CAN_MSG_END_LINE = 81600; // End line to parse for CAN message file

Console output is in "output.txt" file.
Youtube link - https://youtu.be/DYczgVDtnVk
