package project;

public class AcuteBed extends Bed {

    public AcuteBed() {
        super();
        for(int i=1;i<5; i++) {
        	category[i] = true; //for category 2-5
        }
    }

    public String getBedType() {
        return "Acute Bed (Category 2-5)";
    }
}