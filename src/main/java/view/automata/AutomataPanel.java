package view.automata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import model.automata.Automata;
import view.util.WrapLayout;

/**
 * Panel dedicated to manipulate automaton
 * @author douglas
 *
 */
public class AutomataPanel extends JPanel{

    /**
     * 
     */
    private static final long serialVersionUID = 8670784410886817455L;
    
    JPanel toolbar = null;
    JScrollPane transitionTableScroll = null;
    AutomataTableModel transitionTableModel = null;
    JTable transitionTable = null;

    JButton saveAutomataButton = null;
    JButton clearAutomataButton = null;
    JButton deleteAutomataButton = null;
    JButton completeAutomataButton = null;
    JButton unifyAutomataButton = null;
    JButton epsilonClosureButton = null;
    JButton addColumnButton = null;
    JButton addRowButton = null;
    JComboBox<String> automataComboBox = null;
    
    public AutomataPanel(Automata automata) {
	
    }
    
    public AutomataPanel() {
	this.setLayout(new BorderLayout());
	
	
	initializeToolBar();
	
	initializeTransitionTable();
	
	this.add(BorderLayout.NORTH, toolbar);
	this.add(BorderLayout.CENTER, transitionTableScroll);
	
    }
    
    private void initializeToolBar() {

	addColumnButton = new JButton("Add Column");
	addRowButton = new JButton("Add Row");
	automataComboBox = new JComboBox<String>();
	saveAutomataButton = new JButton("Save");
	clearAutomataButton = new JButton("Clear");
	deleteAutomataButton = new JButton("Delete");
	completeAutomataButton = new JButton("Complete");
	unifyAutomataButton = new JButton("Unify");
	epsilonClosureButton = new JButton("Display Epsilon Closure");
	
	toolbar = new JPanel(new WrapLayout());
	toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

	toolbar.add(automataComboBox);
	toolbar.add(saveAutomataButton);
	toolbar.add(deleteAutomataButton);
	
	toolbar.add(new ToolbarSeparator());
	toolbar.add(completeAutomataButton);
	toolbar.add(unifyAutomataButton);
	toolbar.add(epsilonClosureButton);

	toolbar.add(new ToolbarSeparator());
	toolbar.add(clearAutomataButton);
	toolbar.add(addRowButton);
	toolbar.add(addColumnButton);
	

	
	addRowButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		transitionTableModel.addRow();
	    }
	});

	addColumnButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		transitionTableModel.addColumn();
	    }
	});

	clearAutomataButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		transitionTableModel = new AutomataTableModel();
		transitionTable.setModel(transitionTableModel);
	    }
	});
    }
    
    private void initializeTransitionTable() {

	transitionTableScroll = new JScrollPane();
	transitionTable = new JTable();
	transitionTableModel = new AutomataTableModel();
	
	transitionTable.setModel(transitionTableModel);
	transitionTableScroll.setViewportView(transitionTable);
	
	transitionTableScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	transitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	transitionTable.setGridColor(Color.LIGHT_GRAY); 
	transitionTable.setShowGrid(true);
	transitionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    
    private class ToolbarSeparator extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7048708593522245197L;

	public ToolbarSeparator() {
	    // TODO Auto-generated constructor stub
	    super("|");
	    this.setEnabled(false);
	}
	
    }

    
    //Getters and Setters
    
    public JTable getTransitionTable() {
        return transitionTable;
    }

    public void setTransitionTable(JTable transitionTable) {
        this.transitionTable = transitionTable;
    }

    public JButton getSaveAutomataButton() {
        return saveAutomataButton;
    }

    public void setSaveAutomataButton(JButton saveAutomataButton) {
        this.saveAutomataButton = saveAutomataButton;
    }

    public JButton getClearAutomataButton() {
        return clearAutomataButton;
    }

    public void setClearAutomataButton(JButton clearAutomataButton) {
        this.clearAutomataButton = clearAutomataButton;
    }

    public JButton getDeleteAutomataButton() {
        return deleteAutomataButton;
    }

    public void setDeleteAutomataButton(JButton deleteAutomataButton) {
        this.deleteAutomataButton = deleteAutomataButton;
    }

    public JButton getCompleteAutomataButton() {
        return completeAutomataButton;
    }

    public void setCompleteAutomataButton(JButton completeAutomataButton) {
        this.completeAutomataButton = completeAutomataButton;
    }

    public JButton getUnifyAutomataButton() {
        return unifyAutomataButton;
    }

    public void setUnifyAutomataButton(JButton unifyAutomataButton) {
        this.unifyAutomataButton = unifyAutomataButton;
    }

    public JButton getEpsilonClosureButton() {
        return epsilonClosureButton;
    }

    public void setEpsilonClosureButton(JButton epsilonClosureButton) {
        this.epsilonClosureButton = epsilonClosureButton;
    }

    public JButton getAddColumnButton() {
        return addColumnButton;
    }

    public void setAddColumnButton(JButton addColumnButton) {
        this.addColumnButton = addColumnButton;
    }

    public JButton getAddRowButton() {
        return addRowButton;
    }

    public void setAddRowButton(JButton addRowButton) {
        this.addRowButton = addRowButton;
    }

    public JComboBox<String> getAutomataComboBox() {
        return automataComboBox;
    }

    public void setAutomataComboBox(JComboBox<String> automataComboBox) {
        this.automataComboBox = automataComboBox;
    }
    
    
    
    

}
