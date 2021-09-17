package model.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.grammar.Production.NONTERMINAL;

public class Grammar {
	ArrayList<String> symbols;
	ArrayList<Production> productions;
	
	public Grammar() {
		productions = new ArrayList<>();
		symbols = new ArrayList<>();
	}
	
	public Grammar(String grammar) {
		productions = new ArrayList<>();
		symbols = new ArrayList<>();
		
		String[] lines = grammar.split("\n");
		for (String line : lines) {
			String[] production  = line.split("->");
			String stringHead = production[0].trim();
			
			if(stringHead.length() >1) {
				if(stringHead.charAt(1) == '\'') {
					stringHead =stringHead.charAt(0)+"l";
				}
			}
			
			NONTERMINAL head = NONTERMINAL.valueOf(stringHead);
			this.addProduction(new Production(head, production[1].trim()));
		}
		
	}
	
	public Grammar(ArrayList<Production> productions) {
		this.productions = productions;
	}
	
	public void addProduction(Production production) {
		productions.add(production);
	}
	
	public ArrayList<Production> getProductions() {
		return this.productions;
	}
	
	public void setFirstProduction(Production production) {
		ArrayList<Production> updated = new ArrayList<>();
		updated.add(production);
		updated.addAll(productions);
		productions = updated;
	}
	
	public List<String> getRules(String head) {
		List<String> rules = new ArrayList<>();
		for (Production production : productions) {
			if (production.head().toString().equals(head) && !rules.contains(production.rule()))
				rules.add(production.rule());
		}
		return rules;
	}
	
	public List<String> getNonterminals() {
		List<String> nontermSymbols = new ArrayList<>();
		for (Production production : productions)
			if (!nontermSymbols.contains(production.head().toString()))
				nontermSymbols.add(production.head().toString());
		return nontermSymbols;
	}
	
	public List<String> getTerminals() {
		List<String> terminalSymbols = new ArrayList<>();
		for (Production production : productions) {
			String rule = production.rule();
			String[] symbols = rule.split(" ");
			for (String symbol : symbols) {
				boolean IS_TERMINAL = true;
				for (NONTERMINAL nonterminal : NONTERMINAL.values()) {
					if (symbol.equals(nonterminal.toString()))
						IS_TERMINAL = false;
				}
				if (IS_TERMINAL && !terminalSymbols.contains(symbol))
					terminalSymbols.add(symbol);
			}
		}
		return terminalSymbols;
	}
	
	public List<String> getSymbols() {
		List<String> symbols = new ArrayList<>(getTerminals());
		symbols.addAll(getNonterminals());
		return symbols;
	}
	
	public List<String> firstPos(String symbol) {
		
		List<String> firstPos = new ArrayList<>();
		
		//Check symbol Membership in the Grammar
		if (!getSymbols().contains(symbol))
			System.err.println("Símbolo de entrada não pertencente a Gramática");
		
		//Append to First if terminal
		if (getTerminals().contains(symbol))
			firstPos.add(symbol);
		//If symbol is a nonterminal
		else {
			
			for (String rule : getRules(symbol)) {
				String[] rule_ = rule.split(" ");
				String start = rule_[0];
				
				if (start.equals(symbol))
					continue;
			
				//Case 1: Production starts with terminal symbol
				if (getTerminals().contains(start) && !firstPos.contains(start))
					firstPos.add(start);
				
				//Case 2: Production starts with nonterminal symbol
			    if (getNonterminals().contains(start)) {
					List<String> first = firstPos(start);
					for (String element : first) {
						if (!firstPos.contains(element) && !element.equals("&"))
							firstPos.add(element);
					}
					int len = 1;
					while (first.contains("&") && rule_.length > len) {
						start = rule_[len];
						first = firstPos(start);
						for (String element : first) {
							if (!firstPos.contains(element) && !element.equals("&"))
								firstPos.add(element);
						}
						if (len == rule_.length-1 && firstPos(rule_[len]).contains("&"))
							firstPos.add("&");
						len++;
					}
				}
				
			}
		}
		
		
		return firstPos;
	}
	
	public List<String> followPos(String symbol) {
		
		if (!getNonterminals().contains(symbol))
			System.err.println("Símbolo de Entrada não é um não-terminal");
		
		List<String> followPos = new ArrayList<>();
		
		if (productions.get(0).toString().equals(symbol))
			followPos.add("$");
		
		for (Production production : productions) {
			if (production.head().toString().equals(symbol)) {
				String[] rule = production.rule().split(" "); 
				for (int i = 0; i < rule.length; i++) {
					if (rule[i].equals(symbol)) {
						
					}
				}
			}
		}
		
		return followPos;
	}
	
	public Map<String, List<String>> followPosIntern() {
		
		Map<String, List<String>> followPosMap = new HashMap<>();
		for (String nonterminal : getNonterminals())
			followPosMap.put(nonterminal, new ArrayList<>());
		
		for (String nonterminal : getNonterminals()) {
			List<String> followPos = followPosMap.get(nonterminal);
			if (productions.get(0).head().toString().equals(nonterminal))
				followPos.add("$");
			
			for (Production production : productions) {
					String[] rule = production.rule().split(" ");
					for (int i = 0; i < rule.length; i++) {
						if (rule[i].equals(nonterminal)) {
							for (int j = i+1; j < rule.length; j++) {
								for (String first : firstPos(rule[j])) {
									if (!followPos.contains(first) && !first.equals("&")) {
										followPos.add(first);
									}
								}
								if (!firstPos(rule[j]).contains("&"))
									break;
							}
						}
					}
			}
		}
		
		for (Production production : productions) {
			String rule[] = production.rule().split(" ");
			int index = 1;
			while (getNonterminals().contains(rule[rule.length-index]) || rule[rule.length-index].equals("&")) {
				if (rule[rule.length-index].equals("&")) {
					index++;
					break;
				}
				List<String> followPosHead = followPosMap.get(production.head().toString());
				List<String> followPosBody = followPosMap.get(rule[rule.length-index]);
				for (String value : followPosHead) {
					if (!followPosBody.contains(value))
						followPosBody.add(value);
				}
				
				if (!firstPos(rule[rule.length-index]).contains("&"))
					break;
				index++;
			}
				
		}
		
		return followPosMap;
	}

}
