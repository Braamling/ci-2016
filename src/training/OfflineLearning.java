package training;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OfflineLearning {
	static private void iterateTrainingData(){
	    String line = "";
		String trainingFile = "resources/train_data/train.csv";
	    String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(trainingFile))) {
        	// Discard header
        	br.readLine();
            while ((line = br.readLine()) != null) {

                // Retreive datapoints
                String[] data_point = line.split(cvsSplitBy);
                System.out.println(data_point[0]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
    public static void main(String[] args) {
    	iterateTrainingData();
    }
}
