/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author Aman Khan
 */
public class NonTerminals extends SymbolTable {
    
    
		/**
	 * Creates new {@code NonTerminal} object with specified code and
	 * designation
	 * 
	 * @param code code of nonterminal symbol
	 * @param name designation of the nonterminal in the grammar
	 */
	public NonTerminals(int code, String name) {
		super(code, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != NonTerminals.class)
			return false;
		NonTerminals nts = (NonTerminals) obj;
		return this.getCode() == nts.getCode();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public boolean isNonTerminal() {
		return true;
	}

}
