package project;

class JuniorResident extends Physician {
	
	
    public JuniorResident() {
        super(3); 
        for(int i=0; i<5; i++) {
        	category[i] = true; // under supervision and part of team 
        }
    }

    public String getRole() {
        return "Junior Resident";
    }
}