/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author danchengGao
 */
public class PDDLPrefix {
    
    ArrayList<String> logical = new ArrayList<>();
    // ArrayList<String> numerical = new ArrayList<>();
    String name = "";
    String property = "";
    
    // used to check whether an input string is an prefix
    public PDDLPrefix() {
        allLogical();
        // allNUmerical();
    }
    
    public PDDLPrefix(String n, String p) {
        name = n;
        property = p;
    }
    
    private void allLogical() {
        logical.add("and");
        // logical.add("or");
        // logical.add("imply");
        // logical.add("forall");
        // logical.add("when");
        // logical.add("exists");
    }
    
    // private void allNUmerical() {
        // numerical.add("increase");
        // numerical.add("decrease");
        // numerical.add("assign");
    // }
    
    // return 1 for logical prefix
    // return 2 for numerical prefix
    // return 0 for none
    public int checkForPrefix(String input) {
        if(logical.contains(input)) 
            return 1;
        // if(numerical.contains(input))
        //     return 2;
        return 0;
    }
}
