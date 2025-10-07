/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author danchengGao
 */
public class PredicateParser {
    private ArrayList <String> info = new ArrayList <> ();
    private ArrayList <ArrayList <String>> brokenInfo = new ArrayList <> ();
    // map predicte name to arguments
    private TreeMap <String, ArrayList <String[]>> map = new TreeMap <> ();
    
    public PredicateParser (ArrayList <String> t) {
        info = t;
        parse();
    }
    
    private void parse() {
        breakInfo();        
        for (int i = 0; i < brokenInfo.size(); i++) {
            ArrayList <String> predicate = brokenInfo.get(i);  
            String predicateName = predicate.get(0).substring(1);
            // remove the last  ")"
            if(predicateName.endsWith(")")) {
                predicateName = predicateName.substring(0, predicateName.length()-1);
            }
            ArrayList <String[]> arguments = new ArrayList <> ();
            int counter = 1;
            for (int j = 1; j < predicate.size(); j++) {
                // arguments have types
                if(predicate.get(j).equals("-")) {
                    while(counter < j) {
                    	String argumentName = predicate.get(counter);
                        if(predicate.get(j+1).equals("(either")) {
                            String argumentType1 = predicate.get(j+2);
                            String argumentType2 = predicate.get(j+3);
                            if (argumentType2.charAt(argumentType2.length()-1) == ')')
                                argumentType2 = argumentType2.replace(argumentType2.substring(argumentType2.length()-1), "");
                            String[] argument = new String[] {argumentName, argumentType1, argumentType2};
                            arguments.add(argument);
                            j = j+2;
                            break;
                        }
                        else {
                            String argumentType = predicate.get(j+1);
                            if (argumentType.charAt(argumentType.length()-1) == ')')
                                argumentType = argumentType.replace(argumentType.substring(argumentType.length()-1), "");
                            String[] argument = new String[] {argumentName, argumentType};
                            arguments.add(argument);
                            counter = counter + 1;
                        }                        
                    }
                    counter = j+2;
                    j = j+2;
                }
                // arguments do not have types
                else {
                    if(j == predicate.size()-1) {
                        while(counter < predicate.size()) {
                    	    String argumentName = predicate.get(counter);
                            String argumentType = "";
                            if (argumentName.charAt(argumentName.length()-1) == ')')
                                argumentName = argumentName.replace(argumentName.substring(argumentName.length()-1), "");
                            String[] argument = new String[] {argumentName, argumentType};
                            arguments.add(argument);
                            counter = counter + 1;
                        }
                    }
                }
            }
            map.put(predicateName, arguments);            
        }
    }
    
    private void breakInfo() {
        int index = 0;
        int parenthesis_count = 0;
        for (int i = 0; i < info.size(); i++) {
            String t = info.get(i);
            char firstChar = t.charAt(0);
            if(firstChar == '(') {
                parenthesis_count ++;
            }
            char lastChar = t.charAt(t.length()-1);
            if(lastChar == ')') {
                parenthesis_count --;
                if(parenthesis_count == 0) {
                    ArrayList <String> temp = new ArrayList <> ();
                    for (int j = index; j <= i; j++) {
                        temp.add(info.get(j));
                    }
                    index = i+1;
                    brokenInfo.add(temp);
                }                
            }
        }
    }
    
    public TreeMap <String, ArrayList <String[]>> getPredicates() {
        return map;
    }
}
