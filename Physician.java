package project;

public abstract class Physician {
    protected int id;
    private int id_count = 466342;
    protected boolean [] category; // Categories of patients a physician can handle
    protected int maxSimultaneousPatients; 
    protected int currentPatientCount;
	private Patient pausePatient; // for registrar and consultant 


    public Physician(int maxSimultaneousPatients) {
        pausePatient = null;

    	category = new boolean[5];
        id = id_count++;
        this.maxSimultaneousPatients = maxSimultaneousPatients;
        this.currentPatientCount = 0;
    }

   

    public int getId() {
        return id;
    }
    public boolean isAvailable() {
    	if (currentPatientCount < maxSimultaneousPatients) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    public int getMaxSimultaneousPatients() {
        return maxSimultaneousPatients;
    }

    public int getCurrentPatientCount() {
        return currentPatientCount;
    }
    public void available() {
        if (currentPatientCount > 0) {
            currentPatientCount--;  
        } else {
            throw new IllegalStateException("No patients to discharge.");
        }
    }


    public void assignPatient() {
        if (currentPatientCount < maxSimultaneousPatients) {
            currentPatientCount++;
        } else {
            throw new IllegalStateException(" cannot handle more patients.");
        }
    }

    public void dischargePatient() {
        if (currentPatientCount > 0) {
            currentPatientCount--;
        } else {
            throw new IllegalStateException(" has no patients to discharge.");
        }
    }

    public abstract String getRole();
    
    // for registrar and consultant only 
    public void setPausePatient(Patient patient) {
    }
    public void removePausePatient() {
    }
    public boolean pausePatient() {
    	return false;
    	
    }
    public Patient getPausePatient() {
    	return pausePatient;
    }
}


