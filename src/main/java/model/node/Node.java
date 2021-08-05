package model.node;

import java.util.ArrayList;

public class Node {
	private Node parent;
	private Node left;
	private Node right;
	private char data;
	private int id;
	
	private boolean nullable;
	private ArrayList<Node> firstpos;
	private ArrayList<Node> lastpos;
	private ArrayList<Node> followpos;
	
	public Node(char data) {
		setParent(parent);
		setData(data);
		setId(0);
		setLeft(null);
		setRight(null);
		setNullable(false);
		setFirstpos(new ArrayList<>());
		setLastpos(new ArrayList<>());
		setFollowpos(new ArrayList<>());
	}
	
	public Node(char data, int id) {
		setParent(parent);
		setData(data);
		setId(id);
		setLeft(null);
		setRight(null);
		setNullable(false);
		setFirstpos(new ArrayList<>());
		setLastpos(new ArrayList<>());
		setFollowpos(new ArrayList<>());
		
	}
	
	public boolean leaf() {
		return (left == null && right == null);
	}
	
	public Node parent() {
		return parent;
	}
	
	public Node left() {
		 return left;
	}
	
	public Node right() {
		return right;
	}
	
	public char data() {
		return data;
	}
	
	public int id() {
		return id;
	}
	
	public boolean nullable() {
		
		boolean leftNLB = left == null ? false : left.nullable();
		boolean rightNLB = right == null ? false : right.nullable();
		
		switch (data) {
		case '|':
			return nullable = leftNLB || rightNLB;

		case '*':
			return nullable = true;
			
		case '.':
			return nullable = leftNLB && rightNLB;
		case '&':
			return nullable = leaf();
		default:
			return nullable = false;
		}
	}
	
	
	public ArrayList<Node> firstpos() {
		
		ArrayList<Node> leftList = left == null ? new ArrayList<Node>() : left.firstpos();
		ArrayList<Node> rightList = right == null ? new ArrayList<Node>() : right.firstpos();
		
		
		switch (data) {
		case '|':
			return firstpos = union(leftList, rightList);
		case '*':
			return firstpos = left.firstpos();
		case '.':
			if (left.nullable()) {
				return firstpos = union(leftList, rightList);
			} else {
				return firstpos = left.firstpos();
			}
		default:
			ArrayList<Node> temp = new ArrayList<>(firstpos);
			firstpos.add(this);
			firstpos = union(firstpos, temp);
			break;
		}
		return firstpos;
	}
	
	public ArrayList<Node> lastpos() {
		ArrayList<Node> leftList = left == null ? new ArrayList<Node>() : left.lastpos();
		ArrayList<Node> rightList = right == null ? new ArrayList<Node>() : right.lastpos();
		
		
		switch (data) {
		case '|':
			return lastpos = union(leftList, rightList);
		case '*':
			return lastpos = left.lastpos();
		case '.':
			if (right.nullable()) {
				return lastpos = union(leftList, rightList);
			} else {
				return lastpos = right.lastpos();
			}
		default:
			ArrayList<Node> temp = new ArrayList<>(lastpos);
			lastpos.add(this);
			lastpos = union(lastpos, temp);
			break;
		}
		return lastpos;
	}
	
	public ArrayList<Node> followpos() {
		return followpos;
	}
	
	public void calcFollowpos() {
		
		if (left != null) left.calcFollowpos();
		if (right != null) right.calcFollowpos();
		if (data == '*') {
			for (Node last : lastpos()) {
				last.setFollowpos(union(last.followpos(), firstpos()));
			}
		} else if (data == '.') {
			for (Node last : left.lastpos()) {
				last.setFollowpos(union(last.followpos(),right.firstpos()));
			}
		}
		
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void setLeft(Node left) {
		this.left = left;
		if (left == null) return;
		this.left.setParent(this);
	}
	
	public void setRight(Node right) {
		this.right = right;
		if (right == null) return;
		this.right.setParent(this);
	}
	
	public void setData(char data) {
		this.data = data;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	
	public void setFirstpos (ArrayList<Node> firstpos) {
		this.firstpos = firstpos;
	}
	
	public void setLastpos (ArrayList<Node> lastpos) {
		this.lastpos = lastpos;
	}
	
	public void setFollowpos (ArrayList<Node> followpos) {
		this.followpos = followpos;
	}
	
	public ArrayList<Node> union(ArrayList<Node> list1, ArrayList<Node> list2) {
		ArrayList<Node> union = new ArrayList<>();
		ArrayList<Integer> check = new ArrayList<>();
		for (Node node : list1) {
			int element = node.id();
			if (!check.contains(element)) {
				union.add(node);
				check.add(element);
			}
		}
		for (Node node : list2) {
			int element = node.id();
			if (!check.contains(element)) {
				union.add(node);
				check.add(element);
			}
		}
		return union;
	} 
}