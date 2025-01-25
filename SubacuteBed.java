package project;

public class SubacuteBed extends Bed {

    public SubacuteBed() {
        super();
        for(int i=2;i<5; i++) {
        	category[i] = true; //for category 3-5
        }
        
    }

    public String getBedType() {
        return "Subacute Bed (Category 3-5)";
    }
}