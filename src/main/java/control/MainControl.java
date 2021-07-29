package control;

import view.MainView;

/**
 * Dedicated to control main view
 * @author douglas
 *
 */
public class MainControl {
    
    private MainView view = null;
    
    
    
    public MainControl(MainView view) {
	this.view = view;
    }



    public void run() {
	this.view.setVisible(true);
    }
}
