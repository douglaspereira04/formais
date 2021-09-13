package model.parser.ll1;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.ListingTreePrinter;
import hu.webarticum.treeprinter.TreeNode;

/**
 * Simple pojo to hold parsing result from ll1 parser
 * @author douglas
 *
 */
public class ParsingResult {

	private List<String> stack = null;
	private List<String> input = null;
	private List<String> rule = null;
	
	private TreeNode tree = null;
	
	
	public ParsingResult() {

		this.stack = new ArrayList<String>();
		this.input = new ArrayList<String>();
		this.rule = new ArrayList<String>();
	}
	
	public void addEntry(String stack, String input, String rule){
		this.stack.add(stack);
		this.input.add(input);
		this.rule.add(rule);
	}


	public List<String> getStack() {
		return stack;
	}


	public void setStack(List<String> stack) {
		this.stack = stack;
	}


	public List<String> getInput() {
		return input;
	}


	public void setInput(List<String> input) {
		this.input = input;
	}


	public List<String> getRule() {
		return rule;
	}


	public void setRule(List<String> rule) {
		this.rule = rule;
	}


	public TreeNode getTree() {
		return tree;
	}

	public String getTreeAsString() {
		return new ListingTreePrinter().getAsString(tree.getChildren().get(0));
	}
	
	public void setTree(TreeNode tree) {
		this.tree = tree;
	}



}
