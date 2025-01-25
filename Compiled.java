package project;


import java.io.*;
import java.util.*;

public class Compiled {
	
	// reading files
    private String ARRIVAL_Time_FILE; // File with arrival times for each condition
    private final String Triage_Treatment_Time; // File with triage time 
    private final String beds; // File with beds and their division
    private final String physcians; // File with  Physicians and their division
    private String procedure_Time; // time for nursing , laboratory , senior and procedure
    // running on time
    private  long EndTime; // end time of simulation
    private  long currentTime; // current time of simulation
    
    
    private  List<Patient> patients; // list of all the patients 
    private List<Physician> phys; // contain registrar and consultant only 
    
    private  Queues mainQueue; // Added MainQueue object
    private  Queues subQueue;  // patient enters in this when they leave main Queue till they are discharged .. its just to keep the track of the patients inside the ED 
    private  Queues procedureQueue; // Queues outside the procedure room  
    private  Queues laboratoryQueue;
    private  InpatientQueue Inqueue; // inpatient queue depending on time 
    
    // array of beds & physicians length and initialized to their sub categories division.. (Reading from file)
    private Bed[] allBeds;
    private InpatientBeds[] inpatient; // number of inpatient beds set in an array
    private int bCount; //inpatient bed count 
    private Physician[] allPhysicians;
    private int disch;
    
    // Treatment time of each Triage (read from file)
    private double[] triageTreatmentTimes;
    
    // procedure time  in order of 1. senior Consultation 2. Laboratory 3. Treatment Procedures 4. Nursing Procedures
    
    private double[] Time_procedure;
    
    Laboratory lab;
    NursingProcedure nursing;

