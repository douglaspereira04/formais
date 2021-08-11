package app;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import control.MainControl;
import control.automata.AutomataControl;
import control.la.LexicalAnalyzerControl;
import view.MainView;
import view.automata.AutomataPanel;
import view.la.LexicalAnalyzerPanel;
import view.regex.RegexPanel;
import view.util.Splash;

/**
 * App main
 * 
 * @author douglas
 *
 */
public class Main {

	private static MainView mainView;
	private static MainControl mainControl;

	private static AutomataPanel automataPanel;
	private static RegexPanel regexPanel;
	private static LexicalAnalyzerPanel laPanel;

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
		mainView = new MainView(automataPanel, regexPanel, laPanel);
		AutomataControl automataControl = new AutomataControl(automataPanel);
		LexicalAnalyzerControl laControl = new LexicalAnalyzerControl(laPanel);

		splash.getProgressBar().setValue(90);

		mainControl = new MainControl(mainView);
		splash.getProgressBar().setValue(95);

		mainControl.run();
		splash.getProgressBar().setValue(100);
		splash.dispose();

	}

}
