package model.regex;

import java.util.ArrayList;
import java.util.Stack;

import exception.automata.DuplicatedStateException;
import exception.automata.DuplicatedTransitionException;
import exception.automata.InvalidStateException;
import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;
import model.node.Node;

/**
 * Regular Expression class
 * 
 * @author tiago
 *
 */
public class Regex {

	/**
	 *	Regular Expression String
	 */
	private String regex;
	/**
	 * Builds new Regular Expression
	 * 
	 * @param Regular Expression String
	 * @throws BracketMismatchException
	 * @throws OperatorMismatchException
	 * @throws InvalidInputException
	 */
	public Regex(String rgx) throws BracketMismatchException, OperatorMismatchException, InvalidInputException {

		if (rgx == null)
			throw new NullPointerException();

		char[] rgx_char = rgx.toCharArray();
		int bracket = 0;
		
		for (int i = 0; i < rgx.length(); i++) {
			char el = rgx_char[i];
			char next = '&';
			if (i < rgx.length() - 1)
				next = rgx_char[i + 1];
			
			//Counts opening bracket '('
			if (isOperator(el) == 1)
				bracket++;
			//Counts closing bracket ')'
			if (isOperator(el) == 2)
				bracket--;
			//Unopened bracket closed
			if (bracket < 0)
				throw new OperatorMismatchException();
			//Starts with: ')', '|' and '*'
			if (i == 0) {
				if (isOperator(el) > 1)
					throw new OperatorMismatchException();
			}
			//Ends with: '(' and '|'
			if (i == rgx.length()-1) {
				if (isOperator(el) == 1 || isOperator(el) == 3) 
					throw new OperatorMismatchException();
			}
			//Contains sequence: "(*", "(|", "|*" and "||" 
			if (isOperator(el) == 1 || isOperator(el) == 3) {
				if (isOperator(next) == 3 || isOperator(next) == 4)
					throw new OperatorMismatchException();
			}
			//Contains implicit operator '.'
			if (isOperator(el) == 5)
				throw new InvalidInputException("Invalid Input: .");
		}
		//Unclosed bracket
		if (bracket != 0)
			throw new BracketMismatchException();

		regex = rgx;
	}
	/**
	 * Generate Regular Expression Tree
	 * @return root Node
	 */
	public Node tree() {
		/**
		 * Applies implicit operator '.' on Regex String through concatOp method
		 * Converts from conventional infix to parenthesis-less postfix notation  
		 */
		String postfix = infixToPostfix(concatOp(this.regex));
		Stack<Node> stack = new Stack<>();
		char[] regex = postfix.toCharArray();
		//System.out.println(postfix);
		Node root = new Node('.');
		root.setRight(new Node('#'));
		Node node;
		int id = 1;
		/**
		 * Default Case: Create a leaf element Node and push it to the stack
		 * Operand Case: Create a non-leaf operand Node and link properly
		 */
		for (int i = 0; i < regex.length; i++) {
			char element = regex[i];
			switch (element) {
			case '|':
				node = new Node(element);
				root.setLeft(node);
				node.setRight(stack.pop());
				node.setLeft(stack.pop());
				stack.push(node);
				break;
			case '*':
				node = new Node(element);
				root.setLeft(node);
				node.setLeft(stack.pop());
				stack.push(node);
				break;
			case '.':
				node = new Node(element);
				root.setLeft(node);
				node.setRight(stack.pop());
				node.setLeft(stack.pop());
				stack.push(node);
				break;
			default:
				stack.push(new Node(element, id));
				id++;
				break;
			}
		}
		root.right().setId(id);
		return root;
	}
	
