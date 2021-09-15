package model.parser.ll1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.MultiKeyMap;

import exception.automata.InvalidStateException;
import hu.webarticum.treeprinter.SimpleTreeNode;

/**
 * LL1 Parser class
 * 
 * @author douglas
 *
 */
public class LL1Parser {

	public static final String EPSILON = "&";
	private String grammar = null;
	private List<String> alphabet = null;
	private List<String> nonTerminalSymbols = null;
	private List<String> terminalSymbols = null;
	private Map<String, List<String>> followPos = null;
	private Map<String, List<String>> firstPos = null;
	private MultiKeyMap<String, String> parsingTable = null;

	/**
	 * Gets alphabet, non terminal symbols and terminal symbols from the given
	 * grammar
	 * 
	 * @param grammar
	 * @throws InvalidStateException when grammar hasnt been set
	 */
	public void loadGrammar() throws InvalidStateException {
		if (this.grammar == null)
			throw new InvalidStateException("#NO GRAMMAR");

		this.alphabet = new ArrayList<String>();
		this.nonTerminalSymbols = new ArrayList<String>();

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
				if (!symbol.equals(EPSILON)) {
					addNotContains(symbol, alphabet);
				}
			}

			this.terminalSymbols = new ArrayList<String>(this.alphabet);
			this.terminalSymbols.removeAll(this.nonTerminalSymbols);

		}

	}

	/**
	 * Gets first pos from grammar
	 * 
	 * @throws InvalidStateException
	 */
	public void loadFirstPos() throws InvalidStateException {
		if (this.grammar == null)
			throw new InvalidStateException("#NO GRAMMAR");
		if (this.alphabet == null || this.terminalSymbols == null || this.nonTerminalSymbols == null)
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
					notDone |= collectFirsts(tail, this.firstPos.get(head));
				}

			}

		} while (notDone);

	}

	private boolean collectFirsts(List<String> tail, List<String> nonTerminalFirsts) {

		boolean result = false;

		boolean epsilonInSymbolFirsts = true;

		for (int i = 0; i < tail.size(); i++) {

			String symbol = tail.get(i);

			epsilonInSymbolFirsts = false;

			if (this.terminalSymbols.contains(symbol)) {
				result |= addNotContains(symbol, nonTerminalFirsts);
				break;
			}

			if (this.firstPos.get(symbol) == null)
				this.firstPos.put(symbol, new ArrayList<String>());

			for (int j = 0; j < this.firstPos.get(symbol).size(); j++) {
				String first = this.firstPos.get(symbol).get(j);

				epsilonInSymbolFirsts |= first == EPSILON;

				result |= addNotContains(first, nonTerminalFirsts);

			}

			if (!epsilonInSymbolFirsts) {
				break;
			}

		}

		if (epsilonInSymbolFirsts) {
			result |= addNotContains(EPSILON, nonTerminalFirsts);
		}
		return result;

	}

	private List<String> collectFirsts(List<String> sequence) {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		boolean epsilonInSymbolFirsts = true;

		for (int i = 0; i < sequence.size(); i++) {

			String symbol = sequence.get(i);

			epsilonInSymbolFirsts = false;

			if (this.terminalSymbols.contains(symbol)) {
				addNotContains(symbol, result);
				break;
			}

			if (this.firstPos.get(symbol) == null)
				this.firstPos.put(symbol, new ArrayList<String>());

			for (int j = 0; j < this.firstPos.get(symbol).size(); j++) {
				String first = firstPos.get(symbol).get(j);

				epsilonInSymbolFirsts |= first == EPSILON;

				addNotContains(first, result);

			}

			epsilonInSymbolFirsts |= firstPos.get(symbol).isEmpty();

			if (!epsilonInSymbolFirsts) {
				break;
			}

		}

		if (epsilonInSymbolFirsts) {
			addNotContains(EPSILON, result);
		}

		return result;

	}

	/**
	 * Gets follow pos from grammar
	 * 
	 * @throws InvalidStateException When grmmar not set
	 */
	public void loadFollowPos() throws InvalidStateException {
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
				if (production.length < 2)
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

						List<String> afterSymbolFirsts = collectFirsts(tail.subList(i + 1, tail.size()));

						for (String first : afterSymbolFirsts) {
							if (first == EPSILON) {
								for (String headFollow : this.followPos.get(head)) {
									notDone |= addNotContains(headFollow, this.followPos.get(symbol));
								}
							} else {
								notDone |= addNotContains(first, this.followPos.get(symbol));
							}
						}

					}
				}

				isFirstLine = false;
			}

		} while (notDone);

	}

	/**
	 * Loads parsing table from grammar set to parser
	 * 
	 * @throws InvalidStateException if grammar, first or follow pos havent been set
	 */
	public void loadParsingTable() throws InvalidStateException {
		parsingTable = new MultiKeyMap<>();

		if (this.grammar == null)
			throw new InvalidStateException("#NO GRAMMAR");
		if (this.alphabet == null || this.terminalSymbols == null || this.nonTerminalSymbols == null)
			throw new InvalidStateException("#NO GRAMMAR");
		if (this.firstPos == null)
			throw new InvalidStateException("#NO FIRSTPOS");
		if (this.followPos == null)
			throw new InvalidStateException("#NO FOLLOWPOS");
		String[] lines = grammar.split("\n");

		for (String line : lines) {
			String[] production = line.split("->");
			if (production.length < 2)
				continue;
			String head = production[0].trim();

			List<String> tail = Arrays.asList(production[1].trim().split(" "));
			tail.replaceAll(String::trim);

			List<String> tailFirsts = collectFirsts(tail);

			for (String symbol : tailFirsts) {
				if (symbol != EPSILON) {
					String rule = parsingTable.get(head, symbol);
					if (rule == null) {
						parsingTable.put(head, symbol, line.trim());
					} else {
						parsingTable.put(head, symbol, rule + " " + line.trim());
					}

				} else {
					for (String symbol2 : this.followPos.get(head)) {
						String rule = parsingTable.get(head, symbol2);
						if (rule == null) {
							parsingTable.put(head, symbol2, line.trim());
						} else {
							parsingTable.put(head, symbol2, rule + " " + line.trim());
						}
					}
				}
			}

		}
	}

	/**
	 * Parses a token
	 * 
	 * @param token    to be validated
	 * @param maxSteps maximum iteration count
	 * @return {@link LL1ParsingResult} containing the result
	 */
	public LL1ParsingResult parse(String token, int maxSteps) {
		LL1ParsingResult result = new LL1ParsingResult();
		String[] input = token.split(" ");
		SimpleTreeNode tree = new SimpleTreeNode("ROOT");

		List<String> stack = new ArrayList<String>();
		stack.add("$");
		stack.add(this.nonTerminalSymbols.get(0));

		List<SimpleTreeNode> parents = new ArrayList<SimpleTreeNode>();
		parents.add(tree);

		for (int i = 0, index = 0; i < maxSteps && 1 < stack.size(); ++i) {

			String top = stack.get(stack.size() - 1);
			String symbol = index < input.length ? input[index] : "$";

			if (symbol.trim().equals(""))
				symbol = "$";

			String rule = null;
			if (top.equals(symbol)) {
				stack.remove(stack.size() - 1);
				++index;
				(parents.remove(parents.size() - 1)).addChild(new SimpleTreeNode(symbol));

			} else {

				if (this.nonTerminalSymbols.contains(top)) {

					rule = this.parsingTable.get(top, symbol);
					SimpleTreeNode node = new SimpleTreeNode(top);
					(parents.remove(parents.size() - 1)).addChild(node);

					if (rule == null) {
						break;
					}

					stack.remove(stack.size() - 1);

					List<String> tail = Arrays.asList(rule.split("->")[1].trim().split(" "));
					tail.replaceAll(String::trim);
					Collections.reverse(tail);

					for (int j = 0; j < tail.size(); j++) {
						parents.add(node);
					}

					if (!tail.contains(EPSILON)) {
						stack.addAll(tail);
					} else {
						(parents.remove(parents.size() - 1)).addChild(new SimpleTreeNode(EPSILON));
					}
				} else {
					break;
				}
			}
			result.addEntry(String.join(" ", stack), String.join(" ", Arrays.copyOfRange(input, index, input.length)),
					rule);
		}

		result.setTree(tree);
		try {
			result.setAccepted(result.getStack().get(result.getStack().size() - 1).equals("$")
					&& result.getInput().get(result.getInput().size() - 1).trim().equals(""));
		} catch (Exception e) {
			result.setAccepted(false);
		}

		return result;
	}

	private <T> boolean addNotContains(T item, List<T> list) {
		if (!list.contains(item)) {
			list.add(item);
			return true;
		}
		return false;
	}

	public LL1Parser() {

		// TODO Auto-generated constructor stub
	}

	public List<String> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(List<String> alphabet) {
		this.alphabet = alphabet;
	}

	public List<String> getNonTerminalSymbols() {
		return nonTerminalSymbols;
	}

	public void setNonTerminalSymbols(List<String> nonTerminalSymbols) {
		this.nonTerminalSymbols = nonTerminalSymbols;
	}

	public Map<String, List<String>> getFollowPos() {
		return followPos;
	}

	public void setFollowPos(Map<String, List<String>> followPos) {
		this.followPos = followPos;
	}

	public Map<String, List<String>> getFirstPos() {
		return firstPos;
	}

	public void setFirstPos(Map<String, List<String>> firstPos) {
		this.firstPos = firstPos;
	}

	public void setGrammar(String grammar) {
		this.grammar = grammar;
	}

	public String getGrammar() {
		return grammar;
	}

	public List<String> getTerminalSymbols() {
		return terminalSymbols;
	}

	public void setTerminalSymbols(List<String> terminalSymbols) {
		this.terminalSymbols = terminalSymbols;
	}

	public MultiKeyMap<String, String> getParsingTable() {
		return parsingTable;
	}

	public void setParsingTable(MultiKeyMap<String, String> parsingTable) {
		this.parsingTable = parsingTable;
	}

}
