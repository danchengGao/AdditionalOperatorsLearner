/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author danchengGao
 */
public class PDDLPredicate_logical extends PDDLPredicate {
    
    private String name;
    private ArrayList <StripsArgument> arguments = new ArrayList <> ();
    private boolean truthValue = true;
    // whether this predicate belongs to the first or second action during merging
    private boolean first_action = true;
    private boolean function_predicate = false;
    
    public PDDLPredicate_logical(String n) {
        name = n;
    }
    
    public PDDLPredicate_logical(String n, ArrayList <StripsArgument> a) {
        name = n;
        arguments = a;
    }
    
    // a copy constructor
    public PDDLPredicate_logical(PDDLPredicate_logical another) {
        this.name = another.name;
        for(StripsArgument a : another.arguments) {
        	StripsArgument arg = new StripsArgument(a);
        	this.arguments.add(arg);
        }        
        this.truthValue = another.truthValue;
        this.first_action = another.first_action;
        this.function_predicate = another.function_predicate;
    }
    
    @Override
    public PDDLPredicate_logical copy() {
        return new PDDLPredicate_logical(this);
    }
      
    public void setName(String n) {
        name = n;
    }
    
    public void addArgument(StripsArgument a) {
        arguments.add(a);
    }
    
    public void setTruthValue(boolean b) {
        truthValue = b;
    }
    
