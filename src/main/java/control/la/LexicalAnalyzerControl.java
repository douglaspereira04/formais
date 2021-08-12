package control.la;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.io.FileUtils;
import model.la.LexicalAnalyzer;
import model.la.LexicalEntry;
import model.regex.Regex;
import view.la.LexicalAnalyzerPanel;
import view.la.LexicalAnalyzerPanel.LexicalAnalyzerTab;

/**
 * 
 * Class dedicated to control a lexical analyzer panel behavior
 * 
 * @author douglas
 * @author tiago
 *
 */
public class LexicalAnalyzerControl {

	LexicalAnalyzerPanel laPanel = null;
	LexicalAnalyzer analyzer = null;

	public LexicalAnalyzerControl(LexicalAnalyzerPanel laPanel) {

		this.laPanel = laPanel;

		initializeBehavior();
	}

	private void initializeBehavior() {

		this.laPanel.getDefinitionPanel().getSaveButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		this.laPanel.getDefinitionPanel().getLoadButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});

		this.laPanel.getDefinitionPanel().getClearButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});

		this.laPanel.getDefinitionPanel().getSaveAutomataButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAutomata();
			}
		});

		this.laPanel.getTabs().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (laPanel.getTabs().getSelectedComponent() == laPanel.getAnalysisPanel()) {
					changeToAnalyzeTab();
				}
			}
		});

		this.laPanel.getAnalysisPanel().getTextArea().addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				analyze();
			}

		});
	}

	/**
	 * Prompt user to save displaying lexycal analyzer to file
	 */
	private void save() {
		String[] data = null;
		try {
			data = new String[2];
			data[0] = this.laPanel.getDefinitionPanel().getRegexTextArea().getText();
			data[1] = this.laPanel.getDefinitionPanel().getTokenTextArea().getText();

			File path = FileUtils.selectExportFile(
					System.getProperty("user.dir") + System.getProperty("file.separator") + "new.la", this.laPanel);
			FileUtils.saveToFile(data, path.getAbsolutePath());

			System.out.println("### LEXYCAL ANALYZER EXTRACTED ###");
			System.err.println("### NOT VALIDATED YET ###");

		} catch (Exception e) {
			System.out.println("### LEXYCAL ANALYZER NOT EXTRACTED ###");
			System.err.println("### ERROR ###");
			e.printStackTrace();
		}

	}

	/**
	 * Prompts user to load a lexycal analyzer file from file system
	 */
	private void load() {
		if (!(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(laPanel,
				"All current definitions will be lost. Continue?", "Confirmation", JOptionPane.YES_NO_OPTION)))
			return;

		File path = FileUtils.selectImportFile("*.la", this.laPanel);
		if (path == null)
			return;

		String[] data = null;
		try {
			data = FileUtils.loadFromFile(path.getAbsolutePath(), String[].class);

			this.laPanel.getDefinitionPanel().getRegexTextArea().setText(data[0]);
			this.laPanel.getDefinitionPanel().getTokenTextArea().setText(data[1]);

		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(laPanel, "Invalid file", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Clears text areas in lexical analyzer panel
	 */
	private void clear() {
		this.laPanel.getDefinitionPanel().getRegexTextArea().setText("");
		this.laPanel.getDefinitionPanel().getTokenTextArea().setText("");

	}

	private void saveAutomata() {

		String definitions = this.laPanel.getDefinitionPanel().getRegexTextArea().getText();
		String tokens = this.laPanel.getDefinitionPanel().getTokenTextArea().getText();

		try {
			Automata automata = (new LexicalAnalyzer(definitions, tokens)).getAutomata();
			File path = FileUtils.selectExportFile("*.automata", laPanel);
			FileUtils.saveToFile(automata, path.getAbsolutePath());
		} catch (DuplicatedStateException | BracketMismatchException | OperatorMismatchException | InvalidInputException
				| InvalidStateException | DuplicatedTransitionException e) {
			JOptionPane.showMessageDialog(laPanel, "Couldn't generate automata", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(laPanel, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void changeToAnalyzeTab() {
		String definitions = this.laPanel.getDefinitionPanel().getRegexTextArea().getText();
		String tokens = this.laPanel.getDefinitionPanel().getTokenTextArea().getText();

		try {
			this.analyzer = new LexicalAnalyzer(definitions, tokens);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(laPanel,
					"Definitions are not correctely set. Set definitions before analysing.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			this.laPanel.setTab(LexicalAnalyzerTab.DEFINITIONS);
		}
	}

	private void analyze() {
		String text = this.laPanel.getAnalysisPanel().getTextArea().getText();
		List<LexicalEntry> entries = new ArrayList<LexicalEntry>();

		try {
			entries = this.analyzer.analyze(text);
			this.laPanel.getAnalysisPanel().getAnalysisTable().clear();
			for (LexicalEntry lexicalEntry : entries) {
				this.laPanel.getAnalysisPanel().getAnalysisTable().addEntry(lexicalEntry.getToken(),
						lexicalEntry.getLexeme(), lexicalEntry.getPosition());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
