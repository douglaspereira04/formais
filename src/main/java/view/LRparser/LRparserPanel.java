package view.LRparser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Panel dedicated to define ll1 parser
 * 
 * @author douglas
 *
 */
public class LRparserPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 929781123313455926L;

	private JScrollPane grammarScroll = null;
	private JPanel grammarPanel = null;
	private JTextArea grammarTextArea;
	private JLabel grammarLabel = null;

	private JScrollPane parsingTableScroll = null;
	
	private DefaultTableModel actionTableModel = null;
	private JTable actionTable = null;
	private JLabel actionLabel = null;
	private JPanel actionPanel = null;
	
	private DefaultTableModel gotoTableModel = null;
	private JTable gotoTable = null;
	private JLabel gotoLabel = null;
	private JPanel gotoPanel = null;
	
	private DefaultTableModel statesTableModel = null;
	private JTable statesTable = null;
	private JLabel statesLabel = null;
	private JPanel statesPanel = null;
	
	private JPanel parsingPanel = null;
		
	private JLabel parsingTableLabel = null;

	private JScrollPane tokenScroll = null;
	private JPanel tokenPanel = null;
	private JTextArea tokenTextArea;
	private JLabel tokenLabel;

	private JButton parseButton;
	private JLabel resultLabel;

	public LRparserPanel() {
		
		initialize();
	}

	public void initialize() {

		initializeGrammarPanel();
		initializeTokenPanel();
		

		parsingTableScroll = new JScrollPane();
		initializeScroll();

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		this.setLayout(layout);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.gridwidth = 2;

		constraints.gridx = 0;
		constraints.gridy = 0;
		grammarLabel = new JLabel("Gramática");
		this.add(grammarLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		parsingTableLabel = new JLabel("Parsing Table");
		this.add(parsingTableLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		tokenLabel = new JLabel("Token");
		this.add(tokenLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		tokenLabel = new JLabel("Token");
		this.add(tokenLabel, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(this.grammarScroll, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		this.add(this.parsingTableScroll, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		this.add(this.tokenScroll, constraints);
		
		constraints.gridwidth = 1;
		
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.gridy = 6;
		resultLabel = new JLabel("");
		resultLabel.setOpaque(true);
		this.add(this.resultLabel, constraints);
		
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 6;
		parseButton = new JButton("Parse");
		this.add(this.parseButton, constraints);

	}

	private void initializeGrammarPanel() {
		this.grammarPanel = new JPanel();
		this.grammarScroll = new JScrollPane();
		this.grammarTextArea = new JTextArea();
		this.grammarScroll.setViewportView(this.grammarTextArea);
		this.grammarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		grammarPanel.add(grammarScroll);
	}

	public void initializeActionTablePanel() {
		
		actionLabel = new JLabel("Ação");
		
		actionTable = new JTable();
		actionTableModel = new DefaultTableModel();

		actionTable.setModel(actionTableModel);

		actionTable.setGridColor(Color.LIGHT_GRAY);
		actionTable.setShowGrid(true);
		actionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		actionTable.setEnabled(false);
	}
	
	public void initializeGotoTablePanel() {
		
		gotoLabel = new JLabel("GOTO");
		gotoTable = new JTable();
		gotoTableModel = new DefaultTableModel();

		gotoTable.setModel(gotoTableModel);

		gotoTable.setGridColor(Color.LIGHT_GRAY);
		gotoTable.setShowGrid(true);
		gotoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		gotoTable.setEnabled(false);
	}
	
	public void initializeScroll() {
		parsingPanel = new JPanel();

		initializeActionTablePanel();
		initializeGotoTablePanel();
		initializeStatesTablePanel();

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		parsingPanel.setLayout(layout);

		constraints.anchor = GridBagConstraints.BASELINE_LEADING;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 0);
		
		constraints.gridwidth = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;


		constraints.gridx = 0;
		constraints.gridy = 0;
		parsingPanel.add(statesLabel, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		parsingPanel.add(actionLabel, constraints);
	

		constraints.gridx = 2;
		constraints.gridy = 0;
		parsingPanel.add(gotoLabel, constraints);

		constraints.weighty = 1;
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		parsingPanel.add(statesTable, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		parsingPanel.add(actionTable, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 1;
		parsingPanel.add(gotoTable, constraints);
		
		parsingTableScroll.setViewportView(parsingPanel);

		parsingTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	

	public void addActionColumn(String symbol) {
		try {
			actionTableModel.addColumn(symbol);
			
			if (actionTableModel.getRowCount() == 0) {
				actionTableModel.addRow(new Object[] {});
			}
			
			actionTableModel.setValueAt(symbol, 0, actionTableModel.getColumnCount()-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	public void addAction(String symbol, String action, int state) {
		try {
			for (int i = 0; i < actionTableModel.getColumnCount(); i++) {
				if (actionTableModel.getColumnName(i).equals(symbol)) {
					
					while(actionTableModel.getRowCount() < state+2) {
						actionTableModel.addRow(new Object[] {});
					}
					
					actionTableModel.setValueAt(action, state+1, i);
					break;
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void addGotoColumn(String symbol) {
		try {

			gotoTableModel.addColumn(symbol);
			
			if (gotoTableModel.getRowCount() == 0) {
				gotoTableModel.addRow(new Object[] {});
			}
			
			gotoTableModel.setValueAt(symbol, 0, gotoTableModel.getColumnCount()-1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGoto(String symbol, String gotoString, int state) {
		try {
			for (int i = 0; i < gotoTableModel.getColumnCount(); i++) {
				if (gotoTableModel.getColumnName(i).equals(symbol)) {
					
					while(gotoTableModel.getRowCount() < state+2) {
						gotoTableModel.addRow(new Object[] {});
					}
					
					gotoTableModel.setValueAt(gotoString, state+1, i);
					break;
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addState(String state) {
		statesTableModel.addRow(new Object[] {state});
	}
	
	public void addState(int state) {
		String stateString = String.valueOf(state);
		addState(stateString);
	}

	
	public void initializeStatesTablePanel() {
		
		statesLabel = new JLabel("Estados");
		statesTable = new JTable();
		statesTableModel = new DefaultTableModel();

		statesTable.setModel(statesTableModel);
		statesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		statesTable.setGridColor(Color.LIGHT_GRAY);
		statesTable.setShowGrid(true);
		statesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		statesTable.setEnabled(false);
		
		statesTableModel.addColumn("States");
		addState("");
	}

	private void initializeTokenPanel() {
		this.tokenPanel = new JPanel();
		this.tokenScroll = new JScrollPane();
		this.tokenTextArea = new JTextArea();
		this.tokenScroll.setViewportView(this.tokenTextArea);
		this.tokenScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tokenPanel.add(tokenScroll);
	}

	public JTextArea getGrammarTextArea() {
		return grammarTextArea;
	}

	public void setGrammarTextArea(JTextArea grammarTextArea) {
		this.grammarTextArea = grammarTextArea;
	}

	public DefaultTableModel getParsingTableModel() {
		return actionTableModel;
	}

	public void setParsingTableModel(DefaultTableModel parsingTableModel) {
		this.actionTableModel = parsingTableModel;
	}

	public JTable getParsingTable() {
		return actionTable;
	}

	public void setParsingTable(JTable parsingTable) {
		this.actionTable = parsingTable;
	}

	public JTextArea getTokenTextArea() {
		return tokenTextArea;
	}

	public void setTokenTextArea(JTextArea tokenTextArea) {
		this.tokenTextArea = tokenTextArea;
	}

	public JLabel getResultLabel() {
		return resultLabel;
	}

	public void setResultLabel(JLabel resultLabel) {
		this.resultLabel = resultLabel;
	}
	
	public void setResultLabel(boolean accepted) {
		if (accepted) {
			this.resultLabel.setText("Aceito");
			this.resultLabel.setBackground(Color.GREEN);
		} else {
			this.resultLabel.setText("Rejeitado");
			this.resultLabel.setBackground(Color.RED);
		}
	}

	public JButton getParseButton() {
		return parseButton;
	}

	public void setParseButton(JButton parseButton) {
		this.parseButton = parseButton;
	}
	

}
