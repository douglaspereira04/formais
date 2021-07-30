package control.automata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.automata.Automata;
import view.automata.AutomataPanel;
import view.automata.AutomataTableModel;

/**
 * Dedicated to control automata panel behavior
 * @author douglas
 *
 */
public class AutomataControl {

    private AutomataPanel automataPanel;
    private Automata automata;
    
    public AutomataControl(AutomataPanel automataPanel) {
	super();
	this.automataPanel = automataPanel;
	this.automata = null;
	initializeBehavior(); 
	
    }
    
    private void initializeBehavior() {
	automataPanel.getSaveAutomataButton().addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		onSaveButton();
	    }
	});
    }
    
    private void onSaveButton() {
	try {
	    //FileUtils.selectExportFile(System.getProperty("user.dir")+System.getProperty("file.separator")+"new.automata", automataPanel);
	    automata = this.automataPanel.getAutomata();
	    System.out.println("### AUTOMATA EXTRACTED ###");
	    System.err.println("### NOT VALIDATED ###");
	    
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}
    }
    
}
