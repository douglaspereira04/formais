package model.parser.ll1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import exception.automata.InvalidStateException;

public class Parser {

	public static final String EPSILON = "&";
	private String grammar = null;
	private List<String> alphabet = null;
	private List<String> nonTerminalSymbols = null;
	private List<String> terminals = null;
	private Map<String, List<String>> followPos = null;
	private Map<String, List<String>> firstPos = null;

	/**
	 * Gets alphabet, non terminal symbols and terminal symbols from the given
	 * grammar
	 * 
	 * @param grammar
	 */
	public void setGrammar(String grammar) {
		this.grammar = grammar;

		String[] lines = grammar.split("\n");

		for (String line : lines) {
			String[] production = line.split("->");
			if (production.length != 2)
				continue;

			String head = production[0].trim();

			List<String> tail = Arrays.asList(production[1].trim().split(" "));
			tail.replaceAll(String::trim);

			addNotContains(head, this.alphabet);
			addNotContains(head, this.nonTerminalSymbols);

			for (String symbol : tail) {
				if (symbol.equals(EPSILON)) {
					addNotContains(symbol, alphabet);
				}
			}

			List<String> temp = new ArrayList<String>(this.alphabet);
			temp.removeAll(this.nonTerminalSymbols);
			this.terminals.addAll(temp);

		}

	}

	/**
	 * Gets first pos from grammar
	 * 
	 * @throws InvalidStateException
	 */
	public void setFirstPos() throws InvalidStateException {
		if (this.grammar == null)
			throw new InvalidStateException("#NO GRAMMAR");

		this.firstPos = new HashMap<String, List<String>>();

		boolean notDone;

		do {
			notDone = false;

			String[] lines = this.grammar.split("\n");

			for (String line : lines) {
				String[] production = line.split("->");
				if (production.length < 2)
					continue;

				String head = production[0].trim();

				List<String> tail = Arrays.asList(production[1].trim().split(" "));
				tail.replaceAll(String::trim);

				if (this.firstPos.get(head) == null) {
					this.firstPos.put(head, new ArrayList<String>());
				}

				if (tail.size() == 1 && tail.get(0).equals(EPSILON)) {
					notDone |= addNotContains(EPSILON, this.firstPos.get(head));
				} else {
					notDone |= collectFirsts4(tail, this.firstPos.get(head));
				}
				
			}

		} while (notDone);

	}
	
	private boolean collectFirsts4(List<String> tail, List<String> nonTerminalFirsts) {
		
		boolean result = false;
		
		boolean epsilonInSymbolFirsts = true;
		
		for (int i = 0; i < tail.size(); i++) {
			
			String symbol = tail.get(i);
			
			epsilonInSymbolFirsts = false;
			
			if(this.terminals.contains(symbol)) {
				result |= addNotContains(symbol, nonTerminalFirsts);
				break;
			}
			
			for (int j = 0; j < this.firstPos.get(symbol).size(); j++) {
				String first = this.firstPos.get(symbol).get(j);
				
				epsilonInSymbolFirsts |= first == EPSILON;
				
				result |= addNotContains(first, nonTerminalFirsts);
				
			}
			
			if (!epsilonInSymbolFirsts) {
				break;
			}
			
		}
		
		if(epsilonInSymbolFirsts) {
			result |= addNotContains(EPSILON, nonTerminalFirsts);
		}
		return result;
		
		
	}

	private boolean collectFirsts3(List<String> sequence) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Gets follow pos from grammar
	 * 
	 * @throws InvalidStateException When grmmar not set
	 */
	private void setFollowPos() throws InvalidStateException {
		if (this.grammar == null)
			throw new InvalidStateException("#NO GRAMMAR");

		this.followPos = new HashMap<String, List<String>>();

		boolean notDone;

		do {
			notDone = false;

			String[] lines = this.grammar.split("\n");

			boolean isFirstLine = true;
			for (String line : lines) {
				String[] production = line.split("->");
				if (production.length != 2)
					continue;

				String head = production[0].trim();

				List<String> tail = Arrays.asList(production[1].trim().split(" "));
				tail.replaceAll(String::trim);

				if (isFirstLine) {
					if (this.followPos.get(head) == null) {
						this.followPos.put(head, new ArrayList<String>());
					}

					notDone |= addNotContains("$", this.followPos.get(head));
				}

				for (int i = 0; i < tail.size(); i++) {
					String symbol = tail.get(i);

					if (this.nonTerminalSymbols.contains(symbol)) {

						if (this.followPos.get(symbol) == null) {
							this.followPos.put(symbol, new ArrayList<String>());
						}

						// TODO continuar follows

					}
				}

				isFirstLine = false;
			}

		} while (notDone);

	}

	private <T> boolean addNotContains(T item, List<T> list) {
		if (!list.contains(item)) {
			list.add(item);
			return true;
		}
		return false;
	}

	// trim all elements originalStrings.replaceAll(String::trim);
	// diferrence = list1.removeall(list2)

	public Parser() {

		// TODO Auto-generated constructor stub
	}

}
