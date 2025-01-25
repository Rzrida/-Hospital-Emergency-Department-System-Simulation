package project;

import java.io.*;

public class ArrivalTime {
    private  String PARAMETER_FILE;
    private  String Output_FILE;

    public ArrivalTime(String pf, String of) {
    	PARAMETER_FILE = pf;
    	Output_FILE = of;
    	
    }

    // Method to calculate Weibull PDF
    private double weibullPDF(double x, double scale, double shape) {
        if (x < 0) return 0;
        double factor = (shape / scale);
        double power = Math.pow((x / scale), (shape - 1));
        double exponent = Math.exp(-Math.pow((x / scale), shape));
        double density = factor * power * exponent;

       
        return density;
    }

    // Method to find the time with maximum probability density
    private double findMaxDensityTime(double scale, double shape, double start, double end, double step) {
        double maxDensity = 0;	
        double maxTime = 0;
        double threshold = 1e-6;

        for (double x = start; x <= end; x += step) {
            double density = weibullPDF(x, scale, shape);
            if (density > threshold && density > maxDensity) {
                maxDensity = density;
                maxTime = x;
            }
        }

        return maxTime;
    }

    public void simulate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PARAMETER_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(Output_FILE))) {

            String line = reader.readLine();
            writer.write("Condition, Arrival Time (minutes)\n");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String condition = parts[0].trim();
                double scale = Double.parseDouble(parts[1].trim());
                double shape = Double.parseDouble(parts[2].trim());

                // Find max density time for this condition
                double maxDensityTime = findMaxDensityTime(scale, shape, 0, 500, 5);

                double arrivalTimeMinutes = maxDensityTime; 
                // Write to file
                writer.write(String.format("%s, %.2f\n", condition, arrivalTimeMinutes));

            }
        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in the parameter file: " + e.getMessage());
        }
    }

    

}
