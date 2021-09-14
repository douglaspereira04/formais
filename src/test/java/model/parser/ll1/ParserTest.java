package model.parser.ll1;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import exception.automata.InvalidStateException;

public class ParserTest {

	private static final String TESTGRAMMAR = "E -> T E'\n" + "E' -> + T E'\n" + "E' -> &\n" + "T -> F T'\n"
			+ "T' -> * F T'\n" + "T' -> &\n" + "F -> ( E )\n" + "F -> id";
	private static final String stack = "$ E' T, $ E' T' F, $ E' T' id, $ E' T', $ E', $ E' T +, $ E' T, $ E' T' F, $ E' T' id, $ E' T', $ E', $";
	private static final String input = "id + id, id + id, id + id, + id, + id, + id, id, id, id,  ,  ,  ";
	private static final String rule = "E -> T E', T -> F T', F -> id, null, T' -> &, E' -> + T E', null, T -> F T', F -> id, null, T' -> &, E' -> &";
	
	private static final String parsingTable = "[{MultiKey[T', +]=T' -> &, MultiKey[E', )]=E' -> &, MultiKey[E', $]=E' -> &, MultiKey[T', $]=T' -> &, MultiKey[E, id]=E -> T E', MultiKey[T', *]=T' -> * F T', MultiKey[E, (]=E -> T E', MultiKey[T, (]=T -> F T', MultiKey[T, id]=T -> F T', MultiKey[F, id]=F -> id, MultiKey[E', +]=E' -> + T E', MultiKey[T', )]=T' -> &, MultiKey[F, (]=F -> ( E )}]";
	private static final String alphabet = "E, T, E', +, F, T', *, (, ), id";
	private static final String terminal = "+, *, (, ), id";
	private static final String nonTerminal = "E, E', T, T', F";
	private static final String firstPos = "[{E'=[+, &], T'=[*, &], T=[(, id], E=[(, id], F=[(, id]}]";
	private static final String followPos = "[{E'=[$, )], T'=[+, $, )], T=[+, $, )], E=[$, )], F=[*, +, $, )]}]";

	private static final String splitString = ", "; 
	private static final List<String> stackList = Arrays.asList(stack.split(splitString));
	private static final List<String> inputList = Arrays.asList(input.split(splitString));
	private static final List<String> ruleList = Arrays.asList(rule.split(splitString));
	private static final List<String> alphabetList = Arrays.asList(alphabet.split(splitString));
	private static final List<String> terminalList = Arrays.asList(terminal.split(splitString));
	private static final List<String> nonTerminalList = Arrays.asList(nonTerminal.split(splitString));
	

	static {
		stackList.replaceAll(String::trim);
		inputList.replaceAll(String::trim);
		ruleList.replaceAll(String::trim);
		alphabetList.replaceAll(String::trim);
		terminalList.replaceAll(String::trim);
		nonTerminalList.replaceAll(String::trim);
	
	}
	
	@Test
	public void firstPosTest() throws InvalidStateException {

		LL1Parser ll1 = new LL1Parser();
		ll1.setGrammar(TESTGRAMMAR);
		ll1.loadGrammar();
		ll1.loadFirstPos();

		assert(Collections.singletonList(ll1.getFirstPos()).toString().equals(firstPos));

	}

	@Test
	public void followPosTest() throws InvalidStateException {

		LL1Parser ll1 = new LL1Parser();
		ll1.setGrammar(TESTGRAMMAR);
		ll1.loadGrammar();
		ll1.loadFirstPos();
		ll1.loadFollowPos();

		assert(Collections.singletonList(ll1.getFollowPos()).toString().equals(followPos));

	}

	@Test
	public void grammarTest() throws InvalidStateException {

		LL1Parser ll1 = new LL1Parser();
		ll1.setGrammar(TESTGRAMMAR);
		ll1.loadGrammar();


		assert(ll1.getAlphabet().equals(alphabetList));
		assert(ll1.getTerminalSymbols().equals(terminalList));
		assert(ll1.getNonTerminalSymbols().equals(nonTerminalList));
	}

	@Test
	public void parsingTableTest() throws InvalidStateException {

		LL1Parser ll1 = new LL1Parser();
		ll1.setGrammar(TESTGRAMMAR);
		ll1.loadGrammar();
		ll1.loadFirstPos();
		ll1.loadFollowPos();
		ll1.loadParsingTable();

		assert(Collections.singletonList(ll1.getParsingTable()).toString().equals(parsingTable.trim()));
	}

	@Test
	public void parseTest() throws InvalidStateException {

		LL1Parser ll1 = new LL1Parser();
		ll1.setGrammar(TESTGRAMMAR);
		ll1.loadGrammar();
		ll1.loadFirstPos();
		ll1.loadFollowPos();
		ll1.loadParsingTable();

		LL1ParsingResult result = ll1.parse("id + id", 100);
			
		
		assert(stackList.equals(result.getStack()));
		assert(inputList.equals(result.getInput()));

		for (int i = 0; i < result.getRule().size(); i++) {
			String resultRule = result.getRule().get(i);
			if(resultRule == null)
				resultRule = "null";
			
			assert(ruleList.get(i).equals(resultRule));
			
		}

	}
}
