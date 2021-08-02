package model.regex;

import java.util.Stack;

import exception.regex.BracketMismatchException;
import exception.regex.InvalidInputException;
import exception.regex.OperatorMismatchException;
import model.automata.Automata;

public class Regex {
	
	private String regex;
	
	public Regex(String rgx) throws BracketMismatchException, OperatorMismatchException, InvalidInputException {
		
		if (rgx == null) throw new NullPointerException();
		
		char[] rgx_char = rgx.toCharArray();
		int bracket = 0;
		for (int i = 0; i < rgx.length(); i++) {
			
			char el = rgx_char[i];
			char next = 'n';
			if (i < rgx.length()-1) next = rgx_char[i+1];
			
			if (isOperator(el) == 1) bracket++;
			if (isOperator(el) == 2) bracket--;
			if (bracket < 0) throw new OperatorMismatchException();
			
			
			if (isOperator(el) == 3 && i != rgx.length()-1) {
				if (isOperator(next) == 3) throw new OperatorMismatchException();
			}
			
			if (isOperator(el) != 2) {
				if (i == 0 || i == rgx.length()-1) {
					if (isOperator(next) > 2 && isOperator(el) != isOperator(next)) throw new OperatorMismatchException();
				}
			}
			
			
			if (isOperator(el)==5) throw new InvalidInputException("Invalid Input: .");
		}
		
		if (bracket != 0) throw new BracketMismatchException();
		
		
		regex = rgx;
	}
	
	public Automata convert() {
		String regex = infixToPostfix(concatOp(this.regex));
		Stack<Automata> comp = new Stack<>();
		char[] regexA = regex.toCharArray();
		
		for (int i = 0; i < regexA.length; i++) {
			char el = regexA[i];
			switch (el) {
			case '|':
				Automata automata2 = comp.pop();
				Automata automata1 = comp.pop();
				comp.push(Automata.unify(automata1, automata2));
				break;
			case '*':
				automata1 = comp.pop();
				comp.push(Automata.closure(automata1));
				break;
			case '.':
				automata2 = comp.pop();
				automata1 = comp.pop();
				comp.push(Automata.concat(automata1, automata2));
				break;
			default:
				comp.push(new Automata(el));
				break;
			}
		}
		
		return comp.pop();
	}
	
	public String infixToPostfix(String infix) {
		
		char[] infixA = infix.toCharArray();
		String postfix = "";
		Stack<Character> ops = new Stack<>();
		for (int i = 0; i < infixA.length; i++) {
			char el = infixA[i];
			int value = isOperator(el);
			if (value == 0) {
				postfix += el;
			} else if (value == 1) {
				ops.push(el);
			} else if (value == 2) {
				System.out.println(ops.peek());
				while (isOperator(ops.peek())!=1) {
					postfix += ops.pop();
				}
				ops.pop();
			} else {
				while (!ops.isEmpty() && isOperator(ops.peek())!=1 && prec(el) <= prec(ops.peek())) 
					postfix += ops.pop();
				ops.push(el);
			}
		}
		while (!ops.isEmpty()) postfix += ops.pop();
		return postfix;
	}
	
	public String concatOp(String regex) {
		char[] regexA = regex.toCharArray();
		regex = "";
		for (int i = 0; i < regexA.length-1; i++) {
			char el = regexA[i];
			char next = regexA[i+1];
			regex += el;
			if (isOperator(el)==0 || el == ')' || el == '*') {
				if (isOperator(next)==0 || next == '(') regex+='.';
			}
		}
		regex+=regexA[regexA.length-1];
		return regex;
	}
	
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
	
	public int prec (char lexeme) {
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
	
	

}
