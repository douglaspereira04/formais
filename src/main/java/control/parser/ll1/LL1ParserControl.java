package control.parser.ll1;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import exception.automata.InvalidStateException;
import model.parser.ll1.LL1Parser;
import model.parser.ll1.LL1ParsingResult;
import view.parser.ll1.LL1ParserPanel;
import view.parser.ll1.LL1ParsingResultPanel;

/**
 * Class dedicated to control LL1ParserPanel behavior
 * @author douglas
 *
 */
public class LL1ParserControl {

	LL1ParserPanel parserPanel = null;
	LL1Parser parser = null;

	public LL1ParserControl(LL1ParserPanel parserPanel) {
		super();
		this.parserPanel = parserPanel;

		initializeBehavior();
	}
	
	
	private void initializeBehavior() {
		
		this.parserPanel.getParserDefinitionPanel().getGrammarTextArea().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					createParsingTable();
				} catch (Exception e2) {
					e2.printStackTrace();
					parserPanel.getParserDefinitionPanel().initializeParsingTablePanel();
					parser = null;
				}
			}

		});
		
		this.parserPanel.getParsingResultPanel().getTokenTextArea().addKeyListener(new KeyAdapter() {
		
			@Override
			public void keyReleased(KeyEvent e) {
				
				parse();
				
			}
		
		});
		
	}
	
	private void createParsingTable() throws InvalidStateException {
		String TESTGRAMMAR = "E -> T E'\n" + "E' -> + T E'\n" + "E' -> &\n" + "T -> F T'\n"
				+ "T' -> * F T'\n" + "T' -> &\n" + "F -> ( E )\n" + "F -> id";
		
		String grammar = this.parserPanel.getParserDefinitionPanel().getGrammarTextArea().getText();
		this.parser = new LL1Parser();
		
		this.parser.setGrammar(TESTGRAMMAR);
		this.parser.loadGrammar();
		this.parser.loadFirstPos();
		this.parser.loadFollowPos();
		this.parser.loadParsingTable();
		
		this.parserPanel.getParserDefinitionPanel().initializeParsingTablePanel(this.parser);
	}
	
	private void parse() {
		LL1ParsingResultPanel resultPanel = parserPanel.getParsingResultPanel();
		resultPanel.initializeResultTablePanel();
		
		if(parser == null) {
			JOptionPane.showMessageDialog(parserPanel, "Defina um parser válido");
			parserPanel.getParserTabs().setSelectedComponent(parserPanel.getParserDefinitionPanel());
		}else {
			try {
				
				LL1ParsingResult result = parser.parse(resultPanel.getTokenTextArea().getText(), 100000000);
				resultPanel.getTreeTextArea().setText(result.getTreeAsString());

				resultPanel.getResultTableModel().addColumn("Pilha");
				resultPanel.getResultTableModel().addColumn("Entrada");
				resultPanel.getResultTableModel().addColumn("Regra");
				
				for (int i = 0; i < result.getInput().size(); i++) {
					Object[] entry = new Object[] {result.getStack().get(i),result.getInput().get(i),result.getRule().get(i)};
					resultPanel.getResultTableModel().addRow(entry);
				}
				
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(parserPanel, "Erro");
			}
		}
	}

}