    public Compiled() {
    	// reading files
        Triage_Treatment_Time = "triage_time.txt"; // File with triage time 
        beds = "beds.txt"; // File with beds and their division
        physcians = "Physician.txt"; // File with  Physicians and their division
        procedure_Time = "procedure_times.txt"; // time for nursing , laboratory , senior and procedure
        
        patients = new ArrayList<>();
        phys= new ArrayList<>();
        
        mainQueue = new Queues();
        subQueue = new Queues();
        procedureQueue = new Queues();
        laboratoryQueue = new Queues();
        Inqueue = new InpatientQueue();
        
        lab = new Laboratory();
        nursing = new NursingProcedure();
        
        bCount = 50;
        inpatient = new InpatientBeds[bCount]; // initially 50 inpaitent beds available ..change yahan sae kersaktae haen no need of file for 1 integer 
    	disch = 0;
    }
    public void Simm() {
    	
    	
    	// reading from file and storing in array 
        bed_div();
        physician_div();
        
        // set Procedure time of each step 
        setProcedureTimes();        

        // simulate and calculate treatment time for each triage
        TreatmentTime treatmentTime = new TreatmentTime();
        treatmentTime.loadParametersAndProcess();

        // pddt
        PDDT pddt = new PDDT();
        pddt.Saving();

        long startTime = System.currentTimeMillis(); // Get the starting time in milliseconds

        System.out.println("Starting simulation...");

        // calculating treatment time of each triage and saving in a file and 
        triageTreatmentTime();

        while (true) {
            EnterInED();

        	boolean x = true;
        	if(x) { //day
                ARRIVAL_Time_FILE = "category_arrival_time_day.txt"; // File with arrival times for each condition
        		ArrivalTime arrivalTime = new ArrivalTime("condition_parameters_day.txt",ARRIVAL_Time_FILE );
                arrivalTime.simulate();
        	}
        	else { //night
                ARRIVAL_Time_FILE = "category_arrival_time_night.txt"; // File with arrival times for each condition
        		ArrivalTime arrivalTime = new ArrivalTime("condition_parameters_night.txt",ARRIVAL_Time_FILE );
                arrivalTime.simulate();
        	}
        	if(currentTime %(12*60) == 0) {
        		x = !x;
        	}
        	
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Error in sleep: " + e.getMessage());
            }

            currentTime = (System.currentTimeMillis() - startTime) / 1000;

            if (currentTime >= EndTime) break; 
            
            //logic to assign inpatient beds to inpatient 
            if (Inqueue != null && !Inqueue.isEmpty()) {
                for (int i = 0; i < bCount; i++) {
                    if (inpatient[i]!= null && inpatient[i].isAvailable) {
                        Patient pat = Inqueue.getQueue().get(0); 
                        pat.setInpatientBed(inpatient[i]); 
                        long t = pat.getPostTreatmentTime();
                        try {
                            Thread.sleep((long) (t * 1000)); 
                            pat.setTimeofDischarge(currentTime); // Set the discharge time
                            pat.writeDetailsToFile();
                            inpatient[i].setisAvailable(); 
                            Inqueue.removePatient(pat); 
                            subQueue.removePatient(pat);
                            disch++;
                        } catch (InterruptedException e) {
                            System.err.println("Error in sleep: " + e.getMessage());
                        }
                        
                    }
                }
            } 
        	

            
            //  patient arrivals from the file at the specified arrival time
            try (BufferedReader reader = new BufferedReader(new FileReader(ARRIVAL_Time_FILE))) {
                String line;
                boolean isFirstLine = true; 

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length < 2) continue; 

                    String conditionName = parts[0].trim();
                    String arrivalTimeString = parts[1].trim();

                    try {
                        double arrivalTimeInMinutes = Double.parseDouble(arrivalTimeString);

                        //double arrivalTimeInSeconds = (double) (arrivalTimeInMinutes);

                        if (currentTime % arrivalTimeInMinutes == 0 ) {

                            // Create a new patient object for the condition
                            Patient patient = new Patient(conditionName);
                            patient.setArrivalTime(currentTime);
                            patients.add(patient); 

                            // Simulate treatment time processing and triage assignment
                            TriageCalculator t = new TriageCalculator();
                            int triageCategory = t.calculateTriage(patient.getCondition());
                            patient.setTriageCategory(triageCategory);
                            mainQueue.arrive(patient); // Add the patient to the priority queue
                            patient.setTreatmentTime(triageTreatmentTimes[triageCategory - 1]);

                            PDDT_check(patient);
                            waiting_time(patient);
                            
                            patient.writeDetailsToFile2();
                            EnterInED();

                                             
                        }
                        
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading arrival time file: " + e.getMessage());
            }
            
        
        
    } 
        }
        	
        
    
    
    // Functions ....
    
    public void EnterInED() {
    	if (!mainQueue.isEmpty()) {
    	    Patient firstPatient = mainQueue.getQueue().get(0);
    	    int tr = firstPatient.getTriageCategory();
    	    boolean a2 = false, a3 = false;
    	    
    	    Bed b = null;
    	    Physician p;

    	    // Physician assignment logic
    	    for (Physician phy2 : allPhysicians) {
    	        if ((phy2 instanceof SeniorResident || phy2 instanceof JuniorResident)  &&
    	            phy2.getMaxSimultaneousPatients() > phy2.getCurrentPatientCount()) {
    	            a2 = true;
    	            break;
    	        }
    	        if (!a2 && tr == 5 && (phy2.getMaxSimultaneousPatients() > phy2.getCurrentPatientCount()) && phy2 instanceof Intern) {
    	            a2 = true;
    	        }
    	    }

    	    // Bed assignment logic
    	    for (Bed bed : allBeds) {
    	        if (bed.isAvailable()) {
    	            if ((bed instanceof ResuscitationBed && tr == 1) ||
    	                (bed instanceof AcuteBed && tr >= 2 && tr <= 5) ||
    	                (bed instanceof SubacuteBed && tr >= 3 && tr <= 5) ||
    	                (bed instanceof MinorProcedureRoom && tr >= 2 && tr <= 5)) {
    	                a3 = true;
    	                b =bed;
    	                break;
    	            }
    	        }
    	    }

    	    // If no bed found, check alternatives
    	    if (!a3) {
    	        for (Bed bed : allBeds) {
    	            if (bed.isAvailable()) {
    	                if (tr == 1 && bed instanceof Stretchers) {
    	                    a3 = true;
    	                    break;
    	                }
    	                if (tr == 2 && bed instanceof ReclinerChair) {
    	                    a3 = true;
    	                    break;
    	                }
    	            }
    	        }
    	    }

    	    // Main queue to subqueue logic if both physician and bed are available
    	    if ((a2 || (tr == 1 || tr == 2)) && a3) {
    	        subQueue.arrive(firstPatient);
    	        mainQueue.removePatient(firstPatient);
    	        b.assignPatient(firstPatient);
    	        firstPatient.setTimeofBed(currentTime);
    	        firstPatient.setAssignedBed(b); 
    	        assignPatientToPhysician(firstPatient);
    	        startTreatment(firstPatient);
    	    }
    	

    	  // useless condition 
    	    else {
    	    	if (!mainQueue.isEmpty() && !subQueue.isEmpty()) {
            	    Patient lastPatient = subQueue.getQueue().get(subQueue.getQueue().size() - 1); 
            	    
            	    if (firstPatient != null && lastPatient != null) {
            	        int tr3 = firstPatient.getTriageCategory();
            	        int tr2 = lastPatient.getTriageCategory();
            	        
            	        // Check if firstPatient has higher priority (lower triage number)
            	        if (tr2 > tr3 && (tr3 == 1 || tr3 == 2)) {
            	            for (Bed bed : allBeds) {
            	                if (bed.isAvailable()) {
            	                    
            	                        // Free the bed and physician
            	                        Physician supervisedBy = pauseSupervisor(lastPatient);
            	                        lastPatient.setTreatmentTime(triageTreatmentTimes[tr2-1] + triageTreatmentTimes[tr3-1] ); //increasing treatment of paused patient
            	                        
            	                        // Assign bed and physician to the higher-priority patient
            	                        b.assignPatient(firstPatient);
            	            	        firstPatient.setTimeofBed(currentTime);
            	            	        firstPatient.setAssignedBed(b); 
            	                        assignPatientToPhysician(firstPatient);
            	                        subQueue.arrive(firstPatient);
            	            	    	mainQueue.removePatient(firstPatient);
            	                        break; 
            	                    
            	                }
            	            }
            	        }
            	    }
            	}
    	    }           
	   }
    	
    	 
}
    public void triageTreatmentTime() {
    	triageTreatmentTimes = new double[5];

        try (BufferedReader triageReader = new BufferedReader(new FileReader(Triage_Treatment_Time))) {
            String triageLine;
            boolean isHeader = true;
            while ((triageLine = triageReader.readLine()) != null) {
                if (isHeader || triageLine.trim().isEmpty()) {
                    isHeader = false; 
                    continue;
                }

                if (!triageLine.contains(",")) continue;

                String[] triageParts = triageLine.split(",");
                if (triageParts.length < 2) continue;

                // Trim and parse category and treatment time
                String triageCategoryStr = triageParts[0].trim();
                String treatmentTimeStr = triageParts[1].trim();

                try {
                    int triageCategory = Integer.parseInt(triageCategoryStr.replace("Triage ", "").trim()) - 1; 
                    double treatmentT = Double.parseDouble(treatmentTimeStr);
                    triageTreatmentTimes[triageCategory] = treatmentT; 
                } catch (NumberFormatException e) {
                    System.err.println("Invalid treatment time format: " + treatmentTimeStr);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading triage time file: " + e.getMessage());
        }
    }
    
    
        public  void bed_div() {
        int bedIndex = 0;
        int totalBeds = 0;        
        try (BufferedReader reader = new BufferedReader(new FileReader(beds))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String bedType = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    if (bedType.equals("Total Beds")) {
                        totalBeds = count; // Get the total number of beds
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        allBeds = new Bed[totalBeds];
    	try (BufferedReader reader = new BufferedReader(new FileReader(beds))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String text = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());

                    // Create the specified number of beds based on the bed type
                    for (int i = 0; i < count; i++) {
                        if (text.equals("SubacuteBed")) {
                            allBeds[bedIndex++] = new SubacuteBed();
                        } else if (text.equals("AcuteBed")) {
                            allBeds[bedIndex++] = new AcuteBed();
                        } else if (text.equals("MinorProcedureRoom")) {
                            allBeds[bedIndex++] = new MinorProcedureRoom();
                        } else if (text.equals("ResuscitationBed")) {
                            allBeds[bedIndex++] = new ResuscitationBed();
                        } else if (text.equals("Stretchers")) {
                            allBeds[bedIndex++] = new Stretchers();
                        } else if (text.equals("ReclinerChairs")) {
                            allBeds[bedIndex++] = new ReclinerChair();
                        }
                                            }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public  void physician_div() {
        int physicianIndex = 0;
        
        int totalPhysicians = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(physcians))) {
            String line;

            // First pass to calculate total physicians
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String physicianType = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    if (physicianType.equals("Total Physicians")) {
                        totalPhysicians = count; // Get the total number of physicians
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Initialize the Physician array
        allPhysicians = new Physician[totalPhysicians];
     

        try (BufferedReader reader = new BufferedReader(new FileReader(physcians))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String physicianType = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());

                    // Create the specified number of physicians based on the type
                    for (int i = 0; i < count; i++) {
                        if (physicianType.equals("Intern")) {
                            allPhysicians[physicianIndex++] = new Intern();
                        } else if (physicianType.equals("JuniorResident")) {
                            allPhysicians[physicianIndex++] = new JuniorResident();
                        } else if (physicianType.equals("SeniorResident")) {
                            allPhysicians[physicianIndex++] = new SeniorResident();
                        } 
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        
    public  void assignPatientToPhysician(Patient patient) {
    	
    	
        int triageCategory = patient.getTriageCategory();
        // ager triage 1 ya 2 yae to supervisor automatically set hojai ga consultant 
        if ((triageCategory == 1 || triageCategory == 2)) {
        	Physician c = new Consultant(); // consultant always available 
            c.assignPatient();
            patient.setSupervisedBy(c);
            phys.add(c);
        }
        boolean x = false, y = false;
        
        for (Physician phy : allPhysicians) {
            
                // for senior resident and junior resident ... wo check kernae k liyae available hon gae triage 1-5 (but its not necessary for triage 1 and 2 
                 if ((phy instanceof SeniorResident || phy instanceof JuniorResident) && (triageCategory == 3 || triageCategory == 4 || triageCategory == 5 || triageCategory == 1 || triageCategory == 2)&& 
                         phy.getMaxSimultaneousPatients() > phy.getCurrentPatientCount()) {
                    phy.assignPatient();
                    patient.setCheckedBy(phy);
                    y = true;
                
                }
                // Intern k liyae triage 5 ko check keraen gae aur set keraen gae ager senior resident aur junior resident available nahi haen 
                else if (phy instanceof Intern && triageCategory == 5 && 
                        phy.getMaxSimultaneousPatients() > phy.getCurrentPatientCount()) {
                    phy.assignPatient();
                    patient.setCheckedBy(phy);

                    // Find a Senior Resident, Junior Resident to supervise interns to check 
                    for (Physician phy2 : allPhysicians) {
                        if ((phy2 instanceof SeniorResident || phy2 instanceof JuniorResident ) && 
                            phy2.getMaxSimultaneousPatients() > phy2.getCurrentPatientCount()) {
                            phy2.assignPatient();
                            patient.setSupervisedBy(phy2);
                            x = true;
                        }
                    }
                }
                
            }
        // ager triage 5 hae aur supervisor nahi mil raha to Registrar always available 
        if(!x && triageCategory ==5) {
        	Physician c = new Registrar(); // Registrar always available 
            c.assignPatient();
            patient.setSupervisedBy(c);
            phys.add(c);

        }
        // triage 3-5 ko ager check kernae wala hae to Registrar supervise kerae ga always 
        if(y && (triageCategory == 3 || triageCategory == 4 || triageCategory == 5 )) {
        	Physician c = new Registrar(); // Registrar always available 
            c.assignPatient();
            patient.setSupervisedBy(c);
            phys.add(c);
        }

        }

    public void PDDT_check(Patient patient) {
        String postTreatmentDecisionFile = "PDDT_results.txt";

        try (BufferedReader pddtReader = new BufferedReader(new FileReader(postTreatmentDecisionFile))) {
            String pddtLine;

            while ((pddtLine = pddtReader.readLine()) != null) {
                String[] pddtParts = pddtLine.split(",");

                if (pddtParts.length < 3) {
                    continue;
                }

                String condition = pddtParts[0].trim();
                int triageCat;
                int pddtValue;

                try {
                    triageCat = Integer.parseInt(pddtParts[1].trim());
                    pddtValue = (int) Double.parseDouble(pddtParts[2].trim());
                } catch (NumberFormatException e) {
                    continue; // Skip invalid entries
                }

                // Check if the condition and triage category match the patient's condition and triage category
                if (condition.equals(patient.getCondition()) && triageCat == patient.getTriageCategory()) {
                    patient.setPostTreatmentTime(pddtValue);
                    patient.setInpatient(true);
                    break; // Exit the loop once the correct PDDT value is found
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading PDDT file: " + e.getMessage());
        }
    }

    public  void processDischarge(Patient patient) {
    	
    	if(currentTime >= EndTime) {
    		return;
    	}
        if (patient.isInpatient()) {
            //System.out.println("Action: Admit to Inpatient Bed.");
            freeBedDoctor(patient);
            patient.setInpatientTime(currentTime);
            Inqueue.arrive(patient);
           
        } else if (patient.getTriageCategory() == 1) {
            if (isHighRiskCondition(patient.getCondition())) {
                freeBedDoctor(patient);
            } else {
                freeBedDoctor(patient);
                try {
                    Thread.sleep((long) (7000)); //  observation unit for 5 min

                    try {
                        Thread.sleep((long) (patient.getTreatmentTime() * 1000));
                        
                        patient.setTimeofDischarge(currentTime);
                        patient.writeDetailsToFile();
                        freeBedDoctor(patient);
                        subQueue.removePatient(patient);
                        disch++;
                    } catch (InterruptedException e) {
                        System.err.println("Error in sleep: " + e.getMessage());
                    }
                } catch (InterruptedException e) {
                    System.err.println("Error in sleep: " + e.getMessage());
                }
                
            }
        } else if (patient.getTriageCategory() == 0) {
        	 try {
                 Thread.sleep((long) (patient.getTreatmentTime() * 1000));
                 patient.setTimeofDischarge(currentTime);
                 patient.writeDetailsToFile();
                 freeBedDoctor(patient);
                 subQueue.removePatient(patient);
                 disch++;
             } catch (InterruptedException e) {
                 System.err.println("Error in sleep: " + e.getMessage());
             }
            
        } else {
            //System.out.println("Action: Patient discharged to Home.");
        	try {
                Thread.sleep((long) (patient.getTreatmentTime() * 1000));
                patient.setTimeofDischarge(currentTime);
                patient.writeDetailsToFile();
                subQueue.removePatient(patient);
                freeBedDoctor(patient);
                disch++;
            } catch (InterruptedException e) {
                System.err.println("Error in sleep: " + e.getMessage());
            }
        }
    }

    
    //  function to check for high-risk conditions
    private   boolean isHighRiskCondition(String condition) {
        return condition.equalsIgnoreCase("Pain") ||
               condition.equalsIgnoreCase("Renal") ||
               condition.equalsIgnoreCase("DNW Prior to Triage") ||
               condition.equalsIgnoreCase("ENT/Oral") ||
               condition.equalsIgnoreCase("Environmental/Temperature/MISC");
    }
    
    private Physician pauseSupervisor(Patient patient) {
    	for (Physician phy : allPhysicians ) {
    		if (patient.getSupervisedBy() == phy) {
    			phy.setPausePatient(patient);
    	    	 return phy;
    	    	}
    	}
    	return null;
    }
    private  void freeBedDoctor(Patient patient){
    	for (Physician phy : allPhysicians ) {
    		
    		if (patient.getCheckedBy() == phy) {
    	    	
    	    	 phy.available();
    	    	 break;
    	    	}
    	    
    	}
        for (Physician phy : allPhysicians ) {
    		if (patient.getSupervisedBy() == phy) {
    			if(phy.pausePatient()) {
    				Patient p = phy.getPausePatient();
    				phy.removePausePatient();
    				p.setSupervisedBy(phy);
    			}
    			else {
    	    	 phy.available();
    	    	 break;
    	    	}
    		}
    	}  	
    	for (Bed bed : allBeds) {
    		if (patient.getAssignedBed() == bed) {
    			bed.setisAvailable();
    			break;
    		}
    	}    	             
    }

    public void startTreatment(Patient patient) {
        long[] elapsedTime = {0}; 
        long[] totalTime = {0};
        totalTime[0] = (long) patient.getTreatmentTime(); 

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                elapsedTime[0]++;
            	totalTime[0] = (long) patient.getTreatmentTime();

                // Stop the timer if treatment time is complete
                if (elapsedTime[0] >= totalTime[0]) {
                	timer.cancel();
                }
               
                
                    try {
                        if (patient.getTriageCategory() == 1 || patient.getTriageCategory() == 2) {
                            seniorPhysicianConsult();
                            boolean x = lab.addUsage();
                            if (x) {
                                performLaboratoryTests(patient);
                                laboratoryQueue.arrive(patient);
                                performTreatment();
                            }
                            boolean y = nursing.addUsage(); 
                            if (y) {
                                performNursingProcedures(patient);
                            }
                        } else {
                            performTreatment();
                            boolean y = nursing.addUsage();
                            if (y) {
                                performNursingProcedures(patient);
                            }
                        }

                        patient.setTreatmentTime(totalTime[0] * 2); // Doubling treatment time if no space

                        processDischarge(patient); 

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        timer.cancel();
                    }
                }
            
        }, 0, 1000); // Start immediately, run every 1 second
    }

    // Methods to simulate each stage of treatment
    private void seniorPhysicianConsult() {
        try {
            Thread.sleep((long) (Time_procedure[0] * 1000));
        } catch (InterruptedException e) {
            System.err.println("Error in sleep: " + e.getMessage());
        }
        //System.out.println("Step 1: Senior Physician Consultation Done after " + Time_procedure[0]);
    }

    private void performLaboratoryTests(Patient patient) {
        try {
            Thread.sleep((long) (Time_procedure[1] * 1000));
            laboratoryQueue.removePatient(patient);
            lab.reduceUsage();
        } catch (InterruptedException e) {
            System.err.println("Error in sleep: " + e.getMessage());
        }
        
        
        //System.out.println("Step 2: Performing Laboratory Tests after " + Time_procedure[1]);
    }

    private void performTreatment() {
        try {
            Thread.sleep((long) (Time_procedure[2] * 1000));
        } catch (InterruptedException e) {
            System.err.println("Error in sleep: " + e.getMessage());
        }
        //System.out.println("Step 3: Starting Treatment Procedures after " + Time_procedure[2]);
    }

    private void performNursingProcedures(Patient patient) {
        try {
            Thread.sleep((long) (Time_procedure[3] * 1000));
            procedureQueue.removePatient(patient);
            nursing.reduceUsage(); // Assuming nursing is defined elsewhere
        } catch (InterruptedException e) {
            System.err.println("Error in sleep: " + e.getMessage());
        }
       
        //System.out.println("Step 4: Conducting Nursing Procedures after " + Time_procedure[3]);
    }

    
    public void waiting_time(Patient patient) {
        long totalTime ; 
        if(patient.getTriageCategory() == 5) {
        	totalTime = 120;
        }
        else if(patient.getTriageCategory() == 4) {
        	totalTime = 60;
        }
        else if(patient.getTriageCategory() == 4) {
        	totalTime = 30;
        }
        else if(patient.getTriageCategory() == 4) {
        	totalTime = 10;
        }
        else {
        	totalTime = 0;
        }
        long[] elapsedTime = {0}; 

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                elapsedTime[0]++; 
                if(EndTime+elapsedTime[0] >= currentTime) {
                    timer.cancel();
                    return ;
                }
                if(!mainQueue.contains(patient)) {
                	timer.cancel();
                }
                if (elapsedTime[0] >= totalTime ) {
                    try {
                    	
                    	if(patient.getAssignedBed() == null) {
                    		patient.reduceTriageCategory();
                    		patient.setTreatmentTime(triageTreatmentTimes[patient.getTriageCategory()-1]);
                    		PDDT_check(patient);
                            System.out.println("Triage category reduced due to no action");
                    	}
                        
                    } catch (Exception e) {
                        //System.out.println("Error during treatment: " + e.getMessage());
                    } finally {
                        timer.cancel();
                    }
                }
            }
        }, 0, 1000); // Start immediately, run every 1 second 
    }
    
    public void setProcedureTimes() {
        Time_procedure = new double[4];  
        try (BufferedReader br = new BufferedReader(new FileReader(procedure_Time))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();


                // Check for each procedure and save time in the correct index
                if (line.toLowerCase().contains("senior consultation")) {
                    String[] parts = line.split(",");
                    if (parts.length > 1) {
                        Time_procedure[0] = Integer.parseInt(parts[1].trim()); 
                    }
                } else if (line.toLowerCase().contains("laboratory")) {
                    String[] parts = line.split(",");
                    if (parts.length > 1) {
                        Time_procedure[1] = Integer.parseInt(parts[1].trim()); 
                    }
                } else if (line.toLowerCase().contains("treatment procedure")) {
                    String[] parts = line.split(",");
                    if (parts.length > 1) {
                        Time_procedure[2] = Integer.parseInt(parts[1].trim()); 
                    }
                } else if (line.toLowerCase().contains("nursing procedure") || line.toLowerCase().contains("nursingprocedure")) {
                    String[] parts = line.split(",");
                    if (parts.length > 1) {
                        Time_procedure[3] = Integer.parseInt(parts[1].trim()); 
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading procedure times from file: " + e.getMessage());
        }
    }


    // setters
    
    public void setTime(long t) {
    	EndTime = t;
    }
    
    // getters 
    
    public int NumofPatients() {
    	return patients.size();
    }
    
    public int getMainQueue() {
    	return mainQueue.size();
    }
    
    public int getInpatientQueue() {
    	return Inqueue.size();
    }
    public int getPhysSize() {
    	return allPhysicians.length;
    }
    public int getBedSize() {
    	return allBeds.length;
    }
    public int getInpatientSize() {
    	return inpatient.length;
    }
    
    public long CurrentTime() {
    	return currentTime;
    }
    
    
    public int getDischarged() {
    	return disch;
    }
   
}