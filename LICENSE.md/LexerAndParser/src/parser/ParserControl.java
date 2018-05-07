/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

import lexerandparser.Token;

import static jdk.nashorn.internal.objects.NativeArray.map;

/**
 * The {@code Parser} class represents predictive parser. It accepts only LL(1)
 * grammar. If the grammar is not LL(1) most likely you will get
 * {@code StackOverflowError}. Productions in the grammar use the following
 * format, for example:
 *
 * <blockquote>
 *
 * <pre>
 *  Goal -> A
 *  A -> ( A ) | Two
 *  Two -> a
 * </pre>
 *
 * </blockquote>
 *
 * Symbol is inferred as nonterminal by first uppercase char. "->" designates
 * definition, "|" designates alternation, and newlines designate termination.
 * Use "EPSILON" to represent an empty string. Place spaces between things you
 * don't want to read as one symbol: ( A ) != (A).
 *
 * @author Ira Korshunova
 *
 */
public class ParserControl {

    /**
     * Terminal symbol of grammar which represents empty string
     */
    public static Terminals empty = new Terminals(0, "EMPTY");

    /**
     * Terminal symbol which represents end of program
     */
    public static Terminals endOfProgram = new Terminals(-1, "ENDOFPROGRAM");

    /**
     * Start symbol of the grammar
     */
    private NonTerminals sSymbol;

    /**
     * List of rules in the grammar without alternations
     */
    private List<CFGRules> rules;

    /**
     * Grammar's alphabet. Contains terminal and nonterminal symbols
     */
    private Set<SymbolTable> alphabet;

    /**
     * Mapping from string representation of symbol to its object
     */
    private Map<String, SymbolTable> nameToSymbol;

    /**
     * Mapping from symbol to it's first set
     */
    private Map<SymbolTable, Set<Terminals>> firstSet;

    /**
     * Mapping from symbol to it's follow set
     */
    private Map<SymbolTable, Set<Terminals>> followSet;

    /**
     * Representation of parsing table for LL(1) parser
     */
    private Map<SimpleEntry<NonTerminals, Terminals>, SymbolTable[]> parsingTable;

    /**
     * Stack of terminals, which were constructed from input tokens
     */
    private Stack<Terminals> input;

    /**
     * Sequence of applied rules during the derivations
     */
    private List<CFGRules> sequenceOfAppliedRules;

    /**
     * Initializes a newly created {@code Parser} object
     *
     */
    public ParserControl() {
        rules = new ArrayList<CFGRules>();
        alphabet = new HashSet<SymbolTable>();
        nameToSymbol = new HashMap<String, SymbolTable>();
        alphabet.add(empty);
        firstSet = new HashMap<SymbolTable, Set<Terminals>>();
        followSet = new HashMap<SymbolTable, Set<Terminals>>();
        parsingTable = new HashMap<SimpleEntry<NonTerminals, Terminals>, SymbolTable[]>();
        sequenceOfAppliedRules = new ArrayList<CFGRules>();
    }

    /**
     * Parses the source, represented by the list of tokens, using the specified
     * LL(1) grammar rules
     *
     * @param grammarFile file with grammar rules
     * @param list list of tokens from the input
     * @throws FileNotFoundException if file doesn't exist
     * @throws AnalyzerException if the input contains syntax error
     */
    public void parse(File grammarFile, List<Token> list) throws FileNotFoundException {
        parseRules(grammarFile);
        calculateFirstSet();
        calculateFollowSet();
        createParsingTable();
        input = convertToStack(list);
        performAlgorithm();
    }

    /**
     * Returns the sequence of grammar rules, which were applied during the
     * parsing
     *
     * @return list of applied rules
     *
     */
    public List<CFGRules> getSequenceOfAppliedRules() {
        return sequenceOfAppliedRules;
    }

