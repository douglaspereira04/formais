package model.LRparser;

import java.util.ArrayList;
import java.util.List;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Production.NONTERMINAL;

public class LRparser {
	
	private Grammar grammar;
	
	public LRparser(Grammar grammar) {
		Production first = grammar.getProductions().get(0);
		Production new_first = new Production(NONTERMINAL.Sl, first.head().toString());
		grammar.setFirstProduction(new_first);
		this.grammar = grammar;
	}
	
	public Grammar getGrammar() {
		return grammar;
	}
	
	
	public List<Item> closure(List<Item> I) {
		
		//Stores the closure of a given state
		List<Item> closure = new ArrayList<>();
		
		//Iterates I to preserve parameter integrity
		List<Item> iterator = new ArrayList<>(I);
		
		//While there is an item to be added
		while (true) {
			
			//Set escape condition
			boolean LAST_ITEM = true;
			
			//Iterates each Item
			for (Item item : iterator) {
				
				//Get the next to this Item's production rule's marker (i.e the dot symbol)
				String symbol = item.next();
				
				//Check if said symbol is a Nonterminal
				if (grammar.getNonterminals().contains(symbol)) {
					//Iterates through every production whose head equals this item's next production rule's symbol
					for (Production production : grammar.getProductions()) {					
						if (production.head().toString().equals(symbol)) {
							//Calculates the Set (beta_alpha) comprised by all the symbols after 'symbol' + the lookahead
							List<String> beta_alpha = item.beta_alpha();
							
							//Calculates the firstPos set of the Sequence beta_alpha
							List<String> first = firstSeq(beta_alpha);
							
							//Concatenates the lookahead symbol from all symbols belonging to the beta_alpha sequence firstPos
							String lookahead = "";
							for (String b : first) {
								if (!lookahead.isEmpty())
									lookahead+="/";
								lookahead+=b;
							}
							
							//Generate a new Item using the current production, the newly formed lookahead and starting at index 0
							Item new_item = new Item(production, lookahead, 0);
							
							//Locally save the new Item in closure
							if (!iterator.contains(new_item)) {
								closure.add(new_item);
								//Since there is a new item, set last item flag to false
								LAST_ITEM = false;
							}
						}
					}
				}
			}
			//Appends all the new Items on iterator for the next iteration
			iterator.addAll(closure);
			
			//Reset this iteration's closure set to empty
			closure.clear();
			
			//Evaluate escape condition
			if (LAST_ITEM)
				break;
		}
		//Checks the product Item x Item for each closure item to find copies
		for (Item item1 : iterator) {
			String lookahead = "";
			for (Item item2 : iterator) {
				//Check if items are equal ignoring the lookahead value
				if (item1.equals_ignore_lookahead(item2)) {
					//Calculate the new lookahead value by concatenating both item's values
					if (lookahead.isEmpty()) {
						lookahead = item2.lookahead();
					} else {
						for (char c : item2.lookahead().toCharArray()) {
							if (!lookahead.contains((Character.toString(c)))) 
								lookahead += " " + item2.lookahead();
						}
					}
				}
			}
			//Check if a new lookahead was formed
			if (!lookahead.isEmpty()) {
				//Create a new Item with the new lookahead value
				Item new_item = new Item(item1.production(), lookahead, item1.index());
				boolean contains = false;
				for (Item it : closure) {
					if (it.equals_ignore_lookahead(new_item)) {
						contains = true;
					}
				}
				//Append the merged item to the closure items set
				if (!contains)
					closure.add(new_item);
			}
				
		}
		return closure;
	}
	
	public List<String> firstSeq(List<String> seq) {
		List<String> first = new ArrayList<>();
		for (String symbol : seq) {
			if (!grammar.getSymbols().contains(symbol)) {
				if (!first.contains(symbol))
					first.add(symbol);
			} else {
				List<String> temp = grammar.firstPos(symbol);
				for (String fi : temp) {
					if (!first.contains(fi))
						first.add(fi);
				}
				if (!temp.contains("&"))
					break;
			}
		}
		return first;
	}
	
	public List<Item> GOTO(List<Item> I, String X) {
		List<Item> J = new ArrayList<>();
		for (Item item : I) {
			String symbol = item.next();
			if (X.equals(symbol)) {
				Item new_item = new Item(item.production(), item.lookahead(), item.index()+1);
				if (!J.contains(new_item) && item.index() < item.production().rule().split(" ").length)
					J.add(new_item);
			}
		}
		return J;
	}
	
	public List<List<Item>> closureSet() {
		
		List<List<Item>> closureSet = new ArrayList<>();
		
		List<List<Item>> states = canonical();
		
		for (List<Item> state : states )
			closureSet.add(closure(state));
		
		return closureSet;
	}
	
	public List<List<Item>> canonical() {
		
		//Stores the LR(1) FDA States
		List<List<Item>> states = new ArrayList<>();
		
		//Extends the Grammar by adding S' -> S and generate the first Item Set
		Production extended = grammar.getProductions().get(0);
		Item initial = new Item(extended, "$", 0);
		List<Item> I0 = new ArrayList<>();
		I0.add(initial);
		
		//Saves the Initial State
		states.add(I0);
		
		//Temporary variable to store the new created states
		List<List<Item>> temp = new ArrayList<>();
		
		//While there is a state to be added
		while (true) {
			//Set escape condition
			boolean END_STATE = true;
			//Iterates each State in Iterator
			for (List<Item> state : states) {
				//Calculates the State's Closure
				List<Item> closure = closure(state);
				//Iterates each symbol in the grammar
				for (String symbol : grammar.getSymbols()) {
					//Calculates the next states through goto function
					List<Item> next = GOTO(closure, symbol);
					//Check if generated state already exists
					if (!next.isEmpty() && !states.contains(next)) {
						
						//Locally saves the state for next iteration
						temp.add(next);
						
						//Since there is a next state, set end state flag to false
						END_STATE = false;
					}
				}
			}
			//Update States by appending all temporary saved states.
			states.addAll(temp);
			//Delete all states in temporary state keeper for next iteration
			temp.clear();
			//Evaluates escape condition
			if (END_STATE)
				break;
		}
		
		
		return states;
	}

}
