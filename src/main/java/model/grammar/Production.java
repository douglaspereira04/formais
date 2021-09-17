package model.grammar;


public class Production {
	
	public enum NONTERMINAL {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X ,Y, Z,
		Al, Bl, Cl, Dl, El, Fl, Gl, Hl, Il, Jl, Kl, Ll, Ml, Nl, Ol, Pl, Ql, Rl, Sl, Tl, Ul, Vl, Wl, Xl ,Yl, Zl,
		extended;
	}
	
	
	private NONTERMINAL head;
	private String rule;
	
	public Production(String head, String rule) {
		
		for (NONTERMINAL nonterminal : NONTERMINAL.values()) {
			if (head.length() == 1) {
				if (nonterminal.toString().equals(head))
					this.head = nonterminal;
			} else if (head.length() == 2 && head.charAt(1) == "'".charAt(0)) {
				if (nonterminal.toString().equals(Character.toString(head.charAt(0))+"l"))
					this.head = nonterminal;
			}
			else {
				System.err.println("Syntax Error, Production Head Size is Inappropriate");
			}
		}
		
		String[] rules = rule.split(" ");
		
		for (int i = 0; i < rules.length; i++) {
			String rule_ = rules[i];
			for (NONTERMINAL nonterminal : NONTERMINAL.values()) {
				if (nonterminal.toString().equals(Character.toString(rule_.charAt(0))) && rule_.length() == 2 && rule_.charAt(1) == "'".charAt(0))
					rules[i] = rules[i].replace("'".charAt(0), 'l');
			}
		}
		
		String new_rule = "";
		for (int i = 0; i < rules.length; i++) {
			new_rule += rules[i];
			new_rule += " ";
		}
		new_rule = new_rule.trim();
	
		this.rule = new_rule;
	}
	

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
