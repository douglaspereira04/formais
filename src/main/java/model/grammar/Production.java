package model.grammar;


public class Production {
	
	public enum NONTERMINAL {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Sl, T, U, V, W, X ,Y, Z;
	}
	
	private NONTERMINAL head;
	private String rule;
	
	public Production(NONTERMINAL head, String rule) {
		this.head = head;
		this.rule = rule;
	}
	
	public NONTERMINAL head() {
		return this.head;
	}

	public String rule() {
		return this.rule;
	}
	
	public String toString() {
		return head.toString()+"->"+rule;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Production other = (Production) obj;
		if (head == null & other.head() != null)
			return false;
		else if (!head.equals(other.head()))
			return false;
		if (rule == null && other.rule() != null)
			return false;
		else if (!rule.equals(other.rule()))
			return false;
		return true;
	}

}
