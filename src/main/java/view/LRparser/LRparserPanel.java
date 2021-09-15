package view.LRparser;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import model.parser.ll1.LL1Parser;

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
	private DefaultTableModel parsingTableModel = null;
	private JTable parsingTable = null;
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
		initializeParsingTablePanel();

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

	public void initializeParsingTablePanel(LL1Parser parser) {

		initializeParsingTablePanel();

		parsingTableModel.addColumn("");

		List<String> terminal = parser.getTerminalSymbols();
		terminal.add("$");
		for (int i = 0; i < terminal.size(); i++) {
			String symbol = terminal.get(i);
			parsingTableModel.addColumn(symbol);
		}

		for (int i = 0; i < parser.getNonTerminalSymbols().size(); i++) {
			parsingTableModel.addRow(new String[parsingTableModel.getColumnCount()]);
			parsingTableModel.setValueAt(parser.getNonTerminalSymbols().get(i), i, 0);
		}

		for (int i = 0; i < terminal.size(); i++) {
			for (int j = 0; j < parser.getNonTerminalSymbols().size(); j++) {
				String production = parser.getParsingTable().get(parser.getNonTerminalSymbols().get(j),
						terminal.get(i));
				parsingTableModel.setValueAt(production, j, i + 1);
			}
		}

		parsingTable.getTableHeader().repaint();
		parsingTable.repaint();

	}

	public void initializeParsingTablePanel() {
		parsingTable = new JTable();
		parsingTableModel = new DefaultTableModel();

		parsingTable.setModel(parsingTableModel);
		parsingTableScroll.setViewportView(parsingTable);

		parsingTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		parsingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		parsingTable.setGridColor(Color.LIGHT_GRAY);
		parsingTable.setShowGrid(true);
		parsingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		parsingTable.setEnabled(false);
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
		return parsingTableModel;
	}

	public void setParsingTableModel(DefaultTableModel parsingTableModel) {
		this.parsingTableModel = parsingTableModel;
	}

	public JTable getParsingTable() {
		return parsingTable;
	}

	public void setParsingTable(JTable parsingTable) {
		this.parsingTable = parsingTable;
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
