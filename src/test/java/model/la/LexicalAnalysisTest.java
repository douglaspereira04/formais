package model.la;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import model.automata.Automata;
import view.la.AnalysisTable;

public class LexicalAnalysisTest {

	@Test
	public void LexicalAnalysisTest() throws InvalidStateException, DuplicatedStateException, DuplicatedTransitionException {

		Automata automata = new Automata();
		Map<String, String> stateToToken = new HashMap<String, String>();
		AnalysisTable table = new AnalysisTable();
		Character[] inputText = new Character[50];
		String acceptedState = null;
		String lastAcceptedState = null;
		String token = null;
		int position = 0;

		int length = 0;

		boolean lexicalError = false;

		String lexeme = "";
		String lastLexeme = "";

		for (int i = 0; i < inputText.length; i++) {
			lexeme = lexeme.concat(String.valueOf(inputText[i]));

			length++;
			acceptedState = automata.compute(lexeme);

			if (acceptedState == null) {
				if (!(i+1 < inputText.length)) {
					lexicalError = true;
					position = i;
					break;
				}
				continue;
			}

			while (acceptedState != null) {
				length++;
				lastAcceptedState = acceptedState;
				lastLexeme = lexeme;

				i++;

				if (!(i < inputText.length)) {
					break;
				}

				lexeme = lexeme.concat(String.valueOf(inputText[i]));
				acceptedState = automata.compute(lexeme);
			}
			
			position = i - length;
			token = stateToToken.get(lastAcceptedState);
			table.addEntry(token, lastLexeme, position);
			length = 0;
			
		}
		
		if (lexicalError) {
			table.addEntry("ERROR", lexeme, position);
		}

	}

}
