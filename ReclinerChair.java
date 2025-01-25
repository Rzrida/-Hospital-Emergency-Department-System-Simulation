package project;

public class ReclinerChair extends Bed {

    public ReclinerChair() {
        super();
        	category[2] = true; //for category 1
        
    }

    public String getBedType() {
        return "Recliner Chair (Category 2)";
    }
}