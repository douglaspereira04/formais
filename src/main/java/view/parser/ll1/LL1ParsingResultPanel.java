package view.parser.ll1;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Panel dedicated to display parsing result data
 * 
 * @author douglas
 *
 */
public class LL1ParsingResultPanel extends JPanel {

	private JScrollPane resultTableScroll = null;
	private DefaultTableModel resultTableModel = null;
	private JTable resultTable = null;
	private JLabel resultTableLabel = null;

	private JScrollPane treeScroll = null;
	private JPanel treePanel = null;
	private JTextArea treeTextArea;
	private JLabel treeLabel = null;
	
	private JScrollPane tokenScroll = null;
	private JPanel tokenPanel = null;
	private JTextArea tokenTextArea;
	private JLabel tokenLabel;
	private JLabel resultLabel;

	/**
	 * 
	 */
	private static final long serialVersionUID = 582367084399672319L;

	public LL1ParsingResultPanel() {
		
		initialize();
	}
	
	private void initialize() {
		initializeTokenPanel();
		initializeTreePanel();

		resultTableScroll = new JScrollPane();
		initializeResultTablePanel();
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		this.setLayout(layout);

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(4, 4, 4, 4);

		
		constraints.gridx = 0;
		constraints.gridy = 0;
		tokenLabel = new JLabel("Token");
		this.add(tokenLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		resultTableLabel = new JLabel("Resultado");
		this.add(resultTableLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		treeLabel = new JLabel("Árvore");
		this.add(treeLabel, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 0.2;
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(tokenScroll, constraints);

		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		this.add(this.resultTableScroll, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridheight = 3;
		this.add(this.treeScroll, constraints);

		constraints.weightx = 1;
		constraints.weighty = 0;
		resultLabel = new JLabel("");
		resultLabel.setOpaque(true);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		this.add(this.resultLabel, constraints);
		

	}

	private void initializeTreePanel() {
		this.treePanel = new JPanel();
		this.treeScroll = new JScrollPane();
		this.treeTextArea = new JTextArea();
		this.treeTextArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
		this.treeScroll.setViewportView(this.treeTextArea);
		this.treeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treePanel.add(treeScroll);
	}

	private void initializeTokenPanel() {
		this.tokenPanel = new JPanel();
		this.tokenScroll = new JScrollPane();
		this.tokenTextArea = new JTextArea();
		this.tokenScroll.setViewportView(this.tokenTextArea);
		this.tokenScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tokenPanel.add(tokenScroll);
	}

	public void initializeResultTablePanel() {
		resultTable = new JTable();
		resultTableModel = new DefaultTableModel();

		resultTable.setModel(resultTableModel);
		resultTableScroll.setViewportView(resultTable);

		resultTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		resultTable.setGridColor(Color.LIGHT_GRAY);
		resultTable.setShowGrid(true);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resultTable.setEnabled(false);
	}

	public DefaultTableModel getResultTableModel() {
		return resultTableModel;
	}

	public void setResultTableModel(DefaultTableModel resultTableModel) {
		this.resultTableModel = resultTableModel;
	}

	public JTable getResultTable() {
		return resultTable;
	}

	public void setResultTable(JTable resultTable) {
		this.resultTable = resultTable;
	}

	public JTextArea getTreeTextArea() {
		return treeTextArea;
	}

	public void setTreeTextArea(JTextArea treeTextArea) {
		this.treeTextArea = treeTextArea;
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
	
	
	
	

}
