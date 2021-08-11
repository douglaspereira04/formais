package control.la;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import model.io.FileUtils;
import view.la.LexicalAnalyzerPanel;

/**
 * 
 * Class dedicated to control a lexical analyzer panel behavior
 * @author douglas
 * @author tiago
 *
 */
public class LexicalAnalyzerControl {

	LexicalAnalyzerPanel laPanel = null;
	
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
					System.getProperty("user.dir") + System.getProperty("file.separator") + "new.la",
					this.laPanel);
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
		File path = FileUtils.selectImportFile("*.la", this.laPanel);
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

}
