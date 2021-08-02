package control.automata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.automata.Automata;
import view.automata.AutomataPanel;

/**
 * Dedicated to control automata panel behavior
 * 
 * @author douglas
 *
 */
public class AutomataControl {

    private AutomataPanel automataPanel;
    private List<Automata> automaton;
    private Automata currAutomata;

    public AutomataControl(AutomataPanel automataPanel) {
	super();
	this.automataPanel = automataPanel;
	this.automaton = new ArrayList<Automata>();

	setAutomataManipulationEnabled(false);
	initializeBehavior();

    }

    private void setAutomataManipulationEnabled(boolean enabled) {
	automataPanel.getTransitionTable().setEnabled(enabled);
	automataPanel.getSaveAutomataButton().setEnabled(enabled);
	automataPanel.getDeleteAutomataButton().setEnabled(enabled);
	automataPanel.getAddColumnButton().setEnabled(enabled);
	automataPanel.getAddRowButton().setEnabled(enabled);
	automataPanel.getClearAutomataButton().setEnabled(enabled);
	automataPanel.getCompleteAutomataButton().setEnabled(enabled);
	automataPanel.getEpsilonClosureButton().setEnabled(enabled);
	automataPanel.getUnifyAutomataButton().setEnabled(enabled);
    }

    private void initializeBehavior() {
	automataPanel.getSaveAutomataButton().addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		onSaveButton();
	    }
	});
	automataPanel.getNewAutomataButton().addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		onNewButton();
	    }
	});

	automataPanel.getAutomataComboBox().addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		onAutomataListChange();
	    }
	});
    }

    private void onSaveButton() {
	try {
	    // FileUtils.selectExportFile(System.getProperty("user.dir")+System.getProperty("file.separator")+"new.automata",
	    // automataPanel);

	    automaton.set(automataPanel.getAutomataComboBox().getSelectedIndex(), automataPanel.getAutomata());

	    onAutomataListChange();
	    System.out.println("### AUTOMATA EXTRACTED ###");
	    System.err.println("### NOT VALIDATED ###");

	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}
    }

    private void onAutomataListChange() {
	int automataIndex = -1;

	if (automataPanel.getAutomataComboBox().getItemCount() == 0) {
	    setAutomataManipulationEnabled(false);
	} else {
	    setAutomataManipulationEnabled(true);
	    automataIndex = automataPanel.getAutomataComboBox().getSelectedIndex();
	    try {
		Automata newAutomata = automaton.get(automataIndex);
		if (newAutomata == null)
		    automataPanel.getClearAutomataButton().doClick();
		else
		    changeAutomata(newAutomata);
	    } catch (IndexOutOfBoundsException e) {
		System.out.println("### NEW AUTOMATA ###");
	    }

	}
    }

    private void changeAutomata(Automata newAutomata) {
	if (currAutomata != newAutomata) {
	    currAutomata = newAutomata;
	    this.automataPanel.setAutomata(currAutomata);
	}
    }

    private void onNewButton() {
	String automataName = null;
	try {
	    automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
	    if (automataName != null) {
		automataName = automataName.trim();
		if (!automataName.equals("")) {
		    automataPanel.getAutomataComboBox().addItem(automataName);
		    automaton.add(null);
		}

	    }
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}

	onAutomataListChange();
    }
}
