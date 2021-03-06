package view.automata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.automata.InvalidTransitionException;
import exception.automata.MultipleInitialStateException;
import model.automata.Automata;
import view.util.WrapLayout;

/**
 * Panel dedicated to manipulate automaton
 * 
 * @author douglas
 *
 */
public class AutomataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8670784410886817455L;

	JPanel toolbar = null;
	JScrollPane transitionTableScroll = null;
	AutomataTableModel transitionTableModel = null;
	JTable transitionTable = null;

	JButton newAutomataButton = null;
	JButton saveAutomataButton = null;
	JButton determinizeAutomataButton = null;
	JButton loadAutomataButton = null;
	JButton clearAutomataButton = null;
	JButton deleteAutomataButton = null;
	JButton unifyAutomataButton = null;
	JButton computeButton = null;
	JButton addColumnButton = null;
	JButton addRowButton = null;
	JComboBox<String> automataComboBox = null;

	public AutomataPanel(Automata automata) {

	}

	public AutomataPanel() {
		this.setLayout(new BorderLayout());

		initializeTransitionTable();
		initializeToolBar();

		this.add(BorderLayout.NORTH, toolbar);
		this.add(BorderLayout.CENTER, transitionTableScroll);

	}

	private void initializeToolBar() {

		addColumnButton = new JButton("Add Column");
		addRowButton = new JButton("Add Row");
		automataComboBox = new JComboBox<String>();
		newAutomataButton = new JButton("New");
		saveAutomataButton = new JButton("Save");
		loadAutomataButton = new JButton("Load");
		clearAutomataButton = new JButton("Clear");
		deleteAutomataButton = new JButton("Delete");
		determinizeAutomataButton = new JButton("Determinize");
		unifyAutomataButton = new JButton("Unify");
		computeButton = new JButton("Compute");

		toolbar = new JPanel(new WrapLayout());
		toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		toolbar.add(automataComboBox);
		toolbar.add(newAutomataButton);
		toolbar.add(saveAutomataButton);
		toolbar.add(loadAutomataButton);
		toolbar.add(deleteAutomataButton);

		toolbar.add(new ToolbarSeparator());
		toolbar.add(determinizeAutomataButton);
		toolbar.add(unifyAutomataButton);
		toolbar.add(computeButton);

		toolbar.add(new ToolbarSeparator());
		toolbar.add(clearAutomataButton);
		toolbar.add(addRowButton);
		toolbar.add(addColumnButton);

		addRowButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transitionTableModel.addRow();
			}
		});

		addColumnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transitionTableModel.addColumn();
			}
		});

	}

	public void clearAutomata() {
		transitionTableModel = new AutomataTableModel();
		transitionTable.setModel(transitionTableModel);
	}

	private void initializeTransitionTable() {

		transitionTableScroll = new JScrollPane();
		transitionTable = new JTable();
		transitionTableModel = new AutomataTableModel();

		transitionTable.setModel(transitionTableModel);
		transitionTableScroll.setViewportView(transitionTable);

		transitionTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		transitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		transitionTable.setGridColor(Color.LIGHT_GRAY);
		transitionTable.setShowGrid(true);
		transitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	private class ToolbarSeparator extends JLabel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7048708593522245197L;

		public ToolbarSeparator() {
			// TODO Auto-generated constructor stub
			super("|");
			this.setEnabled(false);
		}

	}

	// Getters and Setters

	/**
	 * Extract automata from table model
	 * 
	 * @return the automata
	 * @throws DuplicatedStateException
	 * @throws InvalidStateException
	 * @throws DuplicatedTransitionException
	 * @throws InvalidTransitionException
	 * @throws MultipleInitialStateException
	 */
	public Automata getAutomata() throws DuplicatedStateException, InvalidStateException, DuplicatedTransitionException,
			InvalidTransitionException, MultipleInitialStateException {
		Automata automata = new Automata();

		int rows = transitionTable.getRowCount();
		int columns = transitionTable.getColumnCount();

		boolean foundInitial = false;

		// adds states
		for (int i = 1; i < rows; i++) {
			String state = (String) transitionTable.getValueAt(i, 0);
			if (state == null)
				continue;
			state = state.trim();
			if (state.length() > 0) {
				boolean initialState = false;
				boolean finalState = false;
				if (state.startsWith(">")) {
					initialState = true;

					if (foundInitial) {
						throw new MultipleInitialStateException();
					}

					foundInitial = true;
					state = state.substring(1);
				}
				if (state.endsWith("*")) {
					finalState = true;
					state = state.substring(0, state.length() - 1);
				}

				automata.addState(state);

				if (finalState)
					automata.setAsFinalState(state);

				if (initialState)
					automata.setInitialState(state);
			}

		}

		String sourceState = null;
		String targetStates = null;
		Character transitionCharacter = null;
		String[] targetStatesArray = null;

		// get transitions
		for (int i = 1; i < rows; i++) {

			try {

				/*
				 * get source state of transition if no source state found, jump to next
				 * line/state of transition table
				 */

				sourceState = ((String) transitionTable.getValueAt(i, 0));
				if (sourceState == null)
					continue;
				sourceState = sourceState.trim();
				if (sourceState.equals(""))
					continue;

				if (sourceState.startsWith(">"))
					sourceState = sourceState.substring(1);

				if (sourceState.endsWith("*"))
					sourceState = sourceState.substring(0, sourceState.length() - 1);
			} catch (Exception e) {
				throw new InvalidTransitionException();
			}

			for (int j = 1; j < columns; j++) {

				try {

					/*
					 * gets transition character if cell is empty, jumps to next character/column
					 */
					String transitionCharacterCell = (String) transitionTable.getValueAt(0, j);
					if (transitionCharacterCell == null)
						continue;
					transitionCharacterCell = transitionCharacterCell.trim();
					if (transitionCharacterCell.equals(""))
						continue;
					transitionCharacter = transitionCharacterCell.charAt(0);

					/*
					 * gets target states. If cell is empty, jumps to next transition
					 */
					targetStates = ((String) transitionTable.getValueAt(i, j));
					if (targetStates == null)
						continue;
					targetStates = targetStates.trim();
					if (targetStates.equals(""))
						continue;

					targetStatesArray = targetStates.split(",");

				} catch (Exception e) {
					throw new InvalidTransitionException();
				}

				for (String targetState : targetStatesArray)
					if (!targetState.trim().equals("")) {
						automata.addTransition(sourceState, transitionCharacter, targetState);
					}
			}
		}

		return automata;

	}

	/**
	 * Set transition table values given automata
	 * 
	 * @param automata
	 */
	public void setAutomata(Automata automata) {
		AutomataTableModel newAutomata = new AutomataTableModel();

		List<Character> alphabet = automata.getAlphabet();
		List<String> states = automata.getStates();
		Map<Character, Integer> alphabetToColumn = new HashMap<Character, Integer>();
		Map<String, Integer> stateToRow = new HashMap<String, Integer>();

		String initialState;
		List<String> finalStates;

		// add states
		int row = 1;
		for (String state : states) {
			newAutomata.addRow();
			stateToRow.put(state, row);
			newAutomata.setValueAt(state, row, 0);
			row++;
		}

		// add characters
		int column = 1;
		for (Character character : alphabet) {
			newAutomata.addColumn();
			alphabetToColumn.put(character, column);
			newAutomata.setValueAt(character.toString(), 0, column);
			column++;
		}

		// add transitions
		for (String state : states) {
			for (Character character : alphabet) {
				String targetStates = "";
				try {
					List<String> targetStatesArray = automata.getTransition(state, character);

					// jumps empty transitions
					if (targetStatesArray == null)
						continue;

					for (String targetState : targetStatesArray) {
						targetStates = targetStates + "," + targetState;
					}
					targetStates = targetStates.substring(1, targetStates.length());

					newAutomata.setValueAt(targetStates, stateToRow.get(state), alphabetToColumn.get(character));
				} catch (InvalidStateException e) {
					e.printStackTrace();
				}
			}
		}

		// set initial state
		initialState = automata.getInitialState();
		newAutomata.setValueAt(">" + initialState, stateToRow.get(initialState), 0);

		// set final states
		finalStates = automata.getFinalStates();
		for (String state : finalStates) {
			String finalState = ((String) newAutomata.getValueAt(stateToRow.get(state), 0)) + "*";
			newAutomata.setValueAt(finalState, stateToRow.get(state), 0);
		}

		this.transitionTableModel = newAutomata;
		this.transitionTable.setModel(newAutomata);

	}

	public JTable getTransitionTable() {
		return transitionTable;
	}

	public void setTransitionTable(JTable transitionTable) {
		this.transitionTable = transitionTable;
	}

	public JButton getSaveAutomataButton() {
		return saveAutomataButton;
	}

	public void setSaveAutomataButton(JButton saveAutomataButton) {
		this.saveAutomataButton = saveAutomataButton;
	}

	public JButton getClearAutomataButton() {
		return clearAutomataButton;
	}

	public void setClearAutomataButton(JButton clearAutomataButton) {
		this.clearAutomataButton = clearAutomataButton;
	}

	public JButton getDeleteAutomataButton() {
		return deleteAutomataButton;
	}

	public void setDeleteAutomataButton(JButton deleteAutomataButton) {
		this.deleteAutomataButton = deleteAutomataButton;
	}

	public JButton getUnifyAutomataButton() {
		return unifyAutomataButton;
	}

	public void setUnifyAutomataButton(JButton unifyAutomataButton) {
		this.unifyAutomataButton = unifyAutomataButton;
	}

	public JButton getComputeButton() {
		return computeButton;
	}

	public void setComputeButton(JButton computeButton) {
		this.computeButton = computeButton;
	}

	public JButton getAddColumnButton() {
		return addColumnButton;
	}

	public void setAddColumnButton(JButton addColumnButton) {
		this.addColumnButton = addColumnButton;
	}

	public JButton getAddRowButton() {
		return addRowButton;
	}

	public void setAddRowButton(JButton addRowButton) {
		this.addRowButton = addRowButton;
	}

	public JComboBox<String> getAutomataComboBox() {
		return automataComboBox;
	}

	public void setAutomataComboBox(JComboBox<String> automataComboBox) {
		this.automataComboBox = automataComboBox;
	}

	public JButton getNewAutomataButton() {
		return newAutomataButton;
	}

	public void setNewAutomataButton(JButton newAutomataButton) {
		this.newAutomataButton = newAutomataButton;
	}

	public JButton getDeterminizeAutomataButton() {
		return determinizeAutomataButton;
	}

	public void setDeterminizeAutomataButton(JButton determinizeAutomataButton) {
		this.determinizeAutomataButton = determinizeAutomataButton;
	}

	public JButton getLoadAutomataButton() {
		return loadAutomataButton;
	}

	public void setLoadAutomataButton(JButton loadAutomataButton) {
		this.loadAutomataButton = loadAutomataButton;
	}

}
