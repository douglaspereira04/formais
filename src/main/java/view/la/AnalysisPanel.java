package view.la;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * Panel dedicated to display lexical analyzer analysis input and output data
 * @author douglas
 *
 */
public class AnalysisPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4621820729216191902L;

	public AnalysisTable analysisTable = null;
	public JScrollPane analysisTableScroll = null;

	public JScrollPane textAreaScroll = null;
	public JTextArea textArea = null;

	public JButton exportButton = null;
	public JButton ll1AnalysisButton = null;
	public JButton lr1AnalysisButton = null;
	
	public AnalysisPanel() {
		super(new GridBagLayout());
		initialize();
	}
	
	public void initialize() {
		exportButton = new JButton("Exportar");
		ll1AnalysisButton = new JButton("Abrir no Parser LL1");
		lr1AnalysisButton = new JButton("Abrir no Parser LR1");
		
		analysisTableScroll = new JScrollPane();
		analysisTable = new AnalysisTable();
		analysisTableScroll.setViewportView(analysisTable);
		analysisTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		textAreaScroll = new JScrollPane();
		textArea = new JTextArea();
		textAreaScroll.setViewportView(textArea);
		textAreaScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.insets = new Insets(4,4,4,4);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;	
		constraints.gridwidth = 3;

		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(textAreaScroll, constraints);
		

		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(analysisTableScroll, constraints);

		constraints.weighty = 0;
		constraints.gridwidth = 1;
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		this.add(exportButton, constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		this.add(ll1AnalysisButton, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 2;
		this.add(lr1AnalysisButton, constraints);
		
	}

	public AnalysisTable getAnalysisTable() {
		return analysisTable;
	}

	public void setAnalysisTable(AnalysisTable analysisTable) {
		this.analysisTable = analysisTable;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JButton getExportButton() {
		return exportButton;
	}

	public void setExportButton(JButton exportButton) {
		this.exportButton = exportButton;
	}

	public JButton getLl1AnalysisButton() {
		return ll1AnalysisButton;
	}

	public void setLl1AnalysisButton(JButton ll1AnalysisButton) {
		this.ll1AnalysisButton = ll1AnalysisButton;
	}

	public JButton getLr1AnalysisButton() {
		return lr1AnalysisButton;
	}

	public void setLr1AnalysisButton(JButton lr1AnalysisButton) {
		this.lr1AnalysisButton = lr1AnalysisButton;
	}
	
}
