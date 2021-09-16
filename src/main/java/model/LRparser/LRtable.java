package model.LRparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import model.grammar.Grammar;
import model.grammar.Production;

public class LRtable {
	
	private LRparser parser;
	private Grammar grammar;
	
	private List<List<Item>> states;
	private List<List<Item>> closures;
	
	Map<Integer, Map<String, Integer>> gotoTable;
	Map<Integer, Map<String, String>> actionTable;
	
	private boolean IS_LR1;
	
	public LRtable (Grammar grammar) {
		parser = new LRparser(grammar);
		this.grammar = parser.getGrammar();
		
		states = parser.canonical();
		closures = parser.closureSet();
		
		gotoTable = new HashMap<>();
		actionTable = new HashMap<>();
		
		gotoTable();
		actionTable();
		
		IS_LR1 = true;
	}
	
	public boolean IS_LR1() {
		return IS_LR1;
	}
	
	public List<List<Item>> getStates() {
		return states;
	}
	
	public List<List<Item>> closures() {
		return closures;
	}
	
	public Map<Integer, Map<String, Integer>> getGotoTable() {
		return gotoTable;
	}
	
	public Map<Integer, Map<String, String>> getActionTable() {
		return actionTable;
	}
	
	public LRparser getParser() {
		return parser;
	}
	
	public Grammar getGrammar() {
		return grammar;
	}
	
	public int action(int state, String symbol) {
		
		int action_code = Integer.MAX_VALUE;
		
		if (!actionTable.containsKey(state))
			return action_code;
		
		if (actionTable.containsKey(state) && !actionTable.get(state).containsKey(symbol))
			return action_code;
			
		char type = actionTable.get(state).get(symbol).charAt(0);
		
		if (type == 'a')
			return 0;
		
		if (type == 's')
			return actionState(state, symbol);
		
		
		if (type == 'r')
			return -1*actionState(state, symbol);
		
	return action_code;
	}
	
	public int actionState(int state, String symbol) {
		
		if (!actionTable.containsKey(state))
			return -1;
		
		if (actionTable.containsKey(state) && !actionTable.get(state).containsKey(symbol))
			return -1;
		
		char[] action = actionTable.get(state).get(symbol).toCharArray();
		
		String action_state = "";
		for (int i = 1; i < action.length; i++)
			action_state += action[i];
		
		return Integer.parseInt(action_state);
	}
	
	public void gotoTable() {
		
		//Iterates each State
		for (List<Item> state : states) {
			
			//Get current state index
			int state_index = states.indexOf(state);
			
			//Create a Nonterminal -> GOTO Index Map
			Map<String, Integer> map = new HashMap<>();
			
			//For each State x Nonterminal Symbol combination
			for (String symbol : grammar.getSymbols()) {
				
				//Find next state using GOTO function
				List<Item> next = parser.GOTO(closures.get(state_index), symbol);
				
				//Get next state index
				int next_index = states.indexOf(next);
				
				//Check if any rules contradicts
				if (map.containsKey(symbol))
					IS_LR1 = false;
				
				if (next_index > -1 && states.contains(next))
					map.put(symbol, next_index);
			}
			
			//Check if any rules contradicts
			if (gotoTable.containsKey(state_index))
				IS_LR1 = false;
			
			//Map Current State Index -> Nonterminal x Goto State Index Map
			gotoTable.put(state_index, map);
		}
	}
	
	public void actionTable() {
		
		//Iterates each State
		for (List<Item> state : states) {
			
			//Get current state index
			int state_index = states.indexOf(state);
			
			//Create a Lookahead -> Action Map
			Map<String, String> map = new HashMap<>();
			
			//Iterates each Item in State's closure
			for (Item item : closures.get(state_index)) {
				
				//Get the next symbol after dot
				String symbol = item.next();
				
				//Checks if item = I0
				if (item.production().equals(grammar.getProductions().get(0))) {
					
					if (symbol == null) {
						//Insert Accept
						map.put("$", "acc");
					}
					
				} else {
					//If rule ends in dot
					if (symbol == null) {
						//Get the current item index
						int production_index = grammar.getProductions().indexOf(item.production());
						
						if (production_index < 0)
							System.err.println("Item não pertence ao Estado");
						
						String[] lookahead = item.lookahead().split(" ");
						
						//Iterates each lookahead character
						for (int i = 0; i < lookahead.length; i++) {
							
							//Check if any rule contradicts
							if (map.containsKey(lookahead[i]))
								IS_LR1 = false;
							
							if (production_index >= 0)
								map.put(lookahead[i], "r"+Integer.toString(production_index));
						}
					} 
					
				}
				//Case [A -> ALPHA.aBETA,b]
				 if (symbol != null && grammar.getTerminals().contains(symbol)) {
					
					int goto_state_index = -1;
					if (gotoTable.containsKey(state_index) && gotoTable.get(state_index).containsKey(symbol))
						goto_state_index = states.indexOf(states.get(gotoTable.get(state_index).get(symbol)));
					
					//Check if any rule contradicts
					if (map.containsKey(symbol))
						IS_LR1 = false;
					
					if (goto_state_index >= 0)
						map.put(symbol, "s"+Integer.toString(goto_state_index));
				}
			}
			
			//Check if any rule contradicts
			if (actionTable.containsKey(state_index))
				IS_LR1 = false;
			
			actionTable.put(state_index, map);
		}
		
		
	}
	
	public boolean compute(List<String> word) {
		
		word.add("$");
		
		Stack<Integer> stack = new Stack<>();
		stack.push(0);
		
		int index = 0;
		
		String symbol = word.get(index++);
		
		int state = 0;
		
		while (true) {
			
			state = stack.peek();
			
			int action = action(state, symbol);
			
			if (action == Integer.MAX_VALUE) {
				System.err.println("Syntax Error");
				return false;
			}
			
			if (action > 0) {
				stack.push(action);
				symbol = word.get(index++);
			}
			else if(action < 0) {
				action *= -1;
				Production production = grammar.getProductions().get(action);
				int size = production.rule().split(" ").length;
				for (int i = 0; i < size; i++) {
					stack.pop();
				}
				int t = stack.peek();
				String head = production.head().toString();
				if (gotoTable.containsKey(t) && gotoTable.get(t).containsKey(head)) {
					stack.push(gotoTable.get(t).get(head));
					System.out.println(production.toString());
				}
			} else {
				System.out.println("Accept");
				return true;
			}
			
			
		}
		
	}

}
