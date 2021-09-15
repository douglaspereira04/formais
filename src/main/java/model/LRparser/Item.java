package model.LRparser;

import java.util.ArrayList;
import java.util.List;

import model.grammar.Production;

public class Item {
	
	private Production production;
	private String lookahead;
	private int index;
	
	public Item(Production production, String lookahead, int index) {
		this.production = production;
		this.lookahead = lookahead;
		this.index = index;
	}
	
	public String next() {
		
		if (index == production.rule().split(" ").length)
			return null;
		
		return production.rule().split(" ")[index];
	}
	
	public Item nextItem() {
		if (index == production.rule().split(" ").length)
			return null;
		
		index++;
		return this;
	}
	
	public List<String> beta_alpha() {
		List<String> beta_alpha = new ArrayList<>();
		String[] rule = production.rule().split(" ");
		for (int i = index+1; i < rule.length; i++) {
			beta_alpha.add(rule[i]);
		}
		beta_alpha.add(lookahead);
		return beta_alpha;
	}
	
	
	public Production production() {
		return this.production;
	}
	
	public String lookahead() {
		return this.lookahead;
	}
	
	public int index() {
		return this.index;
	}
	
	public void print() {
		System.out.println("["+production.head().toString()+"->"+production.rule().substring(0, index)+"."+production.rule().substring(index)+", "+lookahead+"]");
	}
	
	public String toString() {
		String rule[] = production.rule().split(" ");
		String output = "[" + production.head().toString() + "->";
		for (int i = 0; i < rule.length; i++) {
			if (i == index)
				output += ".";
			output += rule[i];
		}
		if (index == rule.length)
			output += ".";
		output += ", "+lookahead+"]";
		return output;
	}
	
	public boolean equals_ignore_lookahead(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (index != other.index())
			return false;
		if (production == null && other.production() != null)
			return false;
		else if (!production.equals(other.production()))
			return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (index != other.index())
			return false;
		if (lookahead == null && other.lookahead() != null)
			return false;
		else if (!lookahead.equals(other.lookahead()))
			return false;
		if (production == null && other.production() != null)
			return false;
		else if (!production.equals(other.production()))
			return false;
		return true;
	}
}
