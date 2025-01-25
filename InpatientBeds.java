package project;

public class InpatientBeds  {
	private int id;
    private int id_count = 474121;
    private int bed_count;
    protected boolean isAvailable;
    protected Patient assignedPatient; 

    public InpatientBeds() {
        id = id_count++;
        this.isAvailable = true; 
        this.assignedPatient = null;
    }
    public int getID() {
    	return id;
    }
    public void setisAvailable() {
        isAvailable = true;
    }
    public boolean isAvailable() {
    	
       return isAvailable;
    }

    public void assignPatient(Patient patient) {
        if (isAvailable) {
            assignedPatient = patient;
            isAvailable = false;
        } else {
            throw new IllegalStateException("Bed is already occupied.");
        }
    }

    public void dischargePatient() {
        if (!isAvailable) {
            assignedPatient = null;
            isAvailable = true;
        } else {
            throw new IllegalStateException("Bed is already vacant.");
        }
    }

    public Patient getAssignedPatient() {
        return assignedPatient;
    }

    public  String getBedType() {
    	return "Inpatient Bed";
    }


}
