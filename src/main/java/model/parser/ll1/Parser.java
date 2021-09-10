package model.parser.ll1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Parser {

	public static final char EPSILON = '&';
	private String grammar = null;
	private List<String> alphabet = null;
	private List<String> nonTerminalSymbols = null;
	private List<String> terminals = null;
	
	/**
	 * Gets alphabet, non terminal symbols and terminal symbols from the given grammar
	 * 
	 * @param grammar
	 */
	public void setSymbols(String grammar) {
		this.grammar = grammar;
		
		String[] lines = grammar.split("\n");
		
		for (String line : lines) {
			String[] production = line.split("->");
			if(production.length != 2)
				continue;

			String head = production[0].trim();
			
			List<String> tail = Arrays.asList(production[1].trim().split(" "));
			tail.replaceAll(String::trim);

			addNotContains(head, this.alphabet);
			addNotContains(head, this.nonTerminalSymbols);
			
			for (String symbol : tail) {
				if (symbol.equals(String.valueOf(EPSILON))) {
					addNotContains(symbol, alphabet);
				}
			}
			
			List<String> temp = new ArrayList<String>(this.alphabet);
			temp.removeAll(this.nonTerminalSymbols);
			this.terminals.addAll(temp);
			
		}
		
	}

	private void addNotContains(Object item, List list) {
		if (!list.contains(item))
			list.add(item);
	}

	// trim all elements originalStrings.replaceAll(String::trim);
	// diferrence = list1.removeall(list2)

	public Parser() {

		// TODO Auto-generated constructor stub
	}

}
