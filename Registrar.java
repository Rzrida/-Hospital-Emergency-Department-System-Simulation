package project;

class Registrar extends Physician {
	private Patient pausePatient;
    public Registrar() {
    	
        super(100000); 
        category[0] = true;
        category[1] = true;
        
    }

    public String getRole() {
        return "Registrar";
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