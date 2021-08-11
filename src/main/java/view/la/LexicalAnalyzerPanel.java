package view.la;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel dedicated to display lexical analyzer data 
 * @author douglas
 *
 */
public class LexicalAnalyzerPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2834072946952508202L;

	JTabbedPane laTabs = null;
	
	DefinitionPanel definitionPanel = null;
	
	AnalysisPanel analysisPanel = null;
	
	public LexicalAnalyzerPanel() {
		this.setLayout(new BorderLayout());
		
		initializeDefinitionPanel();
		initializeAnalysisPanel();
		
		laTabs = new JTabbedPane();
		laTabs.addTab("Definições", definitionPanel);
		laTabs.addTab("Análise", analysisPanel);

		laTabs.setTabPlacement(JTabbedPane.TOP);
		laTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		this.add(BorderLayout.CENTER, laTabs);
	}

	private void initializeDefinitionPanel(){
		definitionPanel = new DefinitionPanel();
		
	}
	
	private void initializeAnalysisPanel(){
		analysisPanel = new AnalysisPanel();
		
	}

	public DefinitionPanel getDefinitionPanel() {
		return definitionPanel;
	}

	public void setDefinitionPanel(DefinitionPanel definitionPanel) {
		this.definitionPanel = definitionPanel;
	}

	public AnalysisPanel getAnalysisPanel() {
		return analysisPanel;
	}

	public void setAnalysisPanel(AnalysisPanel analysisPanel) {
		this.analysisPanel = analysisPanel;
	}
	
	
	
	

}
