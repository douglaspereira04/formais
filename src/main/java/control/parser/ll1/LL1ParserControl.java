package control.parser.ll1;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import exception.automata.InvalidStateException;
import model.parser.ll1.LL1Parser;
import view.parser.ll1.LL1ParserPanel;


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
				}
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

}
