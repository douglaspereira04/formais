package model.la;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.automata.Transition;
public class LexicalAnalysisTest {
	
	@Test
	public void LexicalAnalyzerFileReadTest() {
		
		System.out.println("INICIO TESTE 1");
		
		try {
			FileReader redefFR = new FileReader(this.getClass().getClassLoader().getResource("redef.txt").getPath());
			FileReader tokenFR = new FileReader(this.getClass().getClassLoader().getResource("token.txt").getPath());
			BufferedReader redefBR = new BufferedReader(redefFR);
			BufferedReader tokenBR = new BufferedReader(tokenFR);
			List<String> redefList = new ArrayList<>();
			List<String> tokenList = new ArrayList<>();
			while (redefBR.ready()) redefList.add(redefBR.readLine());
			while (tokenBR.ready()) tokenList.add(tokenBR.readLine());
			for (String line : redefList) System.out.println(line);
			for (String line : tokenList) System.out.println(line);
			redefFR.close();
			redefBR.close();
			tokenFR.close();
			tokenBR.close();
		} catch (IOException e) {
			System.err.println("Arquivo Não Encontrado");
			e.printStackTrace();
		}
		
		System.out.println("TERMINO TESTE 1");
		
	}
	
	@Test
	public void LexicalAnalyzerLoadTest() {
		
		System.out.println("INICIO TESTE 2");
		
		try {
			FileReader redefFR = new FileReader(this.getClass().getClassLoader().getResource("redef.txt").getPath());
			FileReader tokenFR = new FileReader(this.getClass().getClassLoader().getResource("token.txt").getPath());
			BufferedReader redefBR = new BufferedReader(redefFR);
			BufferedReader tokenBR = new BufferedReader(tokenFR);
			List<String> redefList = new ArrayList<>();
			List<String> tokenList = new ArrayList<>();
			while (redefBR.ready()) redefList.add(redefBR.readLine());
			while (tokenBR.ready()) tokenList.add(tokenBR.readLine());
			redefFR.close();
			redefBR.close();
			tokenFR.close();
			tokenBR.close();
			
			LexicalAnalyzer lexicon = new LexicalAnalyzer();
			Map<String, String> redefMap = lexicon.loadRegularDefinition(redefList);
			for (String regex : redefMap.keySet()) System.out.println(redefMap.get(regex)+" : "+regex);
		} catch (IOException e) {
			System.err.println("Arquivo Não Encontrado");
			e.printStackTrace();
		}
		
		System.out.println("TERMINO TESTE 2");
	}
	
	@Test
	public void LexicalAnalyzerParserTest() {
		System.out.println("INICIO TESTE 3");
		try {
			FileReader redefFR = new FileReader(this.getClass().getClassLoader().getResource("redef.txt").getPath());
			FileReader tokenFR = new FileReader(this.getClass().getClassLoader().getResource("token.txt").getPath());
			BufferedReader redefBR = new BufferedReader(redefFR);
			BufferedReader tokenBR = new BufferedReader(tokenFR);
			List<String> redefList = new ArrayList<>();
			List<String> tokenList = new ArrayList<>();
			while (redefBR.ready()) redefList.add(redefBR.readLine());
			while (tokenBR.ready()) tokenList.add(tokenBR.readLine());
			redefFR.close();
			redefBR.close();
			tokenFR.close();
			tokenBR.close();
			
			LexicalAnalyzer lexicon = new LexicalAnalyzer();
			try {
				Automata automaton = lexicon.parser(tokenList, redefList);
				System.out.println("ESTADOS:");
				for (String state : automaton.getStates()) System.out.println(state);
				System.out.println("ESTADO INICIAL:");
				System.out.println(automaton.getInitialState());
				System.out.println("ESTADOS FINAIS:");
				for (String state : automaton.getFinalStates()) System.out.println(state);
				System.out.println("TRANSIÇÕES:");
				for (Transition transition : automaton.getTransitions()) {
					for (String targetState : transition.getTargetStates()) {
						System.out.println(transition.getSourceState() + ">>" +transition.getTransitionCharacter()+">>"+ targetState);
					}
				}
				
				System.out.println("----------------------------------------------------------------------------------------");
				System.out.println();
				System.out.println("STATES TO TOKEN");
			} catch (BracketMismatchException | OperatorMismatchException | InvalidInputException
					| DuplicatedStateException | InvalidStateException | DuplicatedTransitionException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("Arquivo Não Encontrado");
			e.printStackTrace();
		}
		
		System.out.println("TERMINO TESTE 3");
		
	}

}
