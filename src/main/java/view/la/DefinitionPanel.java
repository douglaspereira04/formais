package view.la;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import view.util.WrapLayout;

public class DefinitionPanel extends JPanel {
	

	JPanel toolbar = null;
	JButton loadButton = null;
	JButton saveButton = null;
	JButton clearButton = null;
	JButton checkButton = null;
	
	JPanel inputAreaPanel = null;
	
	JScrollPane regexScroll = null;
	JPanel regexPanel = null;
	RegexTextArea regexTextArea = null;

	JScrollPane tokenScroll = null;
	JPanel tokenPanel = null;
	TokenTextArea tokenTextArea = null;
	
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

		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(4,4,4,4);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.inputAreaPanel.add(new JLabel("Definições"), constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		this.inputAreaPanel.add(new JLabel("Tokens"), constraints);

		constraints.weightx = 1;
		constraints.weighty = 1;	
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.inputAreaPanel.add(this.regexScroll, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		this.inputAreaPanel.add(this.tokenScroll, constraints);
		
	}
	
	private void initializeRegexInputArea() {
		this.regexScroll = new JScrollPane();
		this.regexTextArea = new RegexTextArea();
		this.regexScroll.setViewportView(this.regexTextArea);
		this.regexScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	private void initializeTokenInputArea() {
		this.tokenScroll = new JScrollPane();
		this.tokenTextArea = new TokenTextArea();
		this.tokenScroll.setViewportView(this.tokenTextArea);
		this.tokenScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
}
