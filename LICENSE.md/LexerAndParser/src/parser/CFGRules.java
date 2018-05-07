/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.Arrays;

/**
 *
 * @author Aman Khan
 */
public class CFGRules {
    
    
	/** Number of the rule */
	private int ruleNumber;

	/** Left side of production */
	private NonTerminals leftSide;

	/** Right side of production */
	private SymbolTable[] rightSide;

	/**
	 * Creates a rule
	 * 
	 * @param ruleNumber
	 *            number of rule as it is in grammar description
	 * @param leftSide
	 *            nonterminal symbol in the left side of rule
	 * @param rightSide
	 *            terminals and nonterminals in the right side
	 */
        
	public CFGRules() {
	}

    public CFGRules(int ruleNumber, NonTerminals leftSide, SymbolTable[] rightSide) {
        this.ruleNumber = ruleNumber;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

	public int getRuleNumber() {
		return ruleNumber;
	}

	public NonTerminals getLeftSide() {
		return leftSide;
	}

	public SymbolTable[] getRightSide() {
		return rightSide;
	}

    @Override
    public String toString() {
        return  "R=" + ruleNumber + ", L" + leftSide + ", R=" + Arrays.toString(rightSide)+ '}';
    }

	

}
