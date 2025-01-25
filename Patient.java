package project;

import java.io.*;
public class Patient {
    private int id;
    private static int id_count = 465791;
    private String condition;
    private int triageCategory;
    private long arrivalTime;
    private long TimeofBed;
    private long TimeofDischarge;
    private long ArrivaltoInpatientQueue;
    private double treatmentTime;
    private boolean inpatient;
    private int postTreatmentTime;
    private Bed assignedBed;
    private InpatientBeds Ibed;
    private Physician supervisedBy; // The physician who supervises the patient
    private Physician checkedBy; // The physician who checked the patient

    // Constructor
    public Patient() {
        this.id = id_count++;
    }

    public Patient(String condition) {
        this.condition = condition;
        this.id = id_count++;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for condition
    public String getCondition() {
        return condition;
    }

    // Setter for condition
    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Getter for triageCategory
    public int getTriageCategory() {
        return triageCategory;
    }
    public void reduceTriageCategory() {
        triageCategory--;
    }

    // Setter for triageCategory
    public void setTriageCategory(int triageCategory) {
        this.triageCategory = triageCategory;
    }

    // Getter for arrivalTime
    public long getArrivalTime() {
        return arrivalTime;
    }

    // Setter for arrivalTime
    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    // Getter for arrivalTime
    public long getInpatientTime() {
    	if(inpatient) {
    		return ArrivaltoInpatientQueue;
    	}
        return 0;
    }

    // Setter for arrivalTime
    public void setInpatientTime(long ArrivaltoInpatientQueue) {
        this.ArrivaltoInpatientQueue = ArrivaltoInpatientQueue;
    }

    // Getter for treatmentTime
    public double getTreatmentTime() {
        return treatmentTime;
    }

    // Setter for treatmentTime
    public void setTreatmentTime(double treatmentTime) {
        this.treatmentTime = treatmentTime;
    }

    // Getter for inpatient
    public boolean isInpatient() {
        return inpatient;
    }

    // Setter for inpatient
    public void setInpatient(boolean inpatient) {
        this.inpatient = inpatient;
    }

    // Getter for postTreatmentTime
    public int getPostTreatmentTime() {
        return postTreatmentTime;
    }

    // Setter for postTreatmentTime
    public void setPostTreatmentTime(int postTreatmentTime) {
        this.postTreatmentTime = postTreatmentTime;
    }

    // Getter for assigned bed
    public Bed getAssignedBed() {
        return assignedBed;
    }

    // Setter for assigned bed
    public void setInpatientBed(InpatientBeds Ibed) {
        this.Ibed = Ibed;
    }

 // Getter for Inpatient bed
    public InpatientBeds getInpatientBed() {
        return Ibed;
    }

    // Setter for Inpatient bed
    public void setAssignedBed(Bed assignedBed) {
        this.assignedBed = assignedBed;
    }
    
    // Getter for supervisedBy
    public Physician getSupervisedBy() {
        return supervisedBy;
    }

    // Setter for supervisedBy
    public void setSupervisedBy(Physician supervisedBy) {
       if(triageCategory != 1 &&triageCategory != 2 ) {
	        this.supervisedBy = supervisedBy;
    	}
       else {
    	   this.supervisedBy = null;
       }
        
    }

    // Getter for checkedBy
    public Physician getCheckedBy() {
        return checkedBy;
    }

    // Setter for checkedBy
    public void setCheckedBy(Physician checkedBy) {
    	
        this.checkedBy = checkedBy;
    }
    public int getBedID() {
    	return assignedBed.getID();
    }
    
 // Getter for TimeofBed
    public long getTimeofBed() {
        return TimeofBed;
    }

    // Setter for TimeofBed
    public void setTimeofBed(long TimeofBed) {
        this.TimeofBed = TimeofBed;
    }

    // Getter for TimeofDischarge
    public long getTimeofDischarge() {
        return TimeofDischarge;
    }

    // Setter for TimeofDischarge
    public void setTimeofDischarge(long TimeofDischarge) {
        this.TimeofDischarge = TimeofDischarge;
    }


    public void displayDetails() {
        System.out.println("ID: " + id);
        System.out.println("Patient Condition: " + condition);
        System.out.println("Triage Category: " + triageCategory);

        System.out.println("Arrival Time: " + arrivalTime);
        
        System.out.println("Time of Bed: " + TimeofBed);  
        
        if (inpatient) {
            System.out.println("Inpatient: " + (inpatient ? "Yes" : "No"));
            System.out.println("Arrival to Inpatient Queue Time: " + ArrivaltoInpatientQueue);
            System.out.println("Post-Treatment Time: " + postTreatmentTime );

        }
        System.out.println("Treatment Time: " + treatmentTime);
        System.out.println("Time of Discharge: " + TimeofDischarge);

        System.out.println("---------------------------------");
    }
    
    public void writeDetailsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) { // 'true' enables appending to the file
            writer.write("ID: " + id);
            writer.newLine();
            writer.write("Patient Condition: " + condition);
            writer.newLine();
            writer.write("Triage Category: " + triageCategory);
            writer.newLine();

            writer.write("Arrival Time: " + arrivalTime);
            writer.newLine();
            writer.write("Time of Bed: " + TimeofBed);
            writer.newLine();

            if (inpatient) {
                writer.write("Inpatient: Yes");
                writer.newLine();
                writer.write("Arrival to Inpatient Queue Time: " + ArrivaltoInpatientQueue);
                writer.newLine();
                writer.write("Post-Treatment Time: " + postTreatmentTime);
                writer.newLine();
            } else {
                writer.write("Inpatient: No");
                writer.newLine();
            }

            writer.write("Treatment Time: " + treatmentTime);
            writer.newLine();
            writer.write("Time of Discharge: " + TimeofDischarge);
            writer.newLine();

            writer.write("---------------------------------");
            writer.newLine();

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    public void writeDetailsToFile2() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Arrival_output.txt", true))) { // 'true' enables appending to the file
            writer.write("ID: " + id);
            writer.newLine();
            writer.write("Patient Condition: " + condition);
            writer.newLine();
            writer.write("Triage Category: " + triageCategory);
            writer.newLine();

            writer.write("Arrival Time: " + arrivalTime);
            writer.newLine();
            

            if (inpatient) {
                writer.write("Inpatient: Yes");
                writer.newLine();
                writer.write("Arrival to Inpatient Queue Time: " + ArrivaltoInpatientQueue);
                writer.newLine();
                writer.write("Post-Treatment Time: " + postTreatmentTime);
                writer.newLine();
            } else {
                writer.write("Inpatient: No");
                writer.newLine();
            }

            writer.write("Treatment Time: " + treatmentTime);
            writer.newLine();
            writer.write("---------------------------------");
            writer.newLine();

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public String toString() {
        return "Patient " + id + " (Priority " + triageCategory + ")";
    }
}
