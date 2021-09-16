package control.LRparser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.LRparser.LRparser;
import model.LRparser.LRtable;
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
	LRtable table = null;

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
					parserPanel.initializeActionTablePanel();
				}
			}
		});

	}

	private void parse() {
		

	}

	private void createParsingTable() {
		parserPanel.initializeScroll();
		Map<Integer, Map<String, String>> actionTable = null;
		Map<Integer, Map<String, Integer>> gotoTable = null;

		Grammar grammar = null;
		grammar = new Grammar(parserPanel.getGrammarTextArea().getText());
		
		
		table = new LRtable(grammar);
		
		for (int i = 0; i < table.getStates().size(); i++) {
			parserPanel.addState(i);
		}

		for (String nonTeminal : grammar.getNonterminals()) {
			parserPanel.addGotoColumn(nonTeminal);
		}

		for (String terminal : grammar.getTerminals()) {
			parserPanel.addActionColumn(terminal);
		}
		parserPanel.addActionColumn("$");

		actionTable = table.getActionTable();
		gotoTable = table.getGotoTable();
		
		for (int i = 0; i < table.getStates().size(); i++) {
			if(actionTable.containsKey(i)){
				for (Entry<String, String> entry : actionTable.get(i).entrySet()) {
					String terminal = entry.getKey();
					String action = entry.getValue();
					parserPanel.addAction(terminal, action, i);
				}
			}
		}
		
		for (int i = 0; i < table.getStates().size(); i++) {
			if(gotoTable.containsKey(i)){
				for (Entry<String, Integer> entry : gotoTable.get(i).entrySet()) {
					String nonTerminal = entry.getKey();
					String gotoString = entry.getValue().toString();
					parserPanel.addGoto(nonTerminal, gotoString, i);
				}
			}
		}

		parserPanel.repaint();
		System.err.println(Collections.singletonList(actionTable));

	}

}
