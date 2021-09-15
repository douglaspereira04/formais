package control.la;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import app.Main;
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
import view.LRparser.LRparserPanel;
import view.la.LexicalAnalyzerPanel;
import view.la.LexicalAnalyzerPanel.LexicalAnalyzerTab;
import view.parser.ll1.LL1ParserPanel;
import view.parser.ll1.LL1ParsingResultPanel;

/**
 * 
 * Class dedicated to control a lexical analyzer panel behavior
 * 
 * @author douglas
 * @author tiago
 *
 */
public class LexicalAnalyzerControl {

	private LexicalAnalyzerPanel laPanel = null;
	private LexicalAnalyzer analyzer = null;
	private List<LexicalEntry> analysis = null;

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

		this.laPanel.getAnalysisPanel().getExportButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					exportAnalysis();
				} catch (InvalidStateException e1) {
					JOptionPane.showMessageDialog(laPanel, "Nada para exportar");
					e1.printStackTrace();
				}
			}
		});

		this.laPanel.getAnalysisPanel().getLl1AnalysisButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openLL1();
				} catch (InvalidStateException e1) {
					JOptionPane.showMessageDialog(laPanel, "Nada para exportar");
					e1.printStackTrace();
				}
			}
		});

		this.laPanel.getAnalysisPanel().getLr1AnalysisButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openLR1();
				} catch (InvalidStateException e1) {
					JOptionPane.showMessageDialog(laPanel, "Nada para exportar");
					e1.printStackTrace();
				}
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
	 * Prompts user to load a lexical analyzer file from file system
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
		analysis = null;
		String text = this.laPanel.getAnalysisPanel().getTextArea().getText();
		List<LexicalEntry> entries = new ArrayList<LexicalEntry>();

		try {
			entries = this.analyzer.analyze(text);
			analysis = entries;
			this.laPanel.getAnalysisPanel().getAnalysisTable().clear();
			for (LexicalEntry lexicalEntry : entries) {
				this.laPanel.getAnalysisPanel().getAnalysisTable().addEntry(lexicalEntry.getToken(),
						lexicalEntry.getLexeme(), lexicalEntry.getPosition());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exportAnalysis() throws InvalidStateException {
		String tokens = getAnalysisString();

		try {
			File path = FileUtils.selectExportFile("*.lexical", laPanel, ".lexical");
			FileUtils.saveToFile(tokens, path.getAbsolutePath());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(laPanel, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private String getAnalysisString() throws InvalidStateException {

		if (analysis == null)
			throw new InvalidStateException("###NO ANALYSIS");
		String tokens = "";
		for (LexicalEntry lexicalEntry : analysis) {
			tokens += lexicalEntry.getToken() + " ";
		}
		tokens.substring(0, tokens.length() - 1);

		return tokens;
	}

	private void openLL1() throws InvalidStateException {
		String tokens = getAnalysisString();

		LL1ParserPanel parserPanel = Main.mainView.getLl1ParserPanel();
		LL1ParsingResultPanel resultPanel = parserPanel.getParsingResultPanel();
		Main.mainView.getSideBar().setSelectedComponent(parserPanel);
		resultPanel.getTokenTextArea().setText(tokens);
		parserPanel.getParserTabs().setSelectedComponent(resultPanel);
		

	}

	private void openLR1() throws InvalidStateException {
		String tokens = getAnalysisString();

		LRparserPanel parserPanel = Main.mainView.getLr1ParserPanel();
		parserPanel.getTokenTextArea().setText(tokens);

		Main.mainView.getSideBar().setSelectedComponent(parserPanel);
		
		
	}
}
