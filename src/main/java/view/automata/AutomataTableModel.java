package view.automata;

import java.util.Iterator;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.automata.InvalidTransitionException;
import exception.automata.MultipleInitialStateException;
import model.automata.Automata;

/**
 * Automata table model
 * 
 * @author douglas
 *
 */
public class AutomataTableModel extends DefaultTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = -8095541103264108118L;

    /**
     * Creates delta cell and header Accepts only single characters on first line
     */
    public AutomataTableModel() {
	super(new Object[][] {}, new String[] { "0" });
	this.addRow(new Object[] { "δ" });

	this.addTableModelListener(new TableModelListener() {

	    @Override
	    public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE)
		    if (e.getFirstRow() == 0) {
			String word = (String) (((AutomataTableModel) e.getSource()).getValueAt(0, e.getColumn()));
			Character symbol = word.charAt(0);
			int length = word.length();
			if (length > 1) {
			    ((AutomataTableModel) e.getSource()).setValueAt(symbol.toString(), 0, e.getColumn());
			}
		    }
	    }
	});
    }

    /**
     * Disables delta cell edition
     */
    @Override
    public boolean isCellEditable(int row, int column) {
	return !(row == 0 && column == 0);
    }

    public void addRow() {
	super.addRow(new String[this.getColumnCount()]);
    }

    public void addColumn() {
	super.addColumn(this.getColumnCount());
    }

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

	int rows = this.getRowCount();
	int columns = this.getColumnCount();

	boolean foundInitial = false;

	for (int i = 1; i < rows; i++) {
	    String state = (String) this.getValueAt(i, 0);
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

	for (int i = 1; i < rows; i++) {
	    for (int j = 1; j < columns; j++) {
		String sourceState = null;
		String targetState = null;
		Character transitionCharacter = null;
		try {
		    if (sourceState.startsWith(">"))
			sourceState = sourceState.substring(1);

		    if (sourceState.endsWith("*"))
			sourceState = sourceState.substring(0, sourceState.length() - 1);

		    sourceState = (String) this.getValueAt(i, 0);
		    targetState = (String) this.getValueAt(i, j);
		    transitionCharacter = (Character) this.getValueAt(0, j);
		} catch (Exception e) {
		    throw new InvalidTransitionException();
		}
		automata.addTransition(sourceState, transitionCharacter, targetState);
	    }
	}

	return automata;

    }

}
