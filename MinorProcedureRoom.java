package project;

public class MinorProcedureRoom extends Bed {

    public MinorProcedureRoom() {
        super();
        for(int i=1;i<5; i++) {
        	category[i] = true; //for category 2-5
        }
    }

    public String getBedType() {
        return "Minor Procedure Room (Category 2-5)";
    }
}