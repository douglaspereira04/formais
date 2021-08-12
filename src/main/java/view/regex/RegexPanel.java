package view.regex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.util.WrapLayout;

public class RegexPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3012082130199268109L;
	
	JPanel panel = null;
	JTextField regexField = null;
	JButton saveButton = null;
	
	
	public RegexPanel() {
		this.setLayout(new BorderLayout());
		this.initializePanel();
		this.add(BorderLayout.NORTH, panel);
	}
	
	private void initializePanel() {
		
		regexField = new JTextField(20);
		saveButton = new JButton("Save automata");
		
		panel = new JPanel(new WrapLayout());
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		
		panel.add(regexField);
		panel.add(saveButton);
		
	}

	public JTextField getRegexField() {
		return regexField;
	}

	public void setRegexField(JTextField regexField) {
		this.regexField = regexField;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(JButton saveButton) {
		this.saveButton = saveButton;
	}
	

}
