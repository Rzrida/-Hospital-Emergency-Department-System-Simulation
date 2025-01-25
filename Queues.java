package project;

import java.util.*;

public class Queues  {

    private List<Patient> queue;

    public Queues() {
        queue = new ArrayList<>();
    }

    public void arrive(Patient patient) {
        queue.add(patient);
        
        // Manually sort the list based on triage category
        queue.sort(new Comparator<Patient>() {
            public int compare(Patient p1, Patient p2) {
                return Integer.compare(p1.getTriageCategory(), p2.getTriageCategory());
            }
        });
    }
    public List<Patient> getQueue() {
        return queue; // Return the list
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Patient attendNext() {
        if (!queue.isEmpty()) {
            Patient nextPatient = queue.remove(0); 
            return nextPatient;
        } else {
            System.out.println("No patients in the queue.");
            return null;
        }
    }
    
    public int size() {
        return queue.size();
    }
    
    public void removePatient(Patient patient) {
        queue.remove(patient);
    }


    public void display() {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }

        System.out.println("=========Current queue status:=============");
        
        for (Patient patient : queue) {
            patient.displayDetails(); 
        }
    }
    
    public boolean contains(Patient patient) {
        return queue.contains(patient); 
    }

	
}