    /**
     * Implements LL(1) predictive parsing algorithm
     *
     * @throws AnalyzerException if syntax error was found
     */
    private void performAlgorithm() {
        Stack<SymbolTable> stack = new Stack<SymbolTable>();
        stack.push(endOfProgram);
        stack.push(sSymbol);
        int parsedTokensCount = 0;
        do {

            SymbolTable stackTop = stack.peek();
            System.out.println("stack:"+stackTop);
            Terminals inputTop = input.peek();
            if (stackTop.isTerminal()) {
                if (stackTop.equals(inputTop)) {
                    stack.pop();
                    input.pop();
                    parsedTokensCount++;
                } else {
                    JOptionPane.showMessageDialog(null, this, "Syntax error after token 1:"+ String.valueOf(parsedTokensCount), JOptionPane.ERROR_MESSAGE);
                   return;
                }
            } else {
                SimpleEntry<NonTerminals, Terminals> tableKey = new SimpleEntry<NonTerminals, Terminals>(
                        (NonTerminals) stackTop, inputTop);
                if (parsingTable.containsKey(tableKey)) {
                    stack.pop();
                    System.out.println("stack:"+stackTop);
                    SymbolTable[] tableEntry = parsingTable.get(tableKey);
                    for (int j = tableEntry.length - 1; j > -1; j--) {
                        if (!tableEntry[j].equals(empty)) {
                            stack.push(tableEntry[j]);
                        }
                    }
                    sequenceOfAppliedRules.add(getRule((NonTerminals) stackTop, tableEntry));
                } else {
                    System.out.println("stack:"+stackTop);
 JOptionPane.showMessageDialog(null," ", "Syntax error after token :"+parsedTokensCount, JOptionPane.ERROR_MESSAGE);
                               return ;         

                }
            }
        } while (!stack.isEmpty() && !input.isEmpty());

        if (!input.isEmpty()) {

          JOptionPane.showMessageDialog(null," ", "Syntax error after token3 :"+parsedTokensCount, JOptionPane.ERROR_MESSAGE);
           
        
        }
    }

    /**
     * Converts a list of tokens from lexer to stack of terminals for parser.
     * Fist token in the input will be at the top of the stack.
     *
     * @param inputTokens list of input tokens
     * @return stack of terminal symbols
     */
    private Stack<Terminals> convertToStack(List<Token> inputTokens) {
        Stack<Terminals> input = new Stack<Terminals>();
        Collections.reverse(inputTokens);
        input.push(endOfProgram);
        //System.out.println("ERROR TOKEN:"+inputTokens.toString());
        for (Token token : inputTokens) {
            Terminals s = (Terminals) nameToSymbol.get(token.getTokenName());
            
            if (s == null) {
                
                switch (token.getTokenType()) {
                    case Identifier:
                        s = (Terminals) nameToSymbol.get("id");
                        break;
                    case IntC:
                        s = (Terminals) nameToSymbol.get("intConst");
                        break;
                    case DoubleC:
                        s = (Terminals) nameToSymbol.get("doubleConst");
                        break;
                    default:
                        System.out.println(inputTokens.toString()+"ERROR TOKEN:");
                        throw new RuntimeException("Somethig is wrong!");
                }
            }
            System.out.println("ERROR TOKEN1:"+s);
            input.push(s);
        }
        return input;
    }

    /**
     * Automatically builds LL(1) parsing table by using follow and first set
     */
    private void createParsingTable() {
        for (CFGRules r : rules) {
            SymbolTable[] rightSide = r.getRightSide();
            NonTerminals leftSide = r.getLeftSide();
            Set<Terminals> firstSetForRightSide = first(rightSide);
            Set<Terminals> followSetForLeftSide = followSet.get(leftSide);

            for (Terminals s : firstSetForRightSide) {
                parsingTable.put(new SimpleEntry<NonTerminals, Terminals>(leftSide, s), rightSide);
            }

            if (firstSetForRightSide.contains(empty)) {
                for (Terminals s : followSetForLeftSide) {
                    parsingTable
                            .put(new SimpleEntry<NonTerminals, Terminals>(leftSide, s), rightSide);
                }
            }
        }
    }

    private void calculateFirstSet() {
        for (SymbolTable s : alphabet) {
            firstSet.put(s, new HashSet<Terminals>());
        }
        for (SymbolTable s : alphabet) {
            first(s);
        }
    }

