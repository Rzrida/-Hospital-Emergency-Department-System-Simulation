package project;

class Intern extends Physician {
    public Intern() {
        super(2); // Interns can treat 1-2 patients
        category[2] = true;
        category[3] = true;
        category[4] = true;
    }

    
    public String getRole() {
        return "Intern";
    }
}
