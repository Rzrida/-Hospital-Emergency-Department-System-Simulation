package project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InpatientQueue {

    private List<Patient> queue;

    public InpatientQueue() {
        queue = new ArrayList<>();
    }

    public void arrive(Patient patient) {
        queue.add(patient);

        // Sort the queue based on inpatient time
        queue.sort(new Comparator<Patient>() {
            
            public int compare(Patient p1, Patient p2) {
                return Long.compare(p1.getInpatientTime(), p2.getInpatientTime());
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
            return queue.remove(0);
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

        System.out.println("========= Current Queue Status =========");
        for (Patient patient : queue) {
            patient.displayDetails(); 
        }
    }

    public boolean contains(Patient patient) {
        return queue.contains(patient);
    }
}
