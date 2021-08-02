package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import view.automata.AutomataPanel;
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

	public MainView(AutomataPanel automataPanel, RegexPanel regexPanel) {
		this.automataPanel = automataPanel;
		this.regexPanel = regexPanel;
		initialize();
		this.setLocationRelativeTo(null);

	}

	private void initialize() {

		this.setTitle("GALS");
		this.setSize(480, 240);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.add(BorderLayout.CENTER, sideBar);

		sideBar.addTab("Automata", automataPanel);
		sideBar.addTab("Regex", regexPanel);

		sideBar.setTabPlacement(JTabbedPane.LEFT);
		sideBar.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}
}