    /**
     * Calculates first set for specified symbol. By using the next rules:
     * <blockquote>
     *
     * <pre>
     * 1. If X is terminal, then FIRST(X) is {X}.
     * 2. If X -> EPSILON is production, then add EPSILON to FIRST(X).
     * 3. If X is nonterminal and X -> Y1 Y2 ... Yk is a production,
     * then place <i>a</i> (terminal) in FIRST(X) if for some i <i>a</i> is in FIRST(Yi), and Y1, ... ,Yi-1 -> EPSILON.
     * If EPSILON is in FIRST(Yj) for all j = 1, 2, ... , k, then add EPSILON to FIRST(X).
     * </pre>
     *
     * </blockquote>
     *
     *
     * @param s terminal or nonterminal symbol of grammar
     */
    private void first(SymbolTable s) {
        Set<Terminals> first = firstSet.get(s);
        Set<Terminals> auxiliarySet;
        if (s.isTerminal()) {
            first.add((Terminals) s);
            return;
        }

        for (CFGRules r : getRulesWithLeftSide((NonTerminals) s)) {
            SymbolTable[] rightSide = r.getRightSide();
            first(rightSide[0]);
            auxiliarySet = new HashSet<Terminals>(firstSet.get(rightSide[0]));
            auxiliarySet.remove(empty);
            first.addAll(auxiliarySet);

            for (int i = 1; i < rightSide.length
                    && firstSet.get(rightSide[i - 1]).contains(empty); i++) {
                first(rightSide[i]);
                auxiliarySet = new HashSet<Terminals>(firstSet.get(rightSide[i]));
                auxiliarySet.remove(empty);
                first.addAll(auxiliarySet);
            }

            boolean allContainEpsilon = true;
            for (SymbolTable rightS : rightSide) {
                if (!firstSet.get(rightS).contains(empty)) {
                    allContainEpsilon = false;
                    break;
                }
            }
            if (allContainEpsilon) {
                first.add(empty);
            }
        }
    }

    /**
     * Calculates first set for chain of symbols
     *
     * @param chain string of symbols
     * @return first set for the specified string
     */
    private Set<Terminals> first(SymbolTable[] chain) {
        Set<Terminals> firstSetForChain = new HashSet<Terminals>();
        Set<Terminals> auxiliarySet;
        auxiliarySet = new HashSet<Terminals>(firstSet.get(chain[0]));
        auxiliarySet.remove(empty);
        firstSetForChain.addAll(auxiliarySet);

        for (int i = 1; i < chain.length && firstSet.get(chain[i - 1]).contains(empty); i++) {
            auxiliarySet = new HashSet<Terminals>(firstSet.get(chain[i]));
            auxiliarySet.remove(empty);
            firstSetForChain.addAll(auxiliarySet);
        }

        boolean allContainEpsilon = true;
        for (SymbolTable s : chain) {
            if (!firstSet.get(s).contains(empty)) {
                allContainEpsilon = false;
                break;
            }
        }
        if (allContainEpsilon) {
            firstSetForChain.add(empty);
        }

        return firstSetForChain;
    }

    private void calculateFollowSet() {
        for (SymbolTable s : alphabet) {
            if (s.isNonTerminal()) {
                followSet.put(s, new HashSet<Terminals>());
            }
        }

        Map<SimpleEntry<SymbolTable, SymbolTable>, Boolean> callTable = new HashMap<SimpleEntry<SymbolTable, SymbolTable>, Boolean>();
        for (SymbolTable firstS : alphabet) {
            for (SymbolTable secondS : alphabet) {
                callTable.put(new SimpleEntry<SymbolTable, SymbolTable>(firstS, secondS), false);
            }
        }

        NonTerminals firstSymbol = rules.get(0).getLeftSide();
        followSet.get(firstSymbol).add(endOfProgram);
        for (SymbolTable s : alphabet) {
            if (s.isNonTerminal()) {
                follow((NonTerminals) s, null, callTable);
            }
        }
    }

    /**
     * Calculates follow set for nonterminal symbols
     */
    private void follow(NonTerminals s, SymbolTable caller,
            Map<SimpleEntry<SymbolTable, SymbolTable>, Boolean> callTable) {
        Boolean called = callTable.get(new SimpleEntry<SymbolTable, SymbolTable>(caller, s));
        System.out.println("called" + called);
        if (called != null) {
            if (called == true) {
                return;
            } else {
                callTable.put(new SimpleEntry<SymbolTable, SymbolTable>(caller, s), true);
            }
        }

        Set<Terminals> follow = followSet.get(s);
        Set<Terminals> auxiliarySet;

        List<SimpleEntry<NonTerminals, SymbolTable[]>> list = getLeftSideRightChain(s);
        for (SimpleEntry<NonTerminals, SymbolTable[]> pair : list) {
            SymbolTable[] rightChain = pair.getValue();
            NonTerminals leftSide = pair.getKey();
            if (rightChain.length != 0) {
                auxiliarySet = first(rightChain);
                auxiliarySet.remove(empty);
                follow.addAll(auxiliarySet);
                if (first(rightChain).contains(empty)) {
                    follow(leftSide, s, callTable);
                    follow.addAll(followSet.get(leftSide));
                }
            } else {
                follow(leftSide, s, callTable);
                follow.addAll(followSet.get(leftSide));
            }
        }
    }

