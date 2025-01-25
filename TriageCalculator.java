package project;

import java.io.*;
import java.util.Random;

public class TriageCalculator {
    private  Random random;

    private  final String PARAMETER_FILE = "triage_parameters.txt"; // File with parameters for each condition

    public  int calculateTriage(String conditionName) {
        double[] probabilities = readProbabilitiesFromFile(conditionName);
        if (probabilities == null) {
            return 5; 
        }

        random = new Random();

        double rand = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i + 1; // Return triage category (1-5)
            }
        }
        return 5; // Default to the lowest triage category if not assigned
    }

    private  double[] readProbabilitiesFromFile(String conditionName) {
        try (BufferedReader br = new BufferedReader(new FileReader(PARAMETER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String condition = parts[0].trim(); // The condition name

                if (condition.equalsIgnoreCase(conditionName.trim())) {

                	String[] probabilityStrings = parts[1].split(",");
                    double[] probabilities = new double[probabilityStrings.length];

                    for (int i = 0; i < probabilityStrings.length; i++) {
                        probabilities[i] = Double.parseDouble(probabilityStrings[i].trim());
                    }
                    return probabilities;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }
}
