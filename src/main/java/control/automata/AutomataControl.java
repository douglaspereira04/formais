package control.automata;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import model.automata.Automata;
import model.io.FileUtils;
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

		this.automataPanel.getLoadAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onLoadButton();
			}
		});

		this.automataPanel.getUnifyAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onUnifyButton();
			}
		});

		this.automataPanel.getDeleteAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onDeleteButton();
			}
		});

		this.automataPanel.getDeterminizeAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onDeterminizeButton();
			}
		});

		this.automataPanel.getClearAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onClearButton();
			}
		});
	}

	private void onSaveButton() {
		try {

			this.currAutomata = this.automataPanel.getAutomata();
			this.automaton.set(this.automataPanel.getAutomataComboBox().getSelectedIndex(), this.currAutomata);

			File path = FileUtils.selectExportFile(
					System.getProperty("user.dir") + System.getProperty("file.separator") + "new.automata",
					this.automataPanel);
			FileUtils.saveToFile(this.currAutomata, path.getAbsolutePath());

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
			this.automataPanel.clearAutomata();
			setAutomataManipulationEnabled(false);
		} else {
			automataIndex = this.automataPanel.getAutomataComboBox().getSelectedIndex();

			try {
				Automata newAutomata = this.automaton.get(automataIndex);
				if (newAutomata == null) {
					this.automataPanel.clearAutomata();
				} else {
					changeAutomata(newAutomata);
				}

			} catch (IndexOutOfBoundsException e) {
				System.out.println("### NO SUCH AUTOMATA ###");
			}
			setAutomataManipulationEnabled(true);

		}

	}

	private void changeAutomata(Automata newAutomata) {
		this.currAutomata = newAutomata;
		this.automataPanel.setAutomata(this.currAutomata);
	}

	private void onNewButton() {
		String automataName = null;
		try {
			automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
			addsAutomata(automataName, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void onLoadButton() {
		File path = FileUtils.selectImportFile("*.automata", this.automataPanel);
		try {
			Automata automata = FileUtils.loadFromFile(path.getAbsolutePath(), Automata.class);

			String automataName = null;
			try {
				automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");

				addsAutomata(automataName, automata);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onUnifyButton() {
		Automata automata1 = null;
		Automata automata2 = null;
		Automata unified = null;

		JComboBox<String> comboBox = new JComboBox<String>();

		for (int i = 0; i < this.automataPanel.getAutomataComboBox().getItemCount(); i++) {
			comboBox.addItem(automataPanel.getAutomataComboBox().getItemAt(i));
		}
		JOptionPane.showMessageDialog(this.automataPanel, comboBox);
		automata1 = this.automaton.get(comboBox.getSelectedIndex());
		JOptionPane.showMessageDialog(this.automataPanel, comboBox);
		automata2 = this.automaton.get(comboBox.getSelectedIndex());

		try {
			unified = Automata.unify(automata1, automata2);

			String automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
			addsAutomata(automataName, unified);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(automataPanel, "Unification failed");
		}

	}

	private void onDeleteButton() {
		try {
			int index = this.automataPanel.getAutomataComboBox().getSelectedIndex();
			this.automataPanel.getAutomataComboBox().removeItemAt(index);
			this.automaton.remove(index);
			this.automataPanel.getAutomataComboBox().setSelectedIndex(0);
			this.currAutomata = this.automaton.get(0);
		} catch (Exception e) {
			this.currAutomata = null;
			this.automataPanel.clearAutomata();
		}
		onAutomataListChange();
	}

	private void onClearButton() {
		try {
			this.automaton.set(automataPanel.getAutomataComboBox().getSelectedIndex(), null);
			this.currAutomata = null;
			this.onAutomataListChange();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void onDeterminizeButton() {
		Automata determinized = null;

		try {
			determinized = this.currAutomata.determinize();

			String automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
			addsAutomata(automataName, determinized);
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(automataPanel, "Determinization failed");
		}
	}
	
	private void addsAutomata(String name, Automata automata) {
		if (name != null) {
			name = name.trim();
			if (((DefaultComboBoxModel<String>) automataPanel.getAutomataComboBox().getModel())
					.getIndexOf(name) == -1) {
				if (!name.equals("")) {
					this.automataPanel.getAutomataComboBox().addItem(name);
					this.automataPanel.getAutomataComboBox().setSelectedItem(name);
					this.automaton.add(automata);
					this.currAutomata = automata;
					onAutomataListChange();
				}
			} else {
				JOptionPane.showMessageDialog(automataPanel, "Automata " + name + " already exists");
			}
		}
	}
}