	/**
	 * Build FDA from Regex Tree
	 * @throws DuplicatedStateException
	 * @throws InvalidStateException
	 * @throws DuplicatedTransitionException
	 * @return Equivalent Automata
	 */
	public Automata convert() throws DuplicatedStateException, InvalidStateException, DuplicatedTransitionException {
		
		//Empty or Single Character Input
		if (regex.trim().isEmpty()) return new Automata('&');
		if (regex.length() == 1) return new Automata(regex.charAt(0));
		
		Automata automata = new Automata();
		Node root = tree();
		root.calcFollowpos();
		Stack<ArrayList<Node>> stack = new Stack<>();
		//Push Initial State
		stack.push(root.firstpos());
		boolean initial = true;
		while (!stack.isEmpty()) {
			/**
			 * Values: Set of all possible transitions
			 * Component: Set of leaves forming a state
			 */
			ArrayList<Character> values = new ArrayList<>();
			ArrayList<Node> component = stack.pop();
			String source = "";
			for (Node node : component) {
				if (!values.contains(node.data())) values.add(node.data());
				source += Integer.toString(node.id());
			}
			if (!automata.hasState(source)) automata.addState(source);
			if (initial) automata.setInitialState(source);
			initial = false;
			if (values.contains('#')) automata.setAsFinalState(source);
			for (char element : values) {
				String target = "";
				ArrayList<Node> nextState = new ArrayList<>();
				for (Node node : component) {
					if (element == node.data()) {
						nextState = union(nextState, node.followpos());
					}
				}
				for (Node node : nextState) {
					target += Integer.toString(node.id());
				}
				
				if (!target.equalsIgnoreCase("")) {
					if (!automata.hasState(target)) {
						automata.addState(target);
						stack.push(nextState);
					}
					automata.addTransition(source, element, target);
				}
			}
		}
		return automata;
	}
	
	public void printTree(Node node) {
		System.out.println(node.data());
		if (node.left() != null) printTree(node.left());
		if (node.right() != null) printTree(node.right());
	}
	
	public void prints(Node it) {
		if (it.parent() == null) it.calcFollowpos();
		System.out.println("Data: "+it.data());
		System.out.println("Nullable");
		System.out.println(it.nullable());
		System.out.println("Firstpos");
		for (Node node : it.firstpos()) System.out.println(node.id());
		System.out.println("Lastpos");
		for (Node node : it.lastpos()) System.out.println(node.id());
		if (it.leaf()) {
			System.out.println("Followpos");
			for (Node node : it.followpos()) System.out.println(node.id());
		}
		
		if (it.left() != null) prints(it.left());
		if (it.right() != null) prints(it.right());
	}
	
	/**
	 * Infix to Postfix notation converter
	 * (a|b)*.a.b.b should return ab|*a.b.b
	 * Parenthesis-less precedence oriented sequential notation
	 * @return Reversed Polish noted String
	 */
	public String infixToPostfix(String infix) {

		char[] infixA = infix.toCharArray();
		String postfix = "";
		Stack<Character> ops = new Stack<>();
		for (int i = 0; i < infixA.length; i++) {
			char el = infixA[i];
			int value = isOperator(el);
			if (value == 0) {
				//Add all operands to the String
				postfix += el;
			} else if (value == 1) {
				//Push '(' to the Stack
				ops.push(el);
			} else if (value == 2) {
				//In ')' pop everything back to '('
				while (isOperator(ops.peek()) != 1) {
					postfix += ops.pop();
				}
				ops.pop();
			} else {
				//Pop operators with higher precedence 
				while (!ops.isEmpty() && isOperator(ops.peek()) != 1 && prec(el) <= prec(ops.peek()))
					postfix += ops.pop();
				ops.push(el);
			}
		}
		while (!ops.isEmpty())
			postfix += ops.pop();
		return postfix;
	}

	/**
	 * Add Implicit Concatenation Operator '.'
	 * (a|b)*abb should return (a|b)*.a.b.b
	 * Simplifies analysis by creating internal operand
	 * @param Regular Expression String (regex)
	 * @return Regex String using concatenation operator '.'
	 */
	public String concatOp(String regex) {
		char[] regexA = regex.toCharArray();
		regex = "";
		for (int i = 0; i < regexA.length - 1; i++) {
			char el = regexA[i];
			char next = regexA[i + 1];
			regex += el;
			if (isOperator(el) == 0 || el == ')' || el == '*') {
				if (isOperator(next) == 0 || next == '(')
					regex += '.';
			}
		}
		regex += regexA[regexA.length - 1];
		return regex;
	}

	/**
	 * Operator Identifier Method
	 * @param Any character from the Regex String
	 * @return Integer in range [1-5] for operator, 0 for operand
	 */
	public int isOperator(char lexeme) {
		switch (lexeme) {
		case '(':
			return 1;
		case ')':
			return 2;
		case '|':
			return 3;
		case '*':
			return 4;
		case '.':
			return 5;
		default:
			return 0;
		}
	}

	/**
	 * Precedence Identifier Method 
	 * @param Any character from the Regex String
	 * @return Integer number indicating precedence magnitude
	 */
	public int prec(char lexeme) {
		switch (lexeme) {
		case '|':
			return 1;
		case '.':
			return 2;
		case '*':
			return 3;
		default:
			return 0;
		}
	}

	public String getRegex() {
		return regex;
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
