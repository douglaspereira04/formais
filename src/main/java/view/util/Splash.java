package view.util;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Splash extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 5780657149662445339L;
    
    private JPanel panel = new JPanel();
    private JProgressBar progressBar = new JProgressBar();
    private JLabel label = new JLabel();
    
    public Splash() {
	initialize();
    }
    
    
    
    
    private void initialize() {

	this.setSize(400, 280);
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.setUndecorated(true);
	this.setLocationRelativeTo(null);
	this.setVisible(true);
	
	this.setLayout(new BorderLayout());

	
	label.setSize(new Dimension(200,200));
	label.setPreferredSize(new Dimension(200,200));
	label.setMaximumSize(new Dimension(200,200));
	label.setHorizontalAlignment(JLabel.CENTER);
	label.setVerticalAlignment(JLabel.CENTER);
	
	panel.setLayout(new BorderLayout());
	panel.add(BorderLayout.CENTER, label);
	
	this.add(BorderLayout.CENTER, panel);
	this.add(BorderLayout.SOUTH, progressBar);
	
	
	

    }




    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    
    public void setText(String text) {
	label.setText(text);
    }
    
    
}
