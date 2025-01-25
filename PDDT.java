package project;

import java.io.*;
import java.util.*;

public class PDDT {
    private  final String PARAMETER_FILE = "PDDT_conditions.txt"; // File with parameters for each condition
    private  final String OUTPUT_FILE = "PDDT_results.txt"; // File to save the output

    static class Condition {
        String name;
        int triageLevel;
        double meanPDDT;

        public Condition(String name, int triageLevel, double meanPDDT) {
            this.name = name;
            this.triageLevel = triageLevel;
            this.meanPDDT = meanPDDT;
        }
    }

    public  Condition[] readConditionsFromFile() throws IOException {
        List<Condition> conditions = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(PARAMETER_FILE));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid line format: " + line);
            }
            String name = parts[0].trim();
            int triageLevel = Integer.parseInt(parts[1].trim());
            double meanPDDT = Double.parseDouble(parts[2].trim());
            conditions.add(new Condition(name, triageLevel, meanPDDT));
        }
        reader.close();
        return conditions.toArray(new Condition[0]);
    }

    public  double calculatePDF(double x, double mean) {
        if (mean <= 0) {
            throw new IllegalArgumentException("Mean PDDT must be greater than zero.");
        }
        return (1.0 / mean) * Math.exp(-x / mean);
    }

    public  double findOptimalPDDT(double mean) {
        if (mean <= 0) {
            return -1; 
        }

        double peakPDF = 1.0 / mean; 
        double threshold = 0.1 * peakPDF; 
        double optimalPDDT = 0.0;

        for (double x = 0; x <= 5 * mean; x += mean / 100) {
            double pdf = calculatePDF(x, mean);

            if (pdf < threshold) {
                optimalPDDT = x;
                break;
            }
        }

        return optimalPDDT;
    }
    
    public void Saving() {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            // Read conditions from the file
            Condition[] conditions = readConditionsFromFile();

            // Write header to file
            writer.write("Condition  Triage, Optimal PDDT\n");

            // Iterate over conditions and determine PDDT
            for (Condition condition : conditions) {
                double optimalPDDT = findOptimalPDDT(condition.meanPDDT); // Find the PDDT

                String formattedPDDT = String.format("%.0f", optimalPDDT);

                String outputLine = condition.name + ", " + condition.triageLevel + ", " + formattedPDDT;
                writer.write(outputLine + "\n");
            }

        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
    }

}