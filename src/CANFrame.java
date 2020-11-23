/**
 * POJO to store the contents of CANFrame
 * @author srini
 * @since 10/18/2020
 */
public class CANFrame {

    private String frame; //Frame id
    private String field; // Sensor field name
    private float stepSize; // Step size
    private float lowValue; // Low value of the data
    private int dataStartByte; // Data Start byte
    private int dataStartBit; // Data Start bit
    private int fieldSize; // No of bits in data
    private String unit; // unit of the value

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public float getStepSize() {
        return stepSize;
    }

    public void setStepSize(float stepSize) {
        this.stepSize = stepSize;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public float getLowValue() {
        return lowValue;
    }

    public void setLowValue(float lowValue) {
        this.lowValue = lowValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDataStartByte() {
        return dataStartByte;
    }

    public void setDataStartByte(int dataStartByte) {
        this.dataStartByte = dataStartByte;
    }

    public int getDataStartBit() {
        return dataStartBit;
    }

    public void setDataStartBit(int dataStartBit) {
        this.dataStartBit = dataStartBit;
    }
}