    /**
     * Constructs grammar rules from file
     *
     * @param grammarFile file with grammar rules
     * @throws FileNotFoundException if file with the specified pathname does
     * not exist
     */
    private void parseRules(File grammarFile) throws FileNotFoundException {
        nameToSymbol.put("EPSILON", empty);

        Scanner data = new Scanner(grammarFile);
        int code = 1;
        int ruleNumber = 0;
        while (data.hasNext()) {
            StringTokenizer t = new StringTokenizer(data.nextLine());
            String symbolName = t.nextToken();
            if (!nameToSymbol.containsKey(symbolName)) {
                SymbolTable s = new NonTerminals(code, symbolName);
                if (code == 1) {
                    sSymbol = (NonTerminals) s;
                }
                nameToSymbol.put(symbolName, s);
                alphabet.add(s);
                code++;
            }
            t.nextToken();// ->

            NonTerminals leftSide = (NonTerminals) nameToSymbol.get(symbolName);
            while (t.hasMoreTokens()) {
                List<SymbolTable> rightSide = new ArrayList<SymbolTable>();
                do {
                    symbolName = t.nextToken();
                    if (!symbolName.equals("|")) {
                        if (!nameToSymbol.containsKey(symbolName)) {
                            SymbolTable s;
                            if (Character.isUpperCase(symbolName.charAt(0))) {
                                s = new NonTerminals(code++, symbolName);
                            } else {
                                s = new Terminals(code++, symbolName);
                            }
                            nameToSymbol.put(symbolName, s);
                            alphabet.add(s);
                        }
                        rightSide.add(nameToSymbol.get(symbolName));
                    }
                } while (!symbolName.equals("|") && t.hasMoreTokens());
                rules.add(new CFGRules(ruleNumber++, leftSide, rightSide.toArray(new SymbolTable[]{})));
            }
        }
    }

    /**
     * Returns rules with specified left side
     *
     * @param nonTerminalSymbol symbol in the left side of the production
     * @return set of rules which contain the specified symbol in the left side
     */
    private Set<CFGRules> getRulesWithLeftSide(NonTerminals nonTerminalSymbol) {
        Set<CFGRules> set = new HashSet<CFGRules>();
        for (CFGRules r : rules) {
            if (r.getLeftSide().equals(nonTerminalSymbol)) {
                set.add(r);
            }
        }
        return set;
    }

    /**
     * Returns list of pairs. First element of the pair is the left side of the
     * rule if this rule contains specified symbol {@code s} in the right side.
     * The second element contains symbols after {@code s} in the right side of
     * the rule.
     *
     * @param s
     * @return
     */
    private List<SimpleEntry<NonTerminals, SymbolTable[]>> getLeftSideRightChain(SymbolTable s) {
        List<SimpleEntry<NonTerminals, SymbolTable[]>> list = new ArrayList<SimpleEntry<NonTerminals, SymbolTable[]>>();
        for (CFGRules r : rules) {
            SymbolTable[] rightChain = r.getRightSide();
            int index = Arrays.asList(rightChain).indexOf(s);
            if (index != -1) {
                rightChain = Arrays.copyOfRange(rightChain, index + 1, rightChain.length);
                list.add(new SimpleEntry<NonTerminals, SymbolTable[]>(r.getLeftSide(), rightChain));
            }
        }
        return list;
    }

    /**
     * Returns the rule with specified left and right side
     *
     * @param leftSide symbol in the left side of the production
     * @param rightSide symbols in the right side
     * @return rule with specified left and right side or {@code null} if such
     * rule doesn't exist in grammar
     */
    private CFGRules getRule(NonTerminals leftSide, SymbolTable[] rightSide) {
        Set<CFGRules> setOfRules = getRulesWithLeftSide(leftSide);
        for (CFGRules r : setOfRules) {
            if (rightSide.length != r.getRightSide().length) {
                continue;
            }
            for (int i = 0; i < rightSide.length; i++) {
                if (r.getRightSide()[i] != rightSide[i]) {
                    break;
                } else {
                    if (i == rightSide.length - 1) {
                        return r;
                    }
                }
            }
        }

        return null;
    }

}
