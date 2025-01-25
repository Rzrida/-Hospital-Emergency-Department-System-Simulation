package project;

import java.io.*;

public class TreatmentTime {
    private  final String PARAMETER_FILE = "triage_treatmentTimeInp.txt"; // File with parameters for each condition
    private  final String Output_FILE = "triage_time.txt"; // File with parameters for each condition

    private  double gammaFunction(double x) {
        double gammaValue = 1.0;
        if (x <= 0) {
            return Double.NaN; // Gamma function is undefined for non-positive x
        }

        double sqrtTwoPi = Math.sqrt(2 * Math.PI);
        double logGamma = (x - 0.5) * Math.log(x) - x + 0.5 * Math.log(2 * Math.PI) + 1 / (12 * x);
        gammaValue = Math.exp(logGamma);

        return gammaValue;
    }

    private  double betaFunction(double p, double q) {
        if (p <= 0 || q <= 0) {
            throw new IllegalArgumentException("Parameters p and q must be greater than 0");
        }
        return gammaFunction(p) * gammaFunction(q) / gammaFunction(p + q);
    }

    // Pearson VI PDF calculation for a given category
    private  double pearsonVIDensityFunction(double beta, double p, double q, double x) {
        if (x < 0) return 0; // PDF is zero for negative x

        double numerator = Math.pow(x / beta, p - 1) * Math.pow(1 + (x / beta), 0.64);
        double denominator = beta * betaFunction(p, q) * Math.pow(1 + (x / beta), 7.36);
        return numerator / denominator;
    }

    // Find the most likely treatment time for a given category
    public  double findMostLikelyTreatmentTime(double beta, double p, double q) {
        double maxProbability = -1; 
        double bestTime = 0;

        double stepSize = 0.1; 
        double maxX = beta * 5;

        for (double x = 0; x <= maxX; x += stepSize) {
            double probability = pearsonVIDensityFunction(beta, p, q, x);
            
            if (probability > maxProbability) {
                maxProbability = probability;
                bestTime = x;
            }
        }

        return bestTime;
    }

    public void loadParametersAndProcess() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PARAMETER_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(Output_FILE))) {

            String line;

            reader.readLine();

            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String category = parts[0].trim();
                    double beta = Double.parseDouble(parts[1].trim());
                    double p = Double.parseDouble(parts[2].trim());
                    double q = Double.parseDouble(parts[3].trim());

                    double mostLikelyTreatmentTime = findMostLikelyTreatmentTime(beta, p, q);

                    if (firstLine) {
                        writer.write("Category, Time");
                        writer.newLine();
                        firstLine = false;
                    }

                    writer.write(String.format("%s,%d", category, (int) mostLikelyTreatmentTime)); // No decimals
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number in file: " + e.getMessage());
        }
    }



    
}
