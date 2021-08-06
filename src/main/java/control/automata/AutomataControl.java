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

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
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

	/**
	 * Sets avaliability of automata manipulation buttons
	 * 
	 * @param enabled
	 */
	private void setAutomataManipulationEnabled(boolean enabled) {
		this.automataPanel.getTransitionTable().setEnabled(enabled);
		this.automataPanel.getSaveAutomataButton().setEnabled(enabled);
		this.automataPanel.getDeleteAutomataButton().setEnabled(enabled);
		this.automataPanel.getAddColumnButton().setEnabled(enabled);
		this.automataPanel.getAddRowButton().setEnabled(enabled);
		this.automataPanel.getClearAutomataButton().setEnabled(enabled);
		this.automataPanel.getComputeButton().setEnabled(enabled);
		this.automataPanel.getUnifyAutomataButton().setEnabled(enabled);
	}

	/**
	 * Initialze user interface behaviour
	 */
	private void initializeBehavior() {
		this.automataPanel.getSaveAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		this.automataPanel.getNewAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newAutomata();
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
				load();
			}
		});

		this.automataPanel.getUnifyAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				unify();
			}
		});

		this.automataPanel.getDeleteAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		this.automataPanel.getDeterminizeAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				determinize();
			}
		});

		this.automataPanel.getClearAutomataButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		
		this.automataPanel.getComputeButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				compute();
				
			}
		});
	}

	/**
	 * Prompt user to save displaying automata to file
	 */
	private void save() {
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

	/**
	 * Changes displaying automata Verify if panel should be enabled or not
	 * 
	 */
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
					change(newAutomata);
				}

			} catch (IndexOutOfBoundsException e) {
				System.out.println("### NO SUCH AUTOMATA ###");
			}
			setAutomataManipulationEnabled(true);

		}

	}

	/**
	 * Changes displaying automata
	 * 
	 * @param automata
	 */
	private void change(Automata automata) {
		this.currAutomata = automata;
		this.automataPanel.setAutomata(this.currAutomata);
	}

	/**
	 * Adds a new automata to automata list
	 */
	private void newAutomata() {
		String automataName = null;
		try {
			automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
			addAutomata(automataName, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * Prompts user to load a automata file from file system
	 */
	private void load() {
		File path = FileUtils.selectImportFile("*.automata", this.automataPanel);
		try {
			Automata automata = FileUtils.loadFromFile(path.getAbsolutePath(), Automata.class);

			String automataName = null;
			try {
				automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");

				addAutomata(automataName, automata);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Prompts user to unify 2 automaton
	 */
	private void unify() {
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
			addAutomata(automataName, unified);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(automataPanel, "Unification failed");
		}

	}

	/**
	 * Removes displaying automata from automata list
	 */
	private void delete() {
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

	/**
	 * Clear displaying automata
	 */
	private void clear() {
		try {
			this.automaton.set(automataPanel.getAutomataComboBox().getSelectedIndex(), null);
			this.currAutomata = null;
			this.onAutomataListChange();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Prompt user to determinize current displaying automata
	 */
	private void determinize() {
		Automata determinized = null;

		try {
			determinized = this.currAutomata.determinize();

			String automataName = JOptionPane.showInputDialog(this.automataPanel, "Enter new automata name");
			addAutomata(automataName, determinized);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(automataPanel, "Determinization failed");
		}
	}

	/**
	 * prompts user to compute a word in displaying automata
	 */
	private void compute() {
		boolean accepted = false;
		String message = null;
		String word = JOptionPane.showInputDialog(automataPanel, "Word to be computed");

		if (word != null)
			try {
				accepted = this.currAutomata.compute(word);
			} catch (InvalidStateException | DuplicatedStateException | DuplicatedTransitionException e) {
				JOptionPane.showMessageDialog(automataPanel, "Malformed automata");
			}

		if (accepted)
			message = "Accepted";
		else
			message = "Not Accepted";

		JOptionPane.showMessageDialog(automataPanel, message);
	}

	/**
	 * Adds a new automata to control
	 * 
	 * @param name     - of the automata
	 * @param automata - itself
	 */
	private void addAutomata(String name, Automata automata) {
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
