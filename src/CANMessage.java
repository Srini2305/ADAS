/**
 * CANMessage.java - POJO to store the contents of CANMessage
 */
public class CANMessage {

    private float timeOffset; // offset of data
    private String frame; // Frame id
    private String data; // CAN data

    public float getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(float timeOffset) {
        this.timeOffset = timeOffset;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
