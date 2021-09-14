package view.parser.ll1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.apache.commons.collections4.map.MultiKeyMap;

import model.parser.ll1.Parser;
import view.automata.AutomataTableModel;
import view.la.HighlightedTextPane;

public class LL1ParserDefinitionPanel extends JPanel {

	JScrollPane grammarScroll = null;
	JPanel grammarPanel = null;
	JTextArea grammarTextArea;
	JLabel grammarLabel = null;

	JScrollPane parsingTableScroll = null;
	DefaultTableModel parsingTableModel = null;
	JTable parsingTable = null;
	JLabel parsingTableLabel = null;
	

	public LL1ParserDefinitionPanel() {

		initialize();
	}

	public void initialize() {

		initializeGrammarPanel();
		initializeParsingTablePanel();
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		this.setLayout(layout);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(4,4,4,4);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		grammarLabel = new JLabel("Gramática");
		this.add(grammarLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		parsingTableLabel = new JLabel("Parsing Table");
		this.add(parsingTableLabel, constraints);
		

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
	}

	private void initializeGrammarPanel() {
		this.grammarPanel = new JPanel();
		this.grammarScroll = new JScrollPane();
		this.grammarTextArea = new JTextArea();
		this.grammarScroll.setViewportView(this.grammarTextArea);
		this.grammarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		grammarPanel.add(grammarScroll);
	}

	public void initializeParsingTablePanel(Parser parser) {

		initializeParsingTablePanel();

		
		for (int i = 0; i < parser.getTerminalSymbols().size(); i++) {
			String symbol = parser.getTerminalSymbols().get(i);
			parsingTable.getColumnModel().getColumn(i+1).setHeaderValue(symbol);
		}
		
		for (int i = 0; i < parser.getTerminalSymbols().size(); i++) {
			
			parsingTableModel.setValueAt(parser.getNonTerminalSymbols().get(i), i+1, 0);
		}
		parsingTable.getTableHeader().repaint();
		parsingTable.repaint();
		
	}
	
	private void initializeParsingTablePanel() {
		parsingTableScroll = new JScrollPane();
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

	public JTextArea getGrammarTextArea() {
		return grammarTextArea;
	}

	public void setGrammarTextArea(JTextArea grammarTextArea) {
		this.grammarTextArea = grammarTextArea;
	}
		

}
