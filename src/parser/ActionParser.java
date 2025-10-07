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
public class ActionParser {
    
    private ArrayList <String> info = new ArrayList <> ();
    private ArrayList <ArrayList <String>> brokenInfo = new ArrayList <> ();
    // action name=[parameter, precon, effect]
    private TreeMap <String, ArrayList <ArrayList <String>>> map = new TreeMap <> ();
    private ArrayList <String> actionOrder = new ArrayList <> ();
    
    public ActionParser(ArrayList <String> t) {
        info = t;
        parse();
    }
    
    private void parse() {
        breakActions();
        separatePar();
        // for each action in domain
        for(ArrayList <String> actionInfo : brokenInfo) {
            String name = actionInfo.get(0);
            ArrayList <ArrayList <String>> values = new ArrayList <> ();
            // get rid of the last ")" of action
            // separate parameters, precondition, & effect
            for(int i = 1; i < actionInfo.size()-1; i ++) {
                if(actionInfo.get(i).equals(":parameters")) {
                    ArrayList <String> parameters = new ArrayList <> ();
                    values.add(parameters);
                }
                else if(actionInfo.get(i).equals(":precondition")) {
                    ArrayList <String> precondition = new ArrayList <> ();
                    values.add(precondition);
                }
                else if(actionInfo.get(i).equals(":effect")) {
                    ArrayList <String> effect = new ArrayList <> ();
                    values.add(effect);
                }
                else {
                    String temp =  actionInfo.get(i);                    
                    values.get(values.size()-1).add(temp);
                }
            }
            actionOrder.add(name);
            map.put(name, values);            
        }
    }
    
    // break each action into individual string
    private void breakActions() {
        // get rid of the last ")" of domain
        for(int i = 0; i < info.size()-1; i ++) {
            if(info.get(i).equals("(:action")) {
                // check last action, remove redundant info
                if(!brokenInfo.isEmpty())
                    while(!brokenInfo.get(brokenInfo.size()-1).get(brokenInfo.get(brokenInfo.size()-1).size()-1).equals(")")) {
                        brokenInfo.get(brokenInfo.size()-1).remove(brokenInfo.get(brokenInfo.size()-1).size()-1);
                    }
                ArrayList <String> temp = new ArrayList <> ();
                brokenInfo.add(temp);
            }
            else {                
                brokenInfo.get(brokenInfo.size()-1).add(info.get(i));
            }
        }
    }
    
    // separate all the parentheses in string
    // e.g. "(total-cost)" becomes "(", "total-cost", & ")"
    private void separatePar() {
        ArrayList <ArrayList <String>> separated = new ArrayList <>();
        // iterate through each action info string list
        for(ArrayList <String> a : brokenInfo) {
            ArrayList <String> temp = new ArrayList <>();
            // add first element to the list
            // it is the action name so does not have "()"
            temp.add(a.get(0));
            // iterate through action info string list
            // iteration starts from the second element
            for(int i = 1; i < a.size(); i ++) {
                String s = a.get(i);
                // separate the element string into a Char array
                char[] ch = new char[s.length()]; 
                for(int j = 0; j < s.length(); j++) { 
                    ch[j] = s.charAt(j); 
                }
                // iterate through the Char array
                for(int k = 0; k < ch.length; k++) {
                    if(ch[k] == '(') temp.add("(");
                    else if(ch[k] == ')') temp.add(")");
                    else {
                        String t = temp.get(temp.size()-1);
                        // if the last string is "(", then create a new string
                        if(t.equals("(")) temp.add(String.valueOf(ch[k]));
                        // else concatenate to the last string
                        else {
                            // if it is not the first Char then concatenate
                            if(k != 0) {
                                t = t + String.valueOf(ch[k]);
                                temp.set(temp.size()-1, t);
                                
                            }
                            // else create a new string
                            else temp.add(String.valueOf(ch[k]));
                        }
                    }
                }                
            }
            separated.add(temp);
        }
        // replace the original list with the separated list
        brokenInfo = separated;
    }
    
    public TreeMap <String, ArrayList <ArrayList <String>>> getActions() {
        return map;
    }
    
    public ArrayList <String> getActionOrder() {
        return actionOrder;
    }
}
