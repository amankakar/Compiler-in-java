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
public enum TokenType {

    LineComment,
    
    WhiteSpace,
    
    Tab,
    
    NewLine,
    
    CloseB,
    
    OpenB,
    
    OpeningCB,
    
    ClosingCB,
    
    DoubleC,

    IntC,
    
    Plus,
    
    Minus,
    
    Multiply,
    
    Divide,
    
    Point,
    
    EqualEqual,
    
    Equal,
    
    ExclameEqual,
    
    Greater,
    
    Less,
    
    Int,
    
    Double,
    
    Void,
    
    Float,
    
    String,
    
    False,
    
    True,
    
    Null,
    
    Return,
    
    If,
    
    While,
    
    Else,
    
    Semicolon,
    
    Comma,
    
    Identifier;

    
    public boolean isSpace() {
        return this == LineComment || this == NewLine || this == Tab || this == WhiteSpace;
    }
}
