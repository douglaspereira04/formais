package model.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import model.LRparser.Item;
import model.LRparser.LRparser;
import model.LRparser.LRtable;
import model.grammar.Production.NONTERMINAL;
@Ignore
public class GrammarTest {
	
	public String printItemSet(List<Item> itemSet) {
		String output = "";
		for (Item item : itemSet) {
			if (output.isEmpty()) {
				output = "{"+item.toString();
			} else {
				output += "; "+item.toString();
			}
		}
		output+= "}";
		return output;
	}

	@Test
	public void test1() {
		
		System.out.println("Test 1");
		
		Grammar grammar = new Grammar();
		
		grammar.addProduction(new Production(NONTERMINAL.S, "A B C"));
		grammar.addProduction(new Production(NONTERMINAL.A, "a A"));
		grammar.addProduction(new Production(NONTERMINAL.A, "&"));
		grammar.addProduction(new Production(NONTERMINAL.B, "b B"));
		grammar.addProduction(new Production(NONTERMINAL.B, "A C d"));
		grammar.addProduction(new Production(NONTERMINAL.C, "c C"));
		grammar.addProduction(new Production(NONTERMINAL.C, "&"));
		
		for (Production production : grammar.getProductions())
			System.out.println(production.head()+" -> "+production.rule());
		System.out.println();
		
		List<String> symbols = grammar.getSymbols();
		List<String> terminals = grammar.getTerminals();
		List<String> nonterminals = grammar.getNonterminals();
		System.out.println("Symbols: "+symbols);
		System.out.println("Terminals: "+terminals);
		System.out.println("Nonterminals: "+nonterminals);
		
		
		List<String> firstS = grammar.firstPos("S");
		List<String> firstA = grammar.firstPos("A");
		List<String> firstB = grammar.firstPos("B");
		List<String> firstC = grammar.firstPos("C");
		System.out.println("FirstPos");
		System.out.println("S = "+firstS);
		System.out.println("A = "+firstA);
		System.out.println("B = "+firstB);
		System.out.println("C = "+firstC);
		
		Map<String, List<String>> followPos = grammar.followPosIntern();
		
		System.out.println("FollowPos");
		
		for (String nonterminal : grammar.getNonterminals()) {
			System.out.println(nonterminal+" = "+followPos.get(nonterminal));
		}
		
		System.out.println();

	}
	
	@Test
	public void test2() {

		System.out.println("Test 2");
		
		Grammar grammar = new Grammar();
		
		grammar.addProduction(new Production(NONTERMINAL.S, "A b"));
		grammar.addProduction(new Production(NONTERMINAL.S, "A B c"));
		grammar.addProduction(new Production(NONTERMINAL.B, "b B"));
		grammar.addProduction(new Production(NONTERMINAL.B, "A d"));
		grammar.addProduction(new Production(NONTERMINAL.B, "&"));
		grammar.addProduction(new Production(NONTERMINAL.A, "a A"));
		grammar.addProduction(new Production(NONTERMINAL.A, "&"));
		
		for (Production production : grammar.getProductions())
			System.out.println(production.head()+" -> "+production.rule());
		System.out.println();
		
		List<String> symbols = grammar.getSymbols();
		List<String> terminals = grammar.getTerminals();
		List<String> nonterminals = grammar.getNonterminals();
		System.out.println("Symbols: "+symbols);
		System.out.println("Terminals: "+terminals);
		System.out.println("Nonterminals: "+nonterminals);
		
		List<String> firstS = grammar.firstPos("S");
		List<String> firstA = grammar.firstPos("A");
		List<String> firstB = grammar.firstPos("B");
		System.out.println("Firspos");
		System.out.println("S = "+firstS);
		System.out.println("A = "+firstA);
		System.out.println("B = "+firstB);
		Map<String, List<String>> followPos = grammar.followPosIntern();
		System.out.println("FollowPos");
		
		for (String nonterminal : grammar.getNonterminals()) {
			System.out.println(nonterminal+" = "+followPos.get(nonterminal));
		}
		
		System.out.println();
		
	}
	
