package project;

class Consultant extends Physician {
	private Patient pausePatient;

    public Consultant() {
        super(100000); // assuming unlimited patients at a time
        pausePatient = null;

        category[0] = true;
        category[1] = true;
    }

    public String getRole() {
        return "Consultant";
    }
    

    public void setPausePatient(Patient patient) {
    	pausePatient = patient;
    }
    public void removePausePatient() {
    	pausePatient = null;
    }
    public boolean pausePatient() {
    	if(pausePatient != null) {
    		return true;
    	}
    	return false;
    	
    }
}