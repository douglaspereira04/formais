package app;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import control.MainControl;
import control.automata.AutomataControl;
import view.MainView;
import view.automata.AutomataPanel;
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
		mainView = new MainView(automataPanel, regexPanel);
		AutomataControl automataControl = new AutomataControl(automataPanel);

		splash.getProgressBar().setValue(90);

		mainControl = new MainControl(mainView);
		splash.getProgressBar().setValue(95);

		mainControl.run();
		splash.getProgressBar().setValue(100);
		splash.dispose();

	}

}