	@Test
	public void test3() {
		
		System.out.println("Test 3");
		
		Grammar grammar = new Grammar();
		
		//grammar.addProduction(new Production(NONTERMINAL.Sl, "E"));
		grammar.addProduction(new Production(NONTERMINAL.E, "E + T"));
		grammar.addProduction(new Production(NONTERMINAL.E, "T"));
		grammar.addProduction(new Production(NONTERMINAL.T, "T * F"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		for (Production production : grammar.getProductions())
			System.out.println(production.head()+" -> "+production.rule());
		System.out.println();
		
		List<String> symbols = grammar.getSymbols();
		List<String> terminals = grammar.getTerminals();
		List<String> nonterminals = grammar.getNonterminals();
		System.out.println("Symbols: "+symbols);
		System.out.println("Terminals: "+terminals);
		System.out.println("Nonterminals: "+nonterminals);
		
		
		
		for (String nonterminal : grammar.getNonterminals()) {
			List<String> firstPos = grammar.firstPos(nonterminal);
			System.out.println("FirstPos "+nonterminal+": "+firstPos);
		}
		System.out.println();
		Map<String, List<String>> followPosMap = grammar.followPosIntern();
		for (String nonterminal : grammar.getNonterminals()) {
			List<String> followPos = followPosMap.get(nonterminal);
			System.out.println("followPos "+nonterminal+": "+followPos);
		}
		System.out.println();
	
		LRparser lr1 = new LRparser(grammar);
		List<Item> I0 = new ArrayList<>();
		I0.add(new Item(grammar.getProductions().get(0), "$", 0));
		/*
		List<Item> I0_GOTO = lr1.GOTO(I0, "E");
		System.out.println("GOTO: "+printItemSet(I0));
		System.out.println(printItemSet(I0_GOTO));
		*/
	
		I0 = lr1.closure(I0);
		System.out.println("Closure: "+printItemSet(I0));
		for (Item item : I0) {
			item.print();
		}
		System.out.println();
		
		
		Item item = new Item(new Production(NONTERMINAL.F, "( E )"), ")/+/*", 1);
		System.out.println("ITEM");
		item.print();
		System.out.println();
		List<Item> itemSet = new ArrayList<>();
		itemSet.add(item);
		
		System.out.println();
		//System.out.println("CLOSURE: "+printItemSet(lr1.closure(itemSet)));
		//System.out.println("CLOSURE: "+printItemSet(itemSet));
		
	}
	
	@Test
	public void test4() {
		System.out.println("Test 4");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "E + T"));
		grammar.addProduction(new Production(NONTERMINAL.E, "T"));
		grammar.addProduction(new Production(NONTERMINAL.T, "T * F"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		LRparser lr1 = new LRparser(grammar);
		System.out.println("Closure Table: ");
		List<List<Item>> states = lr1.closureSet();
		for (List<Item> state : states)
			System.out.println(printItemSet(state));
		
		System.out.println();
	}
	
	@Test
	public void test5() {
		System.out.println("Test 5");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "E + T"));
		grammar.addProduction(new Production(NONTERMINAL.E, "T"));
		grammar.addProduction(new Production(NONTERMINAL.T, "T * F"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		LRparser lr1 = new LRparser(grammar);
		System.out.println("STATES: ");
		List<List<Item>> states = lr1.canonical();
		for (List<Item> state : states)
			System.out.println(printItemSet(state));
		
		System.out.println();
	}
	
	@Test
	public void test6() {
		
		System.out.println("Test 6");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "E + T"));
		grammar.addProduction(new Production(NONTERMINAL.E, "T"));
		grammar.addProduction(new Production(NONTERMINAL.T, "T * F"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		LRtable lrtable = new LRtable(grammar);
		Map<Integer, Map<String, Integer>> gotoMap = lrtable.getGotoTable();
		
		int size = lrtable.getStates().size();
		
		for (int i = 0; i < size; i++) {
			
			System.out.println("STATE: "+i+" : "+printItemSet(lrtable.getStates().get(i)));
		}
		System.out.println();
		for (int i = 0; i < size; i++) {
			
			System.out.println("CLOSURE: "+i+" : "+printItemSet(lrtable.closures().get(i)));
		}
		
		System.out.println();
		
		System.out.println("GOTO TABLE");
		
		for (int i = 0; i < size; i++) {
			if (gotoMap.containsKey(i)) {
				for (String nonterminal : grammar.getNonterminals()) {
					if (gotoMap.get(i).containsKey(nonterminal)) {
						int gotoIndex = gotoMap.get(i).get(nonterminal);
						System.out.println(i+" x "+nonterminal+" -> "+gotoIndex);
					}
				}
			}
		}
		System.out.println();
	}
	
	@Test
	public void test7() {
		
		System.out.println("Test 7");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "E + T"));
		grammar.addProduction(new Production(NONTERMINAL.E, "T"));
		grammar.addProduction(new Production(NONTERMINAL.T, "T * F"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		LRtable lrtable = new LRtable(grammar);

		Map<Integer, Map<String, String>> actionTable = lrtable.getActionTable();
		
		int size = lrtable.getStates().size();
		
		System.out.println("Action Table");
		
		for (int i = 0; i < size; i++) {
			if (actionTable.containsKey(i)) {
				for (String symbol : actionTable.get(i).keySet()) {
					String action = actionTable.get(i).get(symbol);
					System.out.println(i+" x "+symbol+" -> "+action);
				}
			}
		}
		
		System.out.println();
		if (lrtable.IS_LR1())
			System.out.println("Grammar is LR1");
		System.out.println();
	}
	
	@Test
	public void test8() {
		
		System.out.println("Test 8");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "E + T"));
		grammar.addProduction(new Production(NONTERMINAL.E, "T"));
		grammar.addProduction(new Production(NONTERMINAL.T, "T * F"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		LRtable lrtable = new LRtable(grammar);
		
		System.out.println("Análise Sintática");
		String[] word = "( ( id + id + id ) * id )".split(" ");
		List<String> input = new ArrayList<>();
		for (String w : word)
			input.add(w);
		lrtable.compute(input);
		
		System.out.println();
	}
	
	@Test
	public void test9() {
		
		System.out.println("Test 9");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production("E", "T E'"));
		grammar.addProduction(new Production("E'", "+ T E'"));
		grammar.addProduction(new Production("E'", "&"));
		grammar.addProduction(new Production("T", "F T'"));
		grammar.addProduction(new Production("T'", "* F T'"));
		grammar.addProduction(new Production("T'", "&"));
		grammar.addProduction(new Production("F", "( E )"));
		grammar.addProduction(new Production("F", "id"));
		
		for (Production production : grammar.getProductions()) 
			System.out.println(production.head()+" -> "+production.rule());
		System.out.println();
		
		LRtable lrtable = new LRtable(grammar);
		
		System.out.println("Análise Sintática");
		String[] word = "( id + id )".split(" ");
		System.out.println(word.length);
		List<String> input = new ArrayList<>();
		for (String w : word)
			input.add(w);
		lrtable.compute(input);
		
		System.out.println();		
	}
	
	@Test
	public void test10() {
		
		System.out.println("Test 10");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production("E", "T E'"));
		grammar.addProduction(new Production("E'", "+ T E'"));
		grammar.addProduction(new Production("E'", "&"));
		grammar.addProduction(new Production("T", "F T'"));
		grammar.addProduction(new Production("T'", "* F T'"));
		grammar.addProduction(new Production("T'", "&"));
		grammar.addProduction(new Production("F", "( E )"));
		grammar.addProduction(new Production("F", "id"));
		
		LRtable lrtable = new LRtable(grammar);
		
		System.out.println("Análise Sintática");
		String[] word = "( id + id )".split(" ");
		System.out.println(word.length);
		List<String> input = new ArrayList<>();
		for (String w : word)
			input.add(w);
		lrtable.compute(input);
		
		System.out.println();
		
	}
	
