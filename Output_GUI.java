package project;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.CoolBar;

public class Output_GUI {

    protected Shell shell;
    private LocalResourceManager localResourceManager;
    private Text text;
    private Compiled comp;
    private Text text_1;
    private boolean simulationRunning = false;
    private Label lblTotalPatients;
    private Label lblTotalPatientIn;
    private Text text_2;
    private Label lblTotalPatientIn_2;
    private Text text_3;
    private Label lblTotalPhysicians;
    private Text text_4;
    private Label lblTotalBeds;
    private Label lblBedsAvailable;
    private Text text_5;
    private Text text_6;
    private Text text_7;
    private Label lblTotalPatientIn_1;
    private Text text_8;
    private Label lblTimer;
    private Label lblTotalPatientIn_3;
    private Text text_9;

    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            Output_GUI window = new Output_GUI();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        comp = new Compiled();
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        createResourceManager();
        shell.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
        shell.setSize(546, 417);
        shell.setText("SWT Application");

        Label lblWelcomeToMtr = new Label(shell, SWT.NONE);
        lblWelcomeToMtr.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
        lblWelcomeToMtr.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 20, SWT.BOLD)));
        lblWelcomeToMtr.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
        lblWelcomeToMtr.setBounds(67, 10, 414, 43);
        lblWelcomeToMtr.setText("Welcome to MTR Simulation");

        Label lblEnterTimeOf = new Label(shell, SWT.NONE);
        lblEnterTimeOf.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
        lblEnterTimeOf.setFont(localResourceManager.create(FontDescriptor.createFrom("Times New Roman", 12, SWT.BOLD)));
        lblEnterTimeOf.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
        lblEnterTimeOf.setBounds(25, 79, 168, 19);
        lblEnterTimeOf.setText("Enter Time of Simulation");

        text = new Text(shell, SWT.BORDER);
        text.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
        text.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
        text.setBounds(199, 79, 76, 21);
        
        Button btnClickToSimulate = new Button(shell, SWT.BORDER);
 
        btnClickToSimulate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            	
            	long time = Long.parseLong(text.getText());
                comp.setTime(time);
                
                lblTotalPatients = new Label(shell, SWT.NONE);
                lblTotalPatients.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblTotalPatients.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTotalPatients.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTotalPatients.setBounds(67, 163, 88, 15);
                lblTotalPatients.setText("Total Patients ");
                
                lblTotalPatientIn = new Label(shell, SWT.NONE);
                lblTotalPatientIn.setText("Total Patient in Main Queue");
                lblTotalPatientIn.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTotalPatientIn.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblTotalPatientIn.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTotalPatientIn.setBounds(10, 202, 161, 21);
                
                text_2 = new Text(shell, SWT.BORDER);
                text_2.setBounds(173, 202, 76, 21);
                
                lblTotalPatientIn_2 = new Label(shell, SWT.NONE);
                lblTotalPatientIn_2.setText("Total Patients Impatient");
                lblTotalPatientIn_2.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTotalPatientIn_2.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblTotalPatientIn_2.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTotalPatientIn_2.setBounds(25, 238, 141, 21);
                
                text_3 = new Text(shell, SWT.BORDER);
                text_3.setBounds(173, 238, 76, 21);
                text_1 = new Text(shell, SWT.BORDER);
                text_1.setBounds(173, 162, 76, 21);
                
                lblTotalPhysicians = new Label(shell, SWT.NONE);
                lblTotalPhysicians.setText("Total Physicians");
                lblTotalPhysicians.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTotalPhysicians.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblTotalPhysicians.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTotalPhysicians.setBounds(283, 163, 88, 20);
                
                text_4 = new Text(shell, SWT.BORDER);
                text_4.setBounds(392, 163, 76, 21);
                
                lblTotalBeds = new Label(shell, SWT.NONE);
                lblTotalBeds.setText("Total Beds");
                lblTotalBeds.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTotalBeds.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblTotalBeds.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTotalBeds.setBounds(283, 202, 88, 15);
                
                lblBedsAvailable = new Label(shell, SWT.NONE);
                lblBedsAvailable.setText("Total Inpatient Beds");
                lblBedsAvailable.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblBedsAvailable.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblBedsAvailable.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblBedsAvailable.setBounds(274, 239, 113, 15);
                
                text_5 = new Text(shell, SWT.BORDER);
                text_5.setBounds(392, 202, 76, 21);
                
                text_6 = new Text(shell, SWT.BORDER);
                text_6.setBounds(392, 238, 76, 21);

                text_7 = new Text(shell, SWT.BORDER);
                text_7.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA));
                text_7.setBounds(268, 332, 76, 21);
                lblTotalPatientIn_1 = new Label(shell, SWT.NONE);
                lblTotalPatientIn_1.setText("Patients Discharged");
                lblTotalPatientIn_1.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTotalPatientIn_1.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.ITALIC)));
                lblTotalPatientIn_1.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTotalPatientIn_1.setBounds(41, 282, 123, 20);
                
               
                
                lblTimer = new Label(shell, SWT.NONE);
                lblTimer.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Black", 12, SWT.BOLD)));
                lblTimer.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 255))));
                lblTimer.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(176, 122, 188))));
                lblTimer.setBounds(149, 330, 113, 38);
                lblTimer.setText("Timer .......");
                
              
                
                text_9 = new Text(shell, SWT.BORDER);
                text_9.setBounds(173, 281, 76, 21);
              
                // Start the simulation in a new thread
                if (!simulationRunning) {
                    simulationRunning = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            comp.Simm();
                        }
                    }).start();

                    // Start the counter to update text_1 every second
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (simulationRunning) {
                                try {
                                    Thread.sleep(10); // Wait for 1 second
                                    Display.getDefault().asyncExec(new Runnable() {
                                        @Override
                                        public void run() {
                                            text_1.setText(String.valueOf(comp.NumofPatients()));
                                            text_2.setText(String.valueOf(comp.getMainQueue())); 
                                            text_3.setText(String.valueOf(comp.getInpatientQueue())); 
                                            text_4.setText(String.valueOf(comp.getPhysSize()));
                                            text_5.setText(String.valueOf(comp.getBedSize())); 
                                            text_6.setText(String.valueOf(comp.getInpatientSize()));
                                            text_7.setText(String.valueOf(comp.CurrentTime()));
                                            text_9.setText(String.valueOf(comp.getDischarged()));
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        btnClickToSimulate.setForeground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(128, 0, 128))));
        btnClickToSimulate.setBounds(274, 106, 113, 25);
        btnClickToSimulate.setText("Click to Simulate");
        
       
        
        
             
    }

    private void createResourceManager() {
        localResourceManager = new LocalResourceManager(JFaceResources.getResources(), shell);
    }
}
