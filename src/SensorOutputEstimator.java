import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * SensorOutputEstimator.java - Class for finding the required sensor data
 */
public class SensorOutputEstimator {

    List<Pair<Float, Float>> gpsData;
    List<CANFrame> canFrameList;
    List<CANMessage> canMessageList;
    List<Observer> observerList;
    SensorOutput sensorOutput;

    SensorOutputEstimator(List<Pair<Float, Float>> gpsData, List<CANFrame> canFrameList,
                          List<CANMessage> canMessageList, List<Observer> observerList){
        this.gpsData = gpsData;
        this.canFrameList = canFrameList;
        this.canMessageList = canMessageList;
        this.observerList = observerList;
        sensorOutput = new SensorOutput();
        for(Observer observer:observerList){
            sensorOutput.addObserver(observer);
        }
    }

    /**
     * For Each CANFrame we compute the sensor value using the canMessageMap. We add gps data at its specific offset.
     * @return List<SensorOutput> that contains list of SensorOutput
     */
    public void computeSensorOutput() {
        int pos = 0;
        long prev = 0;
        sensorOutput.setOffset(""+0.0);
        sensorOutput.setGpsLatitude("" + gpsData.get(pos).getKey()+"°");
        sensorOutput.setGpsLongitude("" + gpsData.get(pos).getValue()+"°");
        for(CANMessage canMessage:canMessageList){
            // Compute the sensor value for a given CANFrame and CANMessage
            for(CANFrame canFrame:canFrameList){
                if(canMessage.getFrame().equalsIgnoreCase(canFrame.getFrame())){
                    sensorOutput.setOffset(""+canMessage.getTimeOffset());
                    if(pos*1000<=canMessage.getTimeOffset() && pos<gpsData.size()){
                        sensorOutput.setGpsLatitude("" + gpsData.get(pos).getKey()+"°");
                        sensorOutput.setGpsLongitude("" + gpsData.get(pos).getValue()+"°");
                        pos++;
                    }
                    float result = compute(canFrame, canMessage);
                    switch (canFrame.getField()){
                        case "Steering wheel angle":
                            sensorOutput.setSteeringWheelAngle(""+result+canFrame.getUnit());
                            break;
                        case "Displayed vehicle speed":
                            sensorOutput.setVehicleSpeed(""+result+canFrame.getUnit());
                            break;
                        case "Vehicle yaw rate":
                            sensorOutput.setYawRate(""+result+canFrame.getUnit());
                            break;
                        case "Vehicle longitudinal acceleration (+ means forward)":
                            sensorOutput.setLongitudinalAcceleration(""+result+canFrame.getUnit());
                            break;
                        case "Vehicle lateral acceleration (+ means left)":
                            sensorOutput.setLateralAcceleration(""+result+canFrame.getUnit());
                            break;
                        default:
                    }
                    delay((long) (canMessage.getTimeOffset()-prev));
                    prev = (long) canMessage.getTimeOffset();
                }
            }
        }
        while(pos<gpsData.size()){
            sensorOutput.setGpsLatitude("" + gpsData.get(pos).getKey()+"°");
            sensorOutput.setGpsLongitude("" + gpsData.get(pos).getValue()+"°");
            delay(1000);
            pos++;
        }
    }

    private void delay(long offset) {
        try {
            TimeUnit.MILLISECONDS.sleep(offset);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Based on the given CANFrame and CANMessage the sensor value is computed
     * @param canFrame CANFrame for which we need to compute sensor value
     * @param canMessage CANMessage that contains the offset and data of the given frame
     * @return the computed sensor value
     */
    public float compute(CANFrame canFrame, CANMessage canMessage){
        float result;
        // Get the substring of data starting from the starting byte and convert it to binary string
        String binary = new BigInteger(canMessage.getData().substring(canFrame.getDataStartByte()), 16).toString(2);
        // len determines the no of leading zeroes that needs to be appended to binary data
        int len = binary.length()%8;
        /*
         * Check if len not equals to zero. It means we need to add leading zeroes
         * If true append (8-len) leading Zeros to binary data
         */
        if(len!=0){
            StringBuilder s = new StringBuilder();
            for(int i= 8-len;i>0;i--)
                s.append("0");
            s.append(binary);
            binary = s.toString();
        }
        // Extract data from binary data
        int data = Integer.parseInt(binary.substring(canFrame.getDataStartBit(),canFrame.getDataStartBit()+canFrame.getFieldSize()), 2);
        // Compute result based on formula
        result = (data*canFrame.getStepSize()) + canFrame.getLowValue();
        // Round the result to two decimal values
        return BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
