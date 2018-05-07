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
public class Terminals extends SymbolTable {
    
    public Terminals(int code, String name) {
		super(code,name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if(obj == null)
			return false;
		if (obj.getClass() != Terminals.class)
			return false;
		Terminals ts = (Terminals) obj;
		return this.getCode() == ts.getCode();
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public boolean isNonTerminal() {
		return false;
	}

    
}
