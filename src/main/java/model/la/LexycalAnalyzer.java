package model.la;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import model.automata.Automata;

public class LexycalAnalyzer {
	
	public static List<LexicalEntry> analyze(String text, Automata automata, Map<String, String> stateToToken) throws InvalidStateException, DuplicatedStateException, DuplicatedTransitionException{
		List<LexicalEntry> entries = new ArrayList<LexicalEntry>();
		char[] inputText = text.toCharArray();
		
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
			entries.add(new LexicalEntry(token, lastLexeme, position));
			length = 0;
			
		}
		
		if (lexicalError) {
			entries.add(new LexicalEntry("ERROR", lexeme, position));
		}
		
		return entries;
		
	}

}
