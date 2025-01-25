package project;

class SeniorResident extends Physician {
    public SeniorResident() {
        super(4); 
        for(int i=0; i<5; i++) {
        	category[i] = true; // under supervision and part of team 
        }
    }

    public String getRole() {
        return "Senior Resident";
    }
}