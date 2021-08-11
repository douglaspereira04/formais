package view.la;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import view.util.WrapLayout;

/**
 * Panel dedicated to display lexical analyser definition input interface
 * @author douglas
 *
 */
public class DefinitionPanel extends JPanel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8628544071906669012L;
	
	JPanel toolbar = null;
	JButton loadButton = null;
	JButton saveButton = null;
	JButton clearButton = null;
	JButton checkButton = null;
	
	JPanel inputAreaPanel = null;
	
	JScrollPane regexScroll = null;
	JPanel regexPanel = null;
	HighlightedTextPane regexTextArea = null;

	JScrollPane tokenScroll = null;
	JPanel tokenPanel = null;
	HighlightedTextPane tokenTextArea = null;
	
	JLabel definitionLabel = null;
	JLabel tokenLabel = null;
	
	public DefinitionPanel() {
		this(new BorderLayout());
	}

	public DefinitionPanel(LayoutManager layout) {
		super(layout);
		
		initializeToolBar();
		initializeInputArea();

		this.add(BorderLayout.NORTH, this.toolbar);
		this.add(BorderLayout.CENTER, this.inputAreaPanel);
	}
	
	private void initializeToolBar() {
		this.toolbar = new JPanel(new WrapLayout());
		
		this.loadButton = new JButton("Load");
		this.saveButton = new JButton("Save");
		this.clearButton = new JButton("Clear");
		this.checkButton = new JButton("Check");

		this.toolbar.add(this.loadButton);
		this.toolbar.add(this.saveButton);
		this.toolbar.add(this.clearButton);
		this.toolbar.add(this.checkButton);
	}
	
	private void initializeInputArea() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		this.inputAreaPanel = new JPanel(layout);
		

		initializeRegexInputArea();
		initializeTokenInputArea();

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(4,4,4,4);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		definitionLabel = new JLabel("Definições");
		this.inputAreaPanel.add(definitionLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		tokenLabel = new JLabel("Tokens");
		this.inputAreaPanel.add(tokenLabel, constraints);
		

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;	
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.inputAreaPanel.add(this.regexScroll, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		this.inputAreaPanel.add(this.tokenScroll, constraints);
		
	}
	
	private void initializeRegexInputArea() {
		this.regexScroll = new JScrollPane();
		this.regexTextArea = new HighlightedTextPane();
		this.regexScroll.setViewportView(this.regexTextArea);
		this.regexScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	private void initializeTokenInputArea() {
		this.tokenScroll = new JScrollPane();
		this.tokenTextArea = new HighlightedTextPane();
		this.tokenScroll.setViewportView(this.tokenTextArea);
		this.tokenScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public JButton getLoadButton() {
		return loadButton;
	}

	public void setLoadButton(JButton loadButton) {
		this.loadButton = loadButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(JButton saveButton) {
		this.saveButton = saveButton;
	}

	public JButton getClearButton() {
		return clearButton;
	}

	public void setClearButton(JButton clearButton) {
		this.clearButton = clearButton;
	}

	public JButton getCheckButton() {
		return checkButton;
	}

	public void setCheckButton(JButton checkButton) {
		this.checkButton = checkButton;
	}

	public HighlightedTextPane getRegexTextArea() {
		return regexTextArea;
	}

	public void setRegexTextArea(HighlightedTextPane regexTextArea) {
		this.regexTextArea = regexTextArea;
	}

	public HighlightedTextPane getTokenTextArea() {
		return tokenTextArea;
	}

	public void setTokenTextArea(HighlightedTextPane tokenTextArea) {
		this.tokenTextArea = tokenTextArea;
	}
	
	
	
}
