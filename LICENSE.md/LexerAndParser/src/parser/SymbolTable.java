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
public abstract class SymbolTable {
    
    private int code;
	private String name;

	public SymbolTable(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public abstract boolean isTerminal();

	public abstract boolean isNonTerminal();

	@Override
	public String toString() {
		return name; 
	}

	@Override
	public int hashCode() {
		return code;
	}

}
