package control.LRparser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.LRparser.LRparser;
import model.grammar.Grammar;
import view.LRparser.LRparserPanel;

/**
 * Class dedicated to control LRParserPanel behavior
 * 
 * @author douglas
 *
 */
public class LRparserControl {

	LRparserPanel parserPanel = null;
	LRparser parser = null;

	public LRparserControl(LRparserPanel parserPanel) {
		this.parserPanel = parserPanel;

		initializeBehavior();
	}

	private void initializeBehavior() {

		this.parserPanel.getParseButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parse();
			}
		});

		this.parserPanel.getGrammarTextArea().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					createParsingTable();
				} catch (Exception e2) {
					e2.printStackTrace();
					parserPanel.initializeParsingTablePanel();
				}
			}
		});

	}

	private void parse() {
		// TODO Auto-generated method stub

	}

	private void createParsingTable() {
		// TODO Auto-generated method stub

	}

}
