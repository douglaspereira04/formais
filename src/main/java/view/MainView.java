package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import view.automata.AutomataPanel;
import view.la.LexicalAnalyzerPanel;
import view.parser.ll1.LL1ParserPanel;
import view.regex.RegexPanel;
/**
 * 
 * @author douglas
 *
 */
public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8213771352061694300L;

	private JTabbedPane sideBar = new JTabbedPane();
	private AutomataPanel automataPanel = null;
	private RegexPanel regexPanel = null;
	private LexicalAnalyzerPanel laPanel = null;
	private LL1ParserPanel ll1ParserPanel = null;

	public MainView(AutomataPanel automataPanel, RegexPanel regexPanel, LexicalAnalyzerPanel laPanel, LL1ParserPanel ll1ParserPanel) {
		this.automataPanel = automataPanel;
		this.regexPanel = regexPanel;
		this.laPanel = laPanel;
		this.ll1ParserPanel = ll1ParserPanel;
		initialize();
		this.setLocationRelativeTo(null);

	}

	private void initialize() {

		this.setTitle("GALS");
		this.setSize(640, 480);
		this.setMinimumSize(new Dimension(480, 240));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.add(BorderLayout.CENTER, sideBar);

		sideBar.addTab("Automata", automataPanel);
		sideBar.addTab("Regex", regexPanel);
		sideBar.addTab("Lexical Analyser", laPanel);
		sideBar.addTab("LL1 Parser", ll1ParserPanel);

		sideBar.setTabPlacement(JTabbedPane.LEFT);
		sideBar.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}
}
