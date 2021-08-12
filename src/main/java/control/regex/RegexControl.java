package control.regex;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.io.FileUtils;
import model.regex.Regex;
import view.regex.RegexPanel;

public class RegexControl {
	
	RegexPanel regexPanel = null;
	
	public RegexControl(RegexPanel regexPanel) {
		this.regexPanel = regexPanel;
		
		regexPanel.getSaveButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				save();
			}
		});
	}
	
	private void save() {
		String regex = regexPanel.getRegexField().getText();
		
		try {
			Automata automata = (new Regex(regex)).convert();
			File path = FileUtils.selectExportFile("*.automata", regexPanel);
			FileUtils.saveToFile(automata, path.getAbsolutePath());
		} catch (DuplicatedStateException | InvalidStateException | DuplicatedTransitionException | BracketMismatchException | OperatorMismatchException | InvalidInputException e) {
			JOptionPane.showMessageDialog(regexPanel, "Couldn't convert to automata", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(regexPanel, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
