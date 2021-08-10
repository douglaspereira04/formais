package model.la;

/**
 * POJO to hold lexical analysis entry from text
 * @author douglas
 *
 */
public class LexicalEntry {

	private String token = null;
	private String lexeme = null;
	private Integer position = null;
	
	public LexicalEntry() {
		// TODO Auto-generated constructor stub
	}

	public LexicalEntry(String token, String lexeme, int position) {
		super();
		this.token = token;
		this.lexeme = lexeme;
		this.position = position;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
