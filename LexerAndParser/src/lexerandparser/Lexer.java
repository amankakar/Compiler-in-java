/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexerandparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;


/**
 *
 * @author Aman Khan
 */
public final class Lexer {
    private Map<TokenType, String> checkToken;
    private List<Token> createToken;

    public Lexer() {
checkToken= new TreeMap<TokenType, String>();
createToken=new ArrayList<Token>();
addTokens();
    }

    
    public Lexer(Map<TokenType, String> checkToken, List<Token> createToken) {
        this.checkToken = checkToken;
        this.createToken = createToken;
    }

    public List<Token> getCreateToken() {
        return createToken;
    }
    
    
    
    public void tokenize(String input)
    {
        int position =0;
        Token token=null;
            do{
            token = seperateToken(input , position);
            if(token != null)
            {
                position = token.getIndexEnded();
                createToken.add(token);
            }
            }
            while(token != null && position != input.length());
            if(position != input.length())
            {
            JOptionPane.showMessageDialog(null, "Lexical Error at index : "+ position);
            }
            
            
        
    }

    private Token seperateToken(String input, int position) {
        if(position < 0 || position >= input.length())
        {
            JOptionPane.showMessageDialog(null, "Illegal index found : "+ position);
        }
        else
        {
            for (TokenType tokenType : TokenType.values()) {
                System.out.println(tokenType);
			Pattern p = Pattern.compile(".{" + position + "}" + checkToken.get(tokenType),
					Pattern.DOTALL);
                        
			Matcher m = p.matcher(input);
			if (m.matches()) {
				String name = m.group(1);
				return new Token(position, position + name.length(), tokenType, name);
			}

    }
        }
        return null;
    }
    
    public List<Token> checkSpace() {
		List<Token> filteredSpace = new ArrayList<Token>();
		for (Token t : this.createToken) {
			if (!t.getTokenType().isSpace()) {
				filteredSpace.add(t);
			}
		}
		return filteredSpace;
	}  
    
    public void addTokens(){
		checkToken.put(TokenType.LineComment, "(//(.*?)[\r$]?\n).*");
		checkToken.put(TokenType.OpenB, "(\\().*");
		checkToken.put(TokenType.CloseB, "(\\)).*");
		checkToken.put(TokenType.Semicolon, "(;).*");
		checkToken.put(TokenType.Comma, "(,).*");
		checkToken.put(TokenType.OpeningCB, "(\\{).*");
		checkToken.put(TokenType.ClosingCB, "(\\}).*");
		checkToken.put(TokenType.DoubleC, "\\b(\\d{1,9}\\.\\d{1,32})\\b.*");
		checkToken.put(TokenType.IntC, "\\b(\\d{1,9})\\b.*");
		checkToken.put(TokenType.Void, "\\b(void)\\b.*");
		checkToken.put(TokenType.Int, "\\b(int)\\b.*");
                checkToken.put(TokenType.WhiteSpace, "( ).*");
		checkToken.put(TokenType.Double, "\\b(int|double)\\b.*");
		checkToken.put(TokenType.Tab, "(\\t).*");
		checkToken.put(TokenType.NewLine, "(\\n).*");
		checkToken.put(TokenType.False, "\\b(false)\\b.*");
		checkToken.put(TokenType.True, "\\b(true)\\b.*");
		checkToken.put(TokenType.Null, "\\b(null)\\b.*");
		checkToken.put(TokenType.Return, "\\b(return)\\b.*");
		checkToken.put(TokenType.If, "\\b(if)\\b.*");
		checkToken.put(TokenType.Else, "\\b(else)\\b.*");
		checkToken.put(TokenType.While, "\\b(while)\\b.*");
		checkToken.put(TokenType.Point, "(\\.).*");
		checkToken.put(TokenType.Plus, "(\\+{1}).*");
		checkToken.put(TokenType.Minus, "(\\-{1}).*");
		checkToken.put(TokenType.Multiply, "(\\*).*");
		checkToken.put(TokenType.Divide, "(/).*");
		checkToken.put(TokenType.EqualEqual, "(==).*");
		checkToken.put(TokenType.Equal, "(=).*");
		checkToken.put(TokenType.ExclameEqual, "(\\!=).*");
		checkToken.put(TokenType.Greater, "(>).*");
		checkToken.put(TokenType.Less, "(<).*");
		checkToken.put(TokenType.Identifier, "\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,31})\\b.*");
                checkToken.put(TokenType.Float, "[+-]?([0-9]*[.])?[0-9]+");
    }
    
}