	@Test
	public void test11() {
		
		System.out.println("Test 11");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "T El"));
		grammar.addProduction(new Production(NONTERMINAL.El, "+ T El"));
		grammar.addProduction(new Production(NONTERMINAL.El, "&"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F Tl"));
		grammar.addProduction(new Production(NONTERMINAL.Tl, "* F Tl"));
		grammar.addProduction(new Production(NONTERMINAL.Tl, "&"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		LRtable lrtable = new LRtable(grammar);
		
		System.out.println("Análise Sintática");
		String[] word = "( id + id )".split(" ");
		System.out.println(word.length);
		List<String> input = new ArrayList<>();
		for (String w : word)
			input.add(w);
		lrtable.compute(input);
		
		System.out.println();
		
	}
	
	@Test
	public void test12() {
		
		System.out.println("Test 12");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.E, "T A"));
		grammar.addProduction(new Production(NONTERMINAL.A, "+ T A"));
		grammar.addProduction(new Production(NONTERMINAL.A, "&"));
		grammar.addProduction(new Production(NONTERMINAL.T, "F B"));
		grammar.addProduction(new Production(NONTERMINAL.B, "* F B"));
		grammar.addProduction(new Production(NONTERMINAL.B, "&"));
		grammar.addProduction(new Production(NONTERMINAL.F, "( E )"));
		grammar.addProduction(new Production(NONTERMINAL.F, "id"));
		
		
		for (Production production : grammar.getProductions())
			System.out.println(production.head()+" -> "+production.rule());
		System.out.println();
		
		List<String> symbols = grammar.getSymbols();
		List<String> terminals = grammar.getTerminals();
		List<String> nonterminals = grammar.getNonterminals();
		System.out.println("Symbols: "+symbols);
		System.out.println("Terminals: "+terminals);
		System.out.println("Nonterminals: "+nonterminals);
		
		
		
		for (String nonterminal : grammar.getNonterminals()) {
			List<String> firstPos = grammar.firstPos(nonterminal);
			System.out.println("FirstPos "+nonterminal+": "+firstPos);
		}
		System.out.println();
		
		LRtable lrtable = new LRtable(grammar);
		Map<Integer, Map<String, Integer>> gotoMap = lrtable.getGotoTable();
		
		int size = lrtable.getStates().size();
		
		for (int i = 0; i < size; i++) {
			
			System.out.println("STATE: "+i+" : "+printItemSet(lrtable.getStates().get(i)));
		}
		System.out.println();
		for (int i = 0; i < size; i++) {
			
			System.out.println("CLOSURE: "+i+" : "+printItemSet(lrtable.closures().get(i)));
		}
		
		System.out.println();
		
		System.out.println("GOTO TABLE");
		
		for (int i = 0; i < size; i++) {
			if (gotoMap.containsKey(i)) {
				for (String symbol : grammar.getSymbols()) {
					if (gotoMap.get(i).containsKey(symbol)) {
						int gotoIndex = gotoMap.get(i).get(symbol);
						System.out.println(i+" x "+symbol+" -> "+gotoIndex);
					}
				}
			}
		}
		System.out.println();
		
		Map<Integer, Map<String, String>> actionTable = lrtable.getActionTable();
		
		int size_ = lrtable.getStates().size();
		
		System.out.println("Action Table");
		
		for (int i = 0; i < size_; i++) {
			if (actionTable.containsKey(i)) {
				for (String symbol : actionTable.get(i).keySet()) {
					String action = actionTable.get(i).get(symbol);
					System.out.println(i+" x "+symbol+" -> "+action);
				}
			}
		}
		
		System.out.println();
		if (lrtable.IS_LR1())
			System.out.println("Grammar is LR1");
		System.out.println();
		
		System.out.println("Análise Sintática");
		String[] word = "id".split(" ");
		List<String> input = new ArrayList<>();
		for (String w : word)
			input.add(w);
		lrtable.compute(input);
		
		System.out.println();
		
		
	}
	
	@Test
	public void test13() {
		
		System.out.println("Test 13");
		
		Grammar grammar = new Grammar();
		System.out.println();
		
		grammar.addProduction(new Production(NONTERMINAL.S, "C C"));
		grammar.addProduction(new Production(NONTERMINAL.C, "c C"));
		grammar.addProduction(new Production(NONTERMINAL.C, "d"));
		
		
		for (Production production : grammar.getProductions())
			System.out.println(production.head()+" -> "+production.rule());
		System.out.println();
		
		LRtable lrtable = new LRtable(grammar);
		
		System.out.println("Análise Sintática");
		String[] word = "c d d".split(" ");
		System.out.println(word.length);
		List<String> input = new ArrayList<>();
		for (String w : word)
			input.add(w);
		lrtable.compute(input);
		
	}

}
