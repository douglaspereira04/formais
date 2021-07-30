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

   

}
