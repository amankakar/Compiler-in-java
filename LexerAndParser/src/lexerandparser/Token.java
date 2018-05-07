/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexerandparser;

/**
 *
 * @author Aman Khan
 */
public class Token {
    
     private int indexStarted;
     private int indexEnded;
     private TokenType tokenType;
     public String tokenName;

    public Token(int indexStarted, int indexEnded, TokenType tokenType, String tokenName) {
        this.indexStarted = indexStarted;
        this.indexEnded = indexEnded;
        this.tokenType = tokenType;
        this.tokenName = tokenName;
    }


    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getIndexStarted() {
        return indexStarted;
    }

    public void setIndexStarted(int indexStarted) {
        this.indexStarted = indexStarted;
    }

    public int getIndexEnded() {
        return indexEnded;
    }

    public void setIndexEnded(int indexEnded) {
        this.indexEnded = indexEnded;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
@Override
	public String toString() {
		if (!this.getTokenType().isSpace())
			return tokenType + "  '" + tokenName + "' [" + indexStarted + ";" + indexEnded + "] ";
		else
			return tokenType + "   [" + indexStarted + ";" + indexEnded + "] ";
	}
   
   
          
          
}
    
