package app;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import control.MainControl;
import control.automata.AutomataControl;
import control.la.LexicalAnalyzerControl;
import control.parser.ll1.LL1ParserControl;
import control.regex.RegexControl;
import view.MainView;
import view.automata.AutomataPanel;
import view.la.LexicalAnalyzerPanel;
import view.parser.ll1.LL1ParserPanel;
import view.regex.RegexPanel;
import view.util.Splash;

/**
 * App main
 * 
 * @author douglas
 *
 */
public class Main {

	public static MainView mainView;
	private static MainControl mainControl;

	private static AutomataPanel automataPanel;
	private static RegexPanel regexPanel;
	private static LexicalAnalyzerPanel laPanel;
	private static LL1ParserPanel ll1ParserPanel;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}

		Splash splash = new Splash();
		splash.getProgressBar().setValue(10);

		automataPanel = new AutomataPanel();
		regexPanel = new RegexPanel();
		laPanel = new LexicalAnalyzerPanel();
		ll1ParserPanel = new LL1ParserPanel();
		mainView = new MainView(automataPanel, regexPanel, laPanel, ll1ParserPanel);
		AutomataControl automataControl = new AutomataControl(automataPanel);
		LexicalAnalyzerControl laControl = new LexicalAnalyzerControl(laPanel);
		RegexControl regexControl = new RegexControl(regexPanel);
		LL1ParserControl ll1ParserControl = new LL1ParserControl(ll1ParserPanel);

		splash.getProgressBar().setValue(90);

		mainControl = new MainControl(mainView);
		splash.getProgressBar().setValue(95);

		mainControl.run();
		splash.getProgressBar().setValue(100);
		splash.dispose();

	}

}
