package model.la;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.regex.Regex;

/**
 * 
 * Class dedicated to manage lexical analyzer
 * @author douglas
 * @author tiago
 *
 */
public class LexicalAnalyzer {
	
	private final static String SE = " : ";

	private List<Object[]> stateToToken = null;
	private Automata automata = null;
	
	public LexicalAnalyzer() {
		stateToToken = new ArrayList<Object[]>();
	}
	
	public LexicalAnalyzer(String definitions, String tokens) throws BracketMismatchException, OperatorMismatchException, InvalidInputException, DuplicatedStateException, InvalidStateException, DuplicatedTransitionException {
		stateToToken = new ArrayList<Object[]>();
		this.setDefinitions(definitions, tokens);
	}
	
	public List<Object[]> getStateToToken() {
		return this.stateToToken;
	}
	
	/**
	 * Given a final state returns the token id
	 * @param state - final state
	 * @return token id of {@link String} type
	 */
	private String getTokenID(String state) {

		List<String> states = Automata.stringToStateList(state);
		Object[] candidateEntry = new Object[] {null, null, Integer.valueOf(Integer.MAX_VALUE)};
		for (Object[] entry : stateToToken) {
			if (states.contains(entry[0])) {
				if ((Integer)candidateEntry[2] > (Integer)entry[2]) {
					candidateEntry = entry;
				}
			}
		}
		
		if(candidateEntry[0]==null)
			return null;
		return (String)candidateEntry[1];
	}
	
	/**
	 * Analyze a text
	 * @param text - to be analyzed
	 * @return {@link List} {@code <}{@link LexicalEntry}{@code >} of tokens found
	 * @throws InvalidStateException
	 * @throws DuplicatedStateException
	 * @throws DuplicatedTransitionException
	 */
	public List<LexicalEntry> analyze(String text) throws InvalidStateException, DuplicatedStateException, DuplicatedTransitionException{
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
				lastAcceptedState = acceptedState;
				lastLexeme = lexeme;


				if (!(i+1 < inputText.length)) {
					break;
				}
				
				i++;
				lexeme = lexeme.concat(String.valueOf(inputText[i]));
				length++;
				
				acceptedState = automata.compute(lexeme);
			}
			
			position = i - length;
			token = getTokenID(lastAcceptedState);
			System.out.println(lastAcceptedState);
			entries.add(new LexicalEntry(token, lastLexeme, position));
			length = 0;
			
			if (acceptedState == null) {
				i--;
				lexeme = "";
			}
			
		}
		
		if (lexicalError) {
			entries.add(new LexicalEntry("ERROR", lastLexeme, position));
		}
		
		return entries;
		
	}
	
	public Map<String, String> loadRegularDefinition(List<String> redefList) {
		Map<String, String> redefMap = new HashMap<>();
		for (String line : redefList) {
			StringTokenizer redefST = new StringTokenizer(line, SE);
			String id = redefST.nextToken();
			String redef = redefST.nextToken();
			redefMap.put(redef, id);
		}
		
		return redefMap;
	}
	
	public Map<String, Object[]> loadToken(List<String> tokenList, List<String> redefList) {
		Map<String, Object[]> tokenMap = new HashMap<>();
		int index = 0;
		for (String line : tokenList) {
			StringTokenizer tokenST = new StringTokenizer(line, SE);
			String temp = redefParser(line, redefList);
			tokenMap.put(temp, new Object[] {tokenST.nextToken().trim(), Integer.valueOf(index)});
			index++;
		}
		return tokenMap;
	}
	
	public String redefParser(String token, List<String> redefString) {
		StringTokenizer st = new StringTokenizer(token, SE);
		Map<String, String> redefMap = loadRegularDefinition(redefString);
		st.nextToken();
		String regex = st.nextToken();
		for (String redef : redefMap.keySet()) {
			if (regex.contains(redefMap.get(redef))) {
				regex = regex.replace(redefMap.get(redef), "("+redef+")");
			}
		}
		return regex;
	}
	
	public Automata parser(List<String> tokenList, List<String> redefList) throws BracketMismatchException, OperatorMismatchException, InvalidInputException, DuplicatedStateException, InvalidStateException, DuplicatedTransitionException {
		Map<String, Object[]> tokenMap = loadToken(tokenList, redefList);
		HashMap<Automata, Object[]> automataToID = new HashMap<Automata, Object[]>();
				
		List<Automata> tokenFDAlist = new ArrayList<>();
		for (String regexString : tokenMap.keySet()) {
			Regex regex = new Regex(regexString);
			Automata automaton = regex.convert();
			tokenFDAlist.add(automaton);
			automataToID.put(automaton, tokenMap.get(regexString));
		}
		
		Automata automaton = Automata.unify(tokenFDAlist);
		
		for (String finalState : automaton.getFinalStates()) {
			String[] splited = finalState.split("-");
			int index = Integer.parseInt(splited[splited.length-1])-1;
			String id = (String)automataToID.get(tokenFDAlist.get(index))[0];
			Integer precedence = (Integer)automataToID.get(tokenFDAlist.get(index))[1];
			stateToToken.add(new Object[] {finalState, id, precedence});
		}
		
		
		return automaton.determinize();
	}
	
	public void setDefinitions(String definitions, String tokens) throws BracketMismatchException, OperatorMismatchException, InvalidInputException, DuplicatedStateException, InvalidStateException, DuplicatedTransitionException {
		List<String> definitionList = Arrays.asList(definitions.split("\n"));
		List<String> tokenList = Arrays.asList(tokens.split("\n"));
		
		this.automata = parser(tokenList, definitionList);
	}
	
	public Automata getAutomata() {
		return this.automata;
	}
}
