package project;

public class ResuscitationBed extends Bed {

    public ResuscitationBed() {
        super();
        category[0] =  true;
    }

    public String getBedType() {
        return "Resuscitation Bed (Category 1 only)";
    }
}
