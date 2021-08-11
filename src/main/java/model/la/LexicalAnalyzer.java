package model.la;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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
	private final static String tokenPath = "";
	private final static String redefPath = "";

	Map<String, String> stateToToken = null;
	Automata automata = null;
	
	
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
	
	public static void storeRegularDefinition(String input) {
		FileWriter fw;
		try {
			fw = new FileWriter(redefPath,true);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(new StringReader(input));
			String redef = null;
			while ((redef = br.readLine()) != null) {
				bw.write(redef);
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> loadRegularDefinition() {
		Map<String, String> redefMap = new HashMap<>();
		FileReader fr;
		try {
			fr = new FileReader(redefPath);
		BufferedReader br = new BufferedReader(fr);
		while (br.ready()) {
			StringTokenizer st = new StringTokenizer(br.readLine(), SE);
			redefMap.put(st.nextToken(), st.nextToken());
		}
		br.close();
		fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return redefMap;
	}
	
	public static void storeToken(String input) {
		FileWriter fw;
		try {
			fw = new FileWriter(tokenPath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(new StringReader(input));
			String token = null;
			while ((token = br.readLine()) != null) {
				bw.write(redefParser(token));
				bw.newLine();
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, String> loadToken() {
		Map<String, String> tokenMap = new HashMap<>();
		FileReader fr;
		try {
			fr = new FileReader(tokenPath);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				StringTokenizer st = new StringTokenizer(br.readLine(), SE);
				tokenMap.put(st.nextToken(), st.nextToken());
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tokenMap;
	}
	
	public static String redefParser(String token) {
		StringTokenizer st = new StringTokenizer(token, SE);
		Map<String, String> redefMap = loadRegularDefinition();
		String id = st.nextToken();
		String regex = st.nextToken();
		for (String redef : redefMap.keySet()) {
			if (regex.contains(redef)) {
				regex.replace(redef, redefMap.get(redef));
			}
		}
		String parsedToken = id.concat(SE).concat(regex);
		return parsedToken;
	}
	
	public static Automata parser() throws BracketMismatchException, OperatorMismatchException, InvalidInputException, DuplicatedStateException, InvalidStateException, DuplicatedTransitionException {
		Map<String, String> tokenMap = loadToken();
		List<Automata> tokenFDAlist = new ArrayList<>();
		for (String token : tokenMap.keySet()) {
			Regex regex = new Regex(tokenMap.get(token));
			tokenFDAlist.add(regex.convert());
		}
		Automata parser = Automata.unify(tokenFDAlist).determinize();
		return parser;
	}
}
