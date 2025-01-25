package project;

import java.io.*;

public class NursingProcedure {
    private final String procedure_Capacity = "treatment_Capacity.txt"; 
    private int capacity; 
    private int currentUsage;

    public NursingProcedure() {
        this.currentUsage = 0;
        readCapacityFromFile(procedure_Capacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentUsage() {
        return currentUsage;
    }

    public boolean isCapacityFull() {
        return currentUsage >= capacity;
    }

    public boolean addUsage() {
        if (!isCapacityFull()) {
            currentUsage++;
            return true; 
        }
        return false;
    }

    public boolean reduceUsage() {
        if (currentUsage > 0) {
            currentUsage--;
            return true; 
        }
        return false; 
    }

    public void readCapacityFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) { 
                line = line.trim();
                if (line.toLowerCase().contains("nursing procedures")) {
                    String[] parts = line.split(","); 
                    if (parts.length > 1) {
                        this.capacity = Integer.parseInt(parts[1].trim()); 
                        return; 
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading capacity from file: " + e.getMessage());
            this.capacity = 0; 
        }
    }

    

}
