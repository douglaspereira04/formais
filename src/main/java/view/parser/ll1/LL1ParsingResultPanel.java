package view.parser.ll1;

import java.awt.Color;
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
	
	private JTextArea token = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 582367084399672319L;

	public LL1ParsingResultPanel() {
		
		initialize();
	}
	
	private void initialize() {
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
		resultTableLabel = new JLabel("Result");
		this.add(resultTableLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		treeLabel = new JLabel("Árvore");
		this.add(treeLabel, constraints);

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(this.resultTableScroll, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		this.add(this.treeScroll, constraints);

	}

	private void initializeTreePanel() {
		this.treePanel = new JPanel();
		this.treeScroll = new JScrollPane();
		this.treeTextArea = new JTextArea();
		this.treeScroll.setViewportView(this.treeTextArea);
		this.treeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treePanel.add(treeScroll);
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

}
