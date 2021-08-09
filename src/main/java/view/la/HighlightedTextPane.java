package view.la;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class HighlightedTextPane extends JTextPane {

	Color color0 = Color.BLACK;
	Color color1 = new Color(127,0,0);
	Color color2 = new Color(0,127,0);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2491134656031211708L;

	public HighlightedTextPane() {
		// TODO Auto-generated constructor stub
		super();
		
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyTyped(e);
				hightlight();
			}
			
		});
	}

	public void append(String msg, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		int len = this.getDocument().getLength();
		this.setCaretPosition(len);
		this.setCharacterAttributes(aset, false);
		this.replaceSelection(msg);
	}

	public void append(char character, Color c) {
		append(String.valueOf(character), c);
	}

	private void hightlight() {
		char[] text = this.getText().toCharArray();
		int caretPosition = this.getCaretPosition();

		this.setText("");

		for (int i = 0; i < text.length; i++) {
			if (text[i] == ':') {
				while (true) {
					append(text[i], color2);
					i++;
					if ( !(i<text.length) ){
						break;
					} else if (text[i] == '\n'){
						append(text[i], color2);
						break;
					}
				}
			}else if (text[i] == '"') {
				while (true) {
					append(text[i], color1);
					i++;
					if ( !(i<text.length) ){
						break;
					} else if(text[i] == '"' || text[i] == '\n') {
						append(text[i], color1);
						break;
					}
					
				}

			} else {
				append(text[i], color0);
			}

		}

		try {
			this.setCaretPosition(caretPosition);
		} catch (IllegalArgumentException e) {
			this.setCaretPosition(this.getDocument().getLength());
		}
	}

}
