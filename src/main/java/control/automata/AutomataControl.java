package control.automata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
		this.automataPanel.getTransitionTable().setEnabled(enabled);
		this.automataPanel.getSaveAutomataButton().setEnabled(enabled);
		this.automataPanel.getDeleteAutomataButton().setEnabled(enabled);
		this.automataPanel.getAddColumnButton().setEnabled(enabled);
		this.automataPanel.getAddRowButton().setEnabled(enabled);
		this.automataPanel.getClearAutomataButton().setEnabled(enabled);
		this.automataPanel.getCompleteAutomataButton().setEnabled(enabled);
		this.automataPanel.getEpsilonClosureButton().setEnabled(enabled);
		this.automataPanel.getUnifyAutomataButton().setEnabled(enabled);
	}

	private void initializeBehavior() {
		this.automataPanel.getSaveAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onSaveButton();
			}
		});
		this.automataPanel.getNewAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onNewButton();
			}
		});

		this.automataPanel.getAutomataComboBox().addActionListener(new ActionListener() {

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
			this.currAutomata = this.automataPanel.getAutomata();
			this.automaton.set(this.automataPanel.getAutomataComboBox().getSelectedIndex(), this.currAutomata);

			System.out.println("### AUTOMATA EXTRACTED ###");
			System.err.println("### NOT VALIDATED YET ###");

		} catch (Exception e) {
			System.out.println("### AUTOMATA NOT EXTRACTED ###");
			System.err.println("### INVALID ###");
			e.printStackTrace();
		}
	}

	private void onAutomataListChange() {
		int automataIndex = -1;

		if (this.automataPanel.getAutomataComboBox().getItemCount() == 0) {
			this.automataPanel.getClearAutomataButton().doClick();
			setAutomataManipulationEnabled(false);
		} else {
			setAutomataManipulationEnabled(true);
			automataIndex = this.automataPanel.getAutomataComboBox().getSelectedIndex();

			try {
				Automata newAutomata = this.automaton.get(automataIndex);
				if (newAutomata == null) {
					this.automataPanel.clearAutomata();
				} else {
					changeAutomata(newAutomata);
				}

			} catch (IndexOutOfBoundsException e) {
				System.out.println("### NEW AUTOMATA ###");
			}

		}
	}

	private void changeAutomata(Automata newAutomata) {
		if (this.currAutomata != newAutomata) {
			this.currAutomata = newAutomata;
			this.automataPanel.setAutomata(this.currAutomata);
		}
	}

	private void onNewButton() {
		String automataName = null;
		try {
			automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
			if (automataName != null) {
				automataName = automataName.trim();
				if (!automataName.equals("")) {
					this.automataPanel.getAutomataComboBox().addItem(automataName);
					this.automataPanel.getAutomataComboBox().setSelectedItem(automataName);
					this.automaton.add(null);
					this.currAutomata = null;
					onAutomataListChange();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
