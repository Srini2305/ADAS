import java.util.ArrayList;
import java.util.List;

public class CurveDetectWarning {
    public ArrayList<ArrayList<String>> curveDetect(List<CurveObject> curveObjects){
        ArrayList<ArrayList<String>> message = new ArrayList<>();
        for(int i = 0;i<curveObjects.size();i++){
            ArrayList<String> curveMessage = new ArrayList<>();
            double time;
            double distance;
            time = Float.parseFloat(String.valueOf(curveObjects.get(i).getExitOffset()-
                    curveObjects.get(i).getEntryOffset())) - 0.05;
            curveMessage.add(""+time);
            distance = computeDistance(time,curveObjects.get(i).getAverageSpeed());
            curveMessage.add(""+distance);
            message.add(curveMessage);
        }
        return message;
    }
    public double computeDistance(Double time, float speed){
        double timeSeconds = time / 3600;
        double computedDistance = Double.parseDouble(String.valueOf(speed)) * 0.05;
        return computedDistance;
    }
}
