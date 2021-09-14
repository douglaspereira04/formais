package view.parser.ll1;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel to configure an ll1 parser
 * Displaying an parsing table
 * @author douglas
 *
 */
public class LL1ParserPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4403836902111211975L;

	JTabbedPane parserTabs = null;
	LL1ParserDefinitionPanel parserDefinitionPanel = null;
	LL1ParsingResultPanel parsingResultPanel = null;
	
	
	public LL1ParserPanel() {
		// TODO Auto-generated constructor stub
		initialize();
	}

	public void initialize() {
		this.setLayout(new BorderLayout());
		
		parserDefinitionPanel = new LL1ParserDefinitionPanel();
		
		parserTabs = new JTabbedPane();
		parserTabs.addTab("Definições", parserDefinitionPanel);
		parserTabs.addTab("Análise", parsingResultPanel);


		parserTabs.setTabPlacement(JTabbedPane.TOP);
		parserTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		this.add(BorderLayout.CENTER, parserTabs);
		
	}

	public JTabbedPane getParserTabs() {
		return parserTabs;
	}

	public void setParserTabs(JTabbedPane parserTabs) {
		this.parserTabs = parserTabs;
	}

	public LL1ParserDefinitionPanel getParserDefinitionPanel() {
		return parserDefinitionPanel;
	}

	public void setParserDefinitionPanel(LL1ParserDefinitionPanel parserDefinitionPanel) {
		this.parserDefinitionPanel = parserDefinitionPanel;
	}

	public LL1ParsingResultPanel getParsingResultPanel() {
		return parsingResultPanel;
	}

	public void setParsingResultPanel(LL1ParsingResultPanel parsingResultPanel) {
		this.parsingResultPanel = parsingResultPanel;
	}
	
	
	

}
