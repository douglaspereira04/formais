package view.la;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Analysis table
 * @author douglas
 *
 */
public class AnalysisTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7922685173870009374L;

	public AnalysisTable() {
		super();
		
		this.setModel(new DefaultTableModel(new Object[][] {}, new String[] {"Token", "Lexeme", "Position"}));
		this.setGridColor(Color.LIGHT_GRAY);
		this.setShowGrid(true);
		this.setFillsViewportHeight(true); 
	}
	
	/**
	 * Adds an entry to table
	 * @param token
	 * @param lexeme
	 * @param position
	 */
	public void addEntry(String token, String lexeme, Integer position) {
		((DefaultTableModel)this.getModel()).addRow(new Object[] {token, lexeme, position});
	}
	
	/**
	 * Clear table
	 */
	public void clear() {
		this.setModel(new DefaultTableModel());
	}
	

}