    public void setArgument(ArrayList <StripsArgument> a) {
        arguments = a;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean getTruthValue() {
        return truthValue;
    }
    
    public ArrayList<StripsArgument> getArgument() {
        return arguments;
    }
    
    public boolean getFirstAction() {
        return first_action;
    }
    
    @Override
    public void setFirstAction(boolean value) {
        first_action = value;
    }
    
    @Override
    public ArrayList<StripsArgument> get_all_arguments() {
        return arguments;
    }
    
    public void setAsFunction() {
        function_predicate = true;
    }
    
    // equals without checking truth value
    @Override
    public boolean equals(Object o) {
        if(o instanceof PDDLPredicate_package) {
            return false;
        }
    	// if the names are equal
        if(this.name.equals(((PDDLPredicate_logical)o).getName())) {
            // the predicate with the same name should have the same number of arguments
            ArrayList <StripsArgument> anotherArguments = ((PDDLPredicate_logical)o).getArgument();
            ArrayList<StripsArgument> one = new ArrayList<>(arguments);
            ArrayList<StripsArgument> two = new ArrayList<>(anotherArguments);
            return one.equals(two);
        }   
        // the names are not equal
        return false;
    }
    
    // equals with checking truth value
    // arg names must be equal
    public boolean equals_checking_truth_value(PDDLPredicate_logical o) {
        // if the names are equal, and truth values are equal
        if(this.name.equals(o.getName()) && (this.truthValue == o.getTruthValue())) {
            // the predicate with the same name should have the same number of arguments
            ArrayList <StripsArgument> anotherArguments = ((PDDLPredicate_logical)o).getArgument();
            ArrayList<StripsArgument> one = new ArrayList<>(arguments);
            ArrayList<StripsArgument> two = new ArrayList<>(anotherArguments);
            return one.equals(two);
        }   
        // the names are not equal
        return false;
    }
    
    // whether two predicates are the opposite to each other
    public boolean is_opposite(PDDLPredicate_logical o) {
        // if the names are equal
        if(this.name.equals(((PDDLPredicate_logical)o).getName())) {
            // the predicate with the same name should have the same number of arguments
            ArrayList <StripsArgument> anotherArguments = ((PDDLPredicate_logical)o).getArgument();
            ArrayList<StripsArgument> one = new ArrayList<>(arguments);
            ArrayList<StripsArgument> two = new ArrayList<>(anotherArguments);
            if(one.equals(two)) {
                return this.truthValue != o.truthValue;
            }
        }   
        // the names are not equal
        return false;
    }
    
    public String toStringFull(ArrayList<PDDLPredicate_logical> to_be_merged) {
    	if(this.name.equals("null")) {
    	    return "";
    	}
        
    	String toReturn = "(";
    	if(this.truthValue == true)
            toReturn = toReturn + this.name + " ";
    	else
    	    toReturn = toReturn + "not (" + this.name + " ";
        
        if(to_be_merged.isEmpty()) {
            for(StripsArgument a : arguments) {
                toReturn = toReturn + a.toString() + " ";
            }
            toReturn = toReturn.substring(0, toReturn.length()-1);
        }
        else {
            for(int i = 0; i < to_be_merged.size(); i ++) {
                for(int j = 0; j < arguments.size(); j ++) {
                    StripsArgument a_this = arguments.get(j);
                    StripsArgument a_to_be_merged = to_be_merged.get(i).getArgument().get(j);
                    if(!a_this.equals(a_to_be_merged)) {
                        a_this.addExtraType(a_to_be_merged.getType());
                        break;
                    }
                }              
            }
            
            for(StripsArgument a : arguments) {
                toReturn = toReturn + a.toString() + " ";
                a.clearExtraTypes();
            }
            toReturn = toReturn.substring(0, toReturn.length()-1);
        }
        
        if(this.truthValue == false)
            toReturn = toReturn + ")";        
        toReturn = toReturn + ")";  
        if(this.function_predicate == true)
            toReturn = toReturn + " - number";
        return toReturn;
    }
    
    @Override
    public String toString() {
    	if(this.name.equals("null")) {
    	    return "";
    	}
    	String toReturn = "(";
    	if(this.truthValue == true)
            toReturn = toReturn + this.name + " ";
    	else
    	    toReturn = toReturn + "not (" + this.name + " ";
        for(StripsArgument a : arguments) {
            toReturn = toReturn + a.toStringWithoutType();
        }
        toReturn = toReturn.substring(0, toReturn.length()-1);
        if(this.truthValue == false)
            toReturn = toReturn + ")";
        toReturn = toReturn + ")"; 
        if(this.function_predicate == true)
            toReturn = toReturn + " - number";
        return toReturn;
    }
    
    @Override
    public String toString_PDDL(String indent) {
        if(this.name.equals("null")) {
            return "";
        }
        String toReturn = indent + "    (";
        if(this.truthValue == true)
            toReturn = toReturn + this.name + " ";
        else
            toReturn = toReturn + "not (" + this.name + " ";
        for(StripsArgument a : arguments) {
            toReturn = toReturn + a.toStringWithoutType();
        }
        toReturn = toReturn.substring(0, toReturn.length()-1);
        if(this.truthValue == false)
            toReturn = toReturn + ")";
        toReturn = toReturn + ")"; 
        if(this.function_predicate == true)
            toReturn = toReturn + " - number";
        return toReturn;
    }
    
    @Override
    public void negate() {
        boolean new_truth_value = !truthValue;
        this.setTruthValue(new_truth_value);
    }
    
    public void add_round_number_to_all_arguments(int round) {
        for(StripsArgument a : this.arguments) {
            a.setName(a.getName() + "_" + round);
        } 
    }
    
    @Override
    public void replace_argument_names(String original_name, String new_name) {
        for(StripsArgument a : this.arguments) {
            if(a.getName().equals(original_name)) {
                a.setName(new_name);
            }
        }
    }
    
    @Override
    public void replace_argument(StripsArgument original_arg, StripsArgument new_arg) {
        ArrayList<StripsArgument> newArgumentList = new ArrayList<>();
        for(StripsArgument a : this.arguments) {
            if(a.equals(original_arg)) {
                a = new_arg;                
            }
            newArgumentList.add(a);            
        } 
        this.arguments = newArgumentList;
    }
    
    // check whether the predicate contains an argument a
    // argument must be of the same name
    @Override
    public boolean contains_argument(StripsArgument a) {
        return this.getArgument().contains(a);
    }
    
    public PDDLPredicate merge_simple(PDDLPredicate another, boolean precon) {
        ArrayList<PDDLPredicate> list = new ArrayList<>();
        PDDLPredicate_package merged = new PDDLPredicate_package("and", list);
        list.add(this);
        list.add(another);
        merged.simplify_and(precon);
        return merged;
    }
    
    // equals with different argument names
    public boolean comparePredicate_logical(PDDLPredicate_logical another) {
        TreeMap<String, TreeMap<String, Integer>> thisMap = new TreeMap<>();
        TreeMap<String, TreeMap<String, Integer>> anotherMap = new TreeMap<>();
        // construct the map for this predicate
        // use predicate name as key
        // if predicate is false add a not at the back of the name
        String name = this.getName();              
        if(this.getTruthValue() == false) {
            name = name + "not"; 
        }
        // each predicate will have a map
        // map argument type name to the number of this type in the arguments of this predicate
        TreeMap<String, Integer> typeNumberMap = new TreeMap<>();
        for(StripsArgument a : this.getArgument()) {
            StripsType t = a.getType();
            String typeName = t.getName();
            if(typeNumberMap.containsKey(typeName)) {
                typeNumberMap.put(typeName, typeNumberMap.get(typeName) + 1);
            }
            else {
                typeNumberMap.put(typeName, 1);
            } 
        }
        thisMap.put(name, typeNumberMap);
        
        // construct the map for another predicate
        String another_name = another.getName();
        if(another.getTruthValue() == false) {
            another_name = another_name + "not"; 
        }
        TreeMap<String, Integer> another_typeNumberMap = new TreeMap<>();
        for(StripsArgument a : another.getArgument()) {
            StripsType t = a.getType();
            String typeName = t.getName();
            if(!another_typeNumberMap.containsKey(typeName)) {
                another_typeNumberMap.put(typeName, 1);
            }
            else {
                another_typeNumberMap.put(typeName, another_typeNumberMap.get(typeName) + 1);
            } 
        }
        anotherMap.put(another_name, another_typeNumberMap);
        
        return thisMap.equals(anotherMap);
    }
    
    // 0 : same type conflict, 1 : former is subtype conflict, 2 : latter is subtype conflict, 3 : no conflict, 4 : same first_action conflict
    @Override
    public int check_for_conflict(PDDLPredicate_logical another, boolean different_arg_names) {
        // if this is the former action
        if(this.getFirstAction()) {            
            int result = -1;
            // false
            if(!different_arg_names) {
                result = this.check_for_conflict_with_the_same_arg_name(another);
            }
            // true
            else {
                result = this.check_for_conflict_with_different_arg_names(another);
            }
            
            if(result == 3) {
                return result;
            }
            
            // same first_action
            if(another.getFirstAction()) {
                return 4;    
            }
            else {
                return result;
            }
        }
        
        // if this is the latter action
        else {
            int result = -1;
            // false
            if(!different_arg_names) {
                result = another.check_for_conflict_with_the_same_arg_name(this);
            }
            // true
            else {
                result = another.check_for_conflict_with_different_arg_names(this);
            }
            
            if(result == 3) {
                return result;
            }
            
            // same first_action
            if(!another.getFirstAction()) {
                return 4;    
            }
            else {
                return result;
            }
        }
        
    }
    
    // check whether two simple predicates are conflicts to each other
    // conflict if :
    //    same name, arguments same name, opposite truth value
    // 0 : same type conflict, 1 : this is subtype conflict, 2 : another is subtype conflict, 3 : no conflict
    private int check_for_conflict_with_the_same_arg_name(PDDLPredicate_logical another) {
        // if equal name & opposite truth value
        if(this.getTruthValue() != another.getTruthValue() && this.getName().equals(another.getName())) {
            ArrayList<StripsArgument> one_arg_list = this.getArgument();
            ArrayList<StripsArgument> another_arg_list = another.getArgument();
            if(one_arg_list.size() == another_arg_list.size()) {
                // if the two lists are the same
                if(check_argument_list_equality_same_name(one_arg_list, another_arg_list)) {
                    return 0;
                }
                // if they are not the same
                // check for subtypes
                // if there is a subtype then conflict is true
                else {
                    for(int i = 0; i < one_arg_list.size(); i ++) {
                        StripsArgument a = one_arg_list.get(i);
                        StripsArgument b = another_arg_list.get(i);
                        if(a.getType().isSubtype(b.getType())) {                                
                            return 1;
                        }
                        else if(b.getType().isSubtype(a.getType())) {
                            return 2;
                        }
                    }
                }
            }
            else {
                return 3;
            }
            
        }
        return 3;
    }
    
    // check whether two simple predicates are conflicts to each other
    // conflict if :
    //    same name, arguments same type, opposite truth value
    //    same name, one is a subtype of another, opposite truth value
    // 0 : same type conflict, 1 : this is subtype conflict, 2 : another is subtype conflict, 3 : no conflict
    private int check_for_conflict_with_different_arg_names(PDDLPredicate_logical another) {
        // if equal name & opposite truth value
        if(this.getTruthValue() != another.getTruthValue() && this.getName().equals(another.getName())) {
            ArrayList<StripsArgument> one_arg_list = this.getArgument();
            ArrayList<StripsArgument> another_arg_list = another.getArgument();
            if(one_arg_list.size() == another_arg_list.size()) {
                // if the two lists are the same
                if(check_argument_list_equality_different_name(one_arg_list, another_arg_list)) {
                    return 0;
                }
                // if they are not the same
                // check for subtypes
                // if there is a subtype then conflict is true
                else {
                    for(int i = 0; i < one_arg_list.size(); i ++) {
                        StripsArgument a = one_arg_list.get(i);
                        StripsArgument b = another_arg_list.get(i);
                        if(a.getType().isSubtype(b.getType())) {                                
                            return 1;
                        }
                        else if(b.getType().isSubtype(a.getType())) {
                            return 2;
                        }
                    }
                }
            }
            else {
                return 3;
            }
            
        }
        return 3;
    }
    
    // check equality
    // name must be the same
    private boolean check_argument_list_equality_same_name(ArrayList<StripsArgument> arg1, ArrayList<StripsArgument> arg2) {
        ArrayList<StripsArgument> one = new ArrayList<>(arg1);
        ArrayList<StripsArgument> two = new ArrayList<>(arg2);        
        if(one.size() == two.size()) {
            boolean found_inequality = false;
            for(int i = 0; i < one.size(); i ++) {
                if(one.get(i).equals(two.get(i))) {
                } else {
                    found_inequality = true;
                    break;
                }
            }
            if(found_inequality) {
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    // check equality
    // name can be different
    private boolean check_argument_list_equality_different_name(ArrayList<StripsArgument> arg1, ArrayList<StripsArgument> arg2) {
        ArrayList<StripsArgument> one = new ArrayList<>(arg1);
        ArrayList<StripsArgument> two = new ArrayList<>(arg2);        
        if(one.size() == two.size()) {
            TreeMap<String, Integer> thisMap = new TreeMap<>();
            TreeMap<String, Integer> anotherMap = new TreeMap<>();
            for(StripsArgument a : one) {
                StripsType p = a.getType();
                if(thisMap.containsKey(p.getName())) {
                    thisMap.put(p.getName(), thisMap.get(p.getName()) + 1);
                }
                else {
                    thisMap.put(p.getName(), 1);
                }
            }
            for(StripsArgument a : two) {
                StripsType p = a.getType();
                if(anotherMap.containsKey(p.getName())) {
                    anotherMap.put(p.getName(), anotherMap.get(p.getName()) + 1);
                }
                else {
                    anotherMap.put(p.getName(), 1);
                }
            }
            return thisMap.equals(anotherMap);
        }
        return false;
    }
    
    @Override
    public boolean check_this_validity_by_name() {
        if(this.getName().equals("")) {
            return false;
        }
        else {
            return true;
        }
    }
    
    // true : contains (not (= ?a ?a))
    // false: does not contain
    @Override
    public boolean cotains_not_equal_same_arg() {
        //System.out.println(this);
        if(!this.getTruthValue() && this.getName().equals("=")) {
            // there are two args
            // check whether they are equal to each other
            if (arguments.get(0).equals(arguments.get(1))) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int get_number_of_predicate_logical() {
        return 1;
    }

}
