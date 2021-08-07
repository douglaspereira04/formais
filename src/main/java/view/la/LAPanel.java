package view.la;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel dedicated to display lexical analyzer data 
 * @author douglas
 *
 */
public class LAPanel extends JPanel {
	
	JTabbedPane laTabs = null;
	
	DefinitionPanel definitionPanel = null;
	
	AnalysisPanel analysisPanel = null;
	
	public LAPanel() {
		this.setLayout(new BorderLayout());
		
		initializeDefinitionPanel();
		
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
	
	

}
