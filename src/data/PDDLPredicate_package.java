/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author danchengGao
 */
public class PDDLPredicate_package extends PDDLPredicate {
    
    private String prefix;
    // whether the prefix is logical
    private boolean logical = true;
    
    private ArrayList<PDDLPredicate> predicateList = new ArrayList<>();  
    
    public PDDLPredicate_package(String p) {
        prefix = p;
    }
    
    public PDDLPredicate_package(String p, ArrayList<PDDLPredicate> list) {
        prefix = p;
        predicateList = list;
    }
    
    // a copy constructor
    public PDDLPredicate_package(PDDLPredicate_package another) {
        this.prefix = another.prefix;
        this.logical = another.logical;
        for(PDDLPredicate p : another.predicateList) {
            if(p instanceof PDDLPredicate_logical) {
                PDDLPredicate_logical pre = new PDDLPredicate_logical((PDDLPredicate_logical)p);
                this.predicateList.add(pre);
            }
            else if (p instanceof PDDLPredicate_package) {
                PDDLPredicate_package pre = new PDDLPredicate_package((PDDLPredicate_package)p);
                this.predicateList.add(pre);
            }
            else {
                PDDLPredicate_number pre = new PDDLPredicate_number((PDDLPredicate_number)p);
                this.predicateList.add(pre);
            }
        } 
    }
    
    @Override
    public PDDLPredicate_package copy() {
        return new PDDLPredicate_package(this);
    }
    
    public void setLogical(boolean b) {
        logical = b;
    }
    
    public void addPredicate(PDDLPredicate p) {
        predicateList.add(p);
    }
    
    public void setPredicate(ArrayList<PDDLPredicate> p) {
        predicateList = p;
    }
    
    public void removePredicate(PDDLPredicate p) {
        ArrayList<PDDLPredicate> newList = new ArrayList<>();
        for(PDDLPredicate pre : predicateList) {
            if(pre instanceof PDDLPredicate_logical && p instanceof PDDLPredicate_logical) {
                if(!((PDDLPredicate_logical) pre).equals_checking_truth_value((PDDLPredicate_logical) p)) {
                    newList.add(pre);
                }                
            }
            else if(pre instanceof PDDLPredicate_package && p instanceof PDDLPredicate_package) {
                if(!((PDDLPredicate_package) pre).equals((PDDLPredicate_package) p)) {
                    newList.add(pre);
                }
            }
            else if(pre instanceof PDDLPredicate_package && p instanceof PDDLPredicate_logical) {
                ((PDDLPredicate_package) pre).removePredicate(p);
            }
            else {
                newList.add(pre);
            }
        }
        this.setPredicate(newList);
    }

    public void removePredicate_logical(int index_to_remove) {
        ArrayList<PDDLPredicate> newList = new ArrayList<>();
        int counter = -1;
        boolean remove_completed = false;
        for(PDDLPredicate pre : predicateList) {
            counter = counter + pre.get_number_of_predicate_logical();
            if(counter < index_to_remove || remove_completed) {
                newList.add(pre);
            }
            else {
                if(pre instanceof PDDLPredicate_package) {
                    int index = counter - pre.get_number_of_predicate_logical();
                    ((PDDLPredicate_package) pre).removePredicate_logical(index);
                    newList.add(pre);
                }
                remove_completed = true;
            }
        }
        this.setPredicate(newList);
    }
    
    public void setPrefix(String s) {
        prefix = s;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public boolean isLogical() {
        return logical;
    }
    
    public ArrayList<PDDLPredicate> getPredicateList() {
        return predicateList;
    }
    
    @Override
    public void setFirstAction(boolean value) {
        if(!predicateList.isEmpty()) {
            for(PDDLPredicate p : predicateList) {
                p.setFirstAction(value);
            }
        }
    }
    
    // get all arguments of all predicates
    @Override
    public ArrayList<StripsArgument> get_all_arguments() {
        ArrayList<StripsArgument> toReturn = new ArrayList<>();
        for(PDDLPredicate p : this.getPredicateList()) {
            ArrayList<StripsArgument> p_arg = p.get_all_arguments();
            for(StripsArgument a : p_arg) {
                if(!toReturn.contains(a)) {
                    toReturn.add(a);
                }
            }
        }
        return toReturn;
    }
    
    @Override
    public String toString() {
        String toReturn = "(" + prefix;
        
        toReturn = toReturn + "\n";
        
        toReturn = toReturn + "    ";
        
        for(PDDLPredicate p : predicateList) {
            toReturn = toReturn + p + "\n";
        }
        toReturn = toReturn + ")";
        return toReturn;
    }
    
    @Override
    public String toString_PDDL(String indent) {
        String toReturn = indent + "    " + "(" + prefix;
        if(!logical) {
            toReturn = toReturn + " ";
            toReturn = toReturn + " ";
            for(PDDLPredicate p : predicateList) {                
                toReturn = toReturn + p;
            }
            
            toReturn = toReturn + ")";
            return toReturn;
        }
        else {            
            toReturn = toReturn + "\n";
            
            for(PDDLPredicate p : predicateList) {                
                toReturn = toReturn + p.toString_PDDL(indent + "    ");
                toReturn = toReturn + "\n";
            }
            
            toReturn = toReturn + indent + "    " + ")";
            return toReturn;
        }
    }
    
    @Override
    public void negate() {
        if(this.isLogical()) {
            for(PDDLPredicate p : predicateList) {
                p.negate();
            } 
        }
        
    }
    
    public void add_round_number_to_all_arguments(int round) {
        for(PDDLPredicate p : predicateList) {
            if(p instanceof PDDLPredicate_logical) {
                ((PDDLPredicate_logical) p).add_round_number_to_all_arguments(round);
            }
            else if(p instanceof PDDLPredicate_package) {
                ((PDDLPredicate_package) p).add_round_number_to_all_arguments(round);
            }
        }
    }
    
    public void add_round_number_to_all_arguments_with_exceptions(int round, ArrayList<String> exceptions) {
        for(PDDLPredicate p : predicateList) {
            if(p instanceof PDDLPredicate_logical) {
                for (StripsArgument a : ((PDDLPredicate_logical) p).getArgument()) {
                    if(!exceptions.contains(a.getName())) {
                        a.setName(a.getName() + "_" + round);
                    }
                }                    
            }
            else if(p instanceof PDDLPredicate_package) {
                ((PDDLPredicate_package) p).add_round_number_to_all_arguments_with_exceptions(round, exceptions);
            }
        }        
    }
    
    // get all (= ?a ?b)
    // only return when the prefix is and
    // don't remove them //remove these equal predicates
    public ArrayList<PDDLPredicate_logical> get_equal_logical_predicate() {
        ArrayList<PDDLPredicate_logical> toReturn = new ArrayList<>();
        if(this.prefix.equals("and")) {
            for(PDDLPredicate p : predicateList) {
                if(p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getName().equals("=") && ((PDDLPredicate_logical) p).getTruthValue()) {
                   toReturn.add((PDDLPredicate_logical) p);
                }
            }
        }
        return toReturn;
    }

    public void remove_equal_predicates() {
        ArrayList<PDDLPredicate> newList  = new ArrayList<>();
        if(this.prefix.equals("and")) {
            for(PDDLPredicate p : predicateList) {
                if(p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getName().equals("=") && ((PDDLPredicate_logical) p).getTruthValue()) {

                }
                else {
                    newList.add(p);
                }
            }
            this.predicateList = newList;
        }
    }
    
    @Override
    public void replace_argument_names(String original_name, String new_name) {
        for(PDDLPredicate pre : predicateList) {
            if(pre instanceof PDDLPredicate_logical) {
                ((PDDLPredicate_logical) pre).replace_argument_names(original_name, new_name);
            }
            else if(pre instanceof PDDLPredicate_package) {
                ((PDDLPredicate_package) pre).replace_argument_names(original_name, new_name);
            }
        }
    }
    
    @Override
    public void replace_argument(StripsArgument original_arg, StripsArgument new_arg) {
        for(PDDLPredicate pre : predicateList) {
            if(pre instanceof PDDLPredicate_logical) {
                ((PDDLPredicate_logical) pre).replace_argument(original_arg, new_arg);
            }
            else if(pre instanceof PDDLPredicate_package) {
                ((PDDLPredicate_package) pre).replace_argument(original_arg, new_arg);
            }
        }
    }
    
    // simply join two packs together using an "and"
    // simplify "and" 
    public PDDLPredicate merge_simple(PDDLPredicate another, boolean precon) {
        ArrayList<PDDLPredicate> list = new ArrayList<>();
        PDDLPredicate_package merged = new PDDLPredicate_package("and", list);
        list.add(this);
        list.add(another);
        merged.simplify_and(precon);
        return merged;
    }
    
    // return the simplified version of the package
    public PDDLPredicate simplify(boolean precon) {
        //System.out.println(this.toString_PDDL(""));
        if(this.prefix.equals("and")) {
            PDDLPredicate temp = this.simplify_and(precon);
            if(temp != null)
                return temp;
            else 
                return this;
        }
        return this;
    }
    
    // to check: predicate list
    // if size = 1 & has only one predicate, return the predicate instead
    public PDDLPredicate simplify_and(boolean precon) {
        for(PDDLPredicate p : this.predicateList) {
            if(p instanceof PDDLPredicate_package) {
                ((PDDLPredicate_package) p).simplify(precon);
            }
        }
        this.setPredicate(merge_and(predicateList, precon));
        if(this.predicateList.size() == 1) {
            return this.predicateList.get(0);
        }
        return null;
    }
    
    // merge consecutive "and"s: (and A) ... (and B) -> A ... B
    // remove duplicates
    public ArrayList<PDDLPredicate> merge_and(ArrayList<PDDLPredicate> original, boolean precon) {
        ArrayList<PDDLPredicate> newList = new ArrayList<>();
        for(PDDLPredicate p : original) {
            if(p instanceof PDDLPredicate_package) {
                if(((PDDLPredicate_package)p).getPrefix().equals("and")) {
                    for(PDDLPredicate p1 : ((PDDLPredicate_package) p).getPredicateList()) {
                        newList.add(p1);
                    }
                }
                else {
                    newList.add(p);
                }
            }
            else {
                newList.add(p);
            }
        }
        // remove duplicate predicate_logical
        ArrayList<PDDLPredicate> finalList = remove_duplicate_predicate_logical(newList, precon);
        return finalList;
    }
    
    // used in merge_and and merge_or
    public ArrayList<PDDLPredicate> remove_duplicate_predicate_logical(ArrayList<PDDLPredicate> original, boolean precon) {
        ArrayList<PDDLPredicate> toReturn = new ArrayList<>();
        for(int i = 0; i < original.size(); i ++) {
            if(original.get(i) instanceof PDDLPredicate_logical) {
                if(!toReturn.contains(original.get(i))) {
                    toReturn.add(original.get(i));
                }
                else {
                    boolean present = false;
                    boolean update_first_action = false;
                    for(PDDLPredicate p : toReturn) {
                        if(p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).equals((PDDLPredicate_logical) original.get(i))) {
                            // if their truth values are the same
                            if( ((PDDLPredicate_logical) p).getTruthValue() ==  ((PDDLPredicate_logical) original.get(i)).getTruthValue() ) {
                                present = true;
                                // if found two equals
                                if(precon && !((PDDLPredicate_logical) p).getFirstAction() && ((PDDLPredicate_logical) original.get(i)).getFirstAction()) {
                                    update_first_action = true;
                                }
                                if(!precon) present = false;
                                break;
                            }
                        }
                    }
                    if(!present) {
                        toReturn.add(original.get(i));
                    }
                    if(update_first_action) {
                        toReturn.add(original.get(i));
                    }
                }
            }
            else {
                toReturn.add(original.get(i));
            }
        }
        return toReturn;
    }
    
    // check equality of two condition list
    // same arg names can not have different names
    // size = 1; either a predicate or a package "and"
    private boolean check_condition_list_equality(ArrayList<PDDLPredicate> con1, ArrayList<PDDLPredicate> con2) {
        // if both individual predicate
        if( (con1.get(0) instanceof PDDLPredicate_logical) &&  (con2.get(0) instanceof PDDLPredicate_logical)) {
            return ( ((PDDLPredicate_logical) (con1.get(0))).equals_checking_truth_value( (PDDLPredicate_logical) (con2.get(0)) ) );
        }
        // if both package
        if( (con1.get(0) instanceof PDDLPredicate_package) &&  (con2.get(0) instanceof PDDLPredicate_package)) {
            ArrayList<PDDLPredicate> pre1 = ((PDDLPredicate_package)(con1.get(0))).getPredicateList();
            ArrayList<PDDLPredicate> pre2 = ((PDDLPredicate_package)(con2.get(0))).getPredicateList();
            return check_predicate_list_equality(pre1, pre2);
        }
        return false;
    }
    
    // check equality of two predicate list
    // same arg names can not have different names
    // check size; separate logical & package & number
    // used in check_condition_list_equality && equals
    private boolean check_predicate_list_equality(ArrayList<PDDLPredicate> pre1, ArrayList<PDDLPredicate> pre2) {
        if(pre1.size() == pre2.size()) {
            ArrayList<PDDLPredicate_logical> one_logical = new ArrayList<>();
            ArrayList<PDDLPredicate_logical> another_logical = new ArrayList<>();
            ArrayList<PDDLPredicate_package> one_package = new ArrayList<>();
            ArrayList<PDDLPredicate_package> another_package = new ArrayList<>();
            // separate package & individual predicates
            for(PDDLPredicate pre : pre1) {
                if(pre instanceof PDDLPredicate_logical) {
                    one_logical.add((PDDLPredicate_logical) pre);
                }
                else if(pre instanceof PDDLPredicate_package) {
                    one_package.add((PDDLPredicate_package) pre);
                }
            }
            for(PDDLPredicate pre : pre2) {
                if(pre instanceof PDDLPredicate_logical) {
                    another_logical.add((PDDLPredicate_logical) pre);
                }
                else if(pre instanceof PDDLPredicate_package) {
                    another_package.add((PDDLPredicate_package) pre);
                }
            }
            // compare logical list to logical list, package list to package list, number list to number list
            if(check_predicate_logic_list_equality(one_logical, another_logical) && check_predicate_package_list_equality(one_package, another_package)) {
                return true;
            }
        }
        
        return false;
    }
    
    // used in check_predicate_list_equality
    private boolean check_predicate_logic_list_equality(ArrayList<PDDLPredicate_logical> one, ArrayList<PDDLPredicate_logical> another) {
        if(one.size() == 0 && another.size() == 0) {
            return true;
        }
        if(one.size() == another.size()) {
            out: for(int i = 0; i < one.size(); i ++) {
                in : for(int j = 0; j < another.size(); j ++) {
                    if(one.get(i).equals_checking_truth_value(another.get(j))) { 
                        continue out;
                    }
                    // equal predicate not found until the end of the list
                    if(j == another.size() - 1) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    // used in check_predicate_list_equality
    private boolean check_predicate_package_list_equality(ArrayList<PDDLPredicate_package> one, ArrayList<PDDLPredicate_package> another) {
        if(one.size() == 0 && another.size() == 0) {
            return true;
        }
        if(one.size() == another.size()) {
            out: for(int i = 0; i < one.size(); i ++) {
                in : for(int j = 0; j < another.size(); j ++) {
                    if(one.get(i).equals(another.get(j))) { 
                        continue out;
                    }
                    // equal predicate not found until the end of the list
                    if(j == another.size() - 1) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    // check whether the predicate contains an argument a
    // argument must be of the same name
    @Override
    public boolean contains_argument(StripsArgument a) {
        for(PDDLPredicate p : this.getPredicateList()) {
            if(p.contains_argument(a)) {
                return true;
            }
        }
        return false;
    }
    
    // whether two packages are equal
    // equal: same arg list, con list & pre list (names for args are equal too)
    // and : predicate list same, return true
    @Override
    public boolean equals(Object o) {
        if(o instanceof PDDLPredicate_logical) {
            return false;
        }
        if(this.prefix.equals(((PDDLPredicate_package)o).getPrefix())) {
            if(this.prefix.equals("and")) {
                return check_predicate_list_equality(this.getPredicateList(), ((PDDLPredicate_package)o).getPredicateList());
            }
        }
        return false;
    }    
    
    // return true if the two predicate_packages are the same
    // used for same arg different names (compare across diff actions)
    // and : check predicate list
    public boolean comparePredicate_package(PDDLPredicate_package another) {
        if(this.prefix.equals(another.prefix)) {
            ArrayList<PDDLPredicate_logical> one_logical = new ArrayList<>();
            ArrayList<PDDLPredicate_logical> another_logical = new ArrayList<>();
            ArrayList<PDDLPredicate_package> one_package = new ArrayList<>();
            ArrayList<PDDLPredicate_package> another_package = new ArrayList<>();
            // separate package & individual predicates
            for(PDDLPredicate pre : this.getPredicateList()) {
                if(pre instanceof PDDLPredicate_logical) {
                    one_logical.add((PDDLPredicate_logical) pre);
                }
                else if(pre instanceof PDDLPredicate_package && ((PDDLPredicate_package) pre).isLogical()) {
                    one_package.add((PDDLPredicate_package) pre);
                }
            }
            for(PDDLPredicate pre : another.getPredicateList()) {
                if(pre instanceof PDDLPredicate_logical) {
                    another_logical.add((PDDLPredicate_logical) pre);
                }
                else if(pre instanceof PDDLPredicate_package && ((PDDLPredicate_package) pre).isLogical()) {
                    another_package.add((PDDLPredicate_package) pre);
                }
            }
            
            if(this.prefix.equals("and")) {
               if(comparePredicateLists_package(one_package, another_package) && comparePredicateLists_logical(one_logical, another_logical)) {
                   return true;
               }
               else {
                   return false;
               }
            }
        }
        return false;
    }
    
    private boolean comparePredicateLists_package(ArrayList<PDDLPredicate_package> one, ArrayList<PDDLPredicate_package> another) {
        if(one.size() == 0 && another.size() == 0) {
            return true;
        }
        if(one.size() == another.size()) {
            ArrayList<Integer> numberList = new ArrayList<Integer>();
            for(PDDLPredicate_package pre : one) {
                boolean matched = false;
                for(int i = 0; i < another.size(); i ++) {
                    if(!numberList.contains(i) && another.get(i).getPrefix().equals(pre.getPrefix()) && pre.comparePredicate_package(another.get(i))) {
                        matched = true;
                        numberList.add(i);
                        break;
                    }
                    if (i == another.size() - 1 && !matched){                       
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    // return true if the two predicate_logical lists are the same
    // used for same arg different names
    private boolean comparePredicateLists_logical(ArrayList<PDDLPredicate_logical> one, ArrayList<PDDLPredicate_logical> another) {
        if(one.size() == 0 && another.size() == 0) {
            return true;
        }
        ArrayList<PDDLPredicate_logical> thisList = new ArrayList<>();
        ArrayList<PDDLPredicate_logical> anotherList = new ArrayList<>();
        for(PDDLPredicate_logical p : one) {
            if(!p.getName().equals("null")) {
                thisList.add(p);
            }
        }
        for(PDDLPredicate_logical p : another) {
            if(!p.getName().equals("null")) {
                anotherList.add(p);
            }
        }
        if(thisList.size() == anotherList.size()) {
            TreeMap<String, ArrayList<TreeMap<String, Integer>>> thisMap = new TreeMap<>();
            TreeMap<String, ArrayList<TreeMap<String, Integer>>> anotherMap = new TreeMap<>();
            // construct the map for this list
            for(PDDLPredicate_logical p : thisList) {
                // use predicate name as key
                // if predicate is false add a not at the back of the name
                String name = p.getName();              
                if(p.getTruthValue() == false) {
                    name = name + "not"; 
                }  
                // each predicate will have a map
                // map argument type name to the number of this type in the arguments of this predicate
                TreeMap<String, Integer> typeNumberMap = new TreeMap<>();
                for(StripsArgument a : p.getArgument()) {
                    StripsType t = a.getType();
                    String typeName = t.getName();
                    if(typeNumberMap.containsKey(typeName)) {
                        typeNumberMap.put(typeName, typeNumberMap.get(typeName) + 1);
                    }
                    else {
                        typeNumberMap.put(typeName, 1);
                    } 
                }
                // each predicate has a list of type-number map
                // because different predicates with the same name can appear : (move a b)(move b c)
                if(thisMap.containsKey(name)) {
                    thisMap.get(name).add(typeNumberMap);
                }
                else {
                    ArrayList<TreeMap<String, Integer>> argTypeList = new ArrayList<TreeMap<String, Integer>>();
                    argTypeList.add(typeNumberMap);
                    thisMap.put(name, argTypeList);
                }               
            }
            // construct the map for another list
            for(PDDLPredicate_logical p : anotherList) {
                String name = p.getName();
                if(p.getTruthValue() == false) {
                    name = name + "not"; 
                }
                
                TreeMap<String, Integer> typeNumberMap = new TreeMap<>();
                for(StripsArgument a : p.getArgument()) {
                    StripsType t = a.getType();
                    String typeName = t.getName();
                    if(typeNumberMap.containsKey(typeName)) {
                        typeNumberMap.put(typeName, typeNumberMap.get(typeName) + 1);
                    }
                    else {
                        typeNumberMap.put(typeName, 1);
                    }
                }
                
                if(anotherMap.containsKey(name)) {
                    anotherMap.get(name).add(typeNumberMap);
                }
                else {
                    ArrayList<TreeMap<String, Integer>> argTypeList = new ArrayList<>();
                    argTypeList.add(typeNumberMap);
                    anotherMap.put(name, argTypeList);
                }           
            }
            return mapEqual(thisMap, anotherMap);
        }
        return false;
    }
    
    // test whether two maps are equal
    // ignore the order of the array list
    private boolean mapEqual(TreeMap<String, ArrayList<TreeMap<String, Integer>>> one, TreeMap<String, ArrayList<TreeMap<String, Integer>>> another) {
        boolean equal = true;
        if (one.entrySet().size() != another.entrySet().size()) {
            return false;
        }
        out: for(Map.Entry <String, ArrayList<TreeMap<String, Integer>>> entry1 : one.entrySet()) {
            ArrayList<TreeMap<String, Integer>> list1 = entry1.getValue();
            ArrayList<TreeMap<String, Integer>> list2 = another.get(entry1.getKey());
            // if there are two predicates with the same name in two maps
            if(list2 != null){
                if(list1.size() != list2.size()) {
                    return false;
                }
                else {
                    // compare two list
                    // assess equality ignoring list order
                    // copy list 2 to avoid the same element being used twice
                    ArrayList<TreeMap<String, Integer>> list2Copy = new ArrayList<>(list2);
                    in : for(TreeMap<String, Integer> map1 : list1) {
                        for(TreeMap<String, Integer> map2 : list2) {
                            // equal value found
                            // continue to next list value
                            if(map1.equals(map2)) {
                                // if this element has not been used before
                                if(list2Copy.contains(map2)) {
                                    list2Copy.remove(map2);
                                    //System.out.println(list2Copy);
                                    continue in;
                                }                               
                            }
                            // equal value not found until end of entry 2 list
                            // return false
                            if(list2.indexOf(map2) == list2.size() - 1) {
                                equal = false;
                                break out;
                            }
                        }
                    }                                           
                }
            }
            else {
                return false;
            }
        }
        return equal;
    }
    
    // whether this package contains a logical predicate
    // only check predicate list
    // (used by precondition)
    public PDDLPredicate_logical contains(PDDLPredicate_logical another) {
        if(this.isLogical()) {
            for(int i = 0; i < this.predicateList.size(); i ++) {
                PDDLPredicate p = this.predicateList.get(i);
                if(p instanceof PDDLPredicate_logical) {
                    if(((PDDLPredicate_logical) p).equals_checking_truth_value(another)) {
                        return (PDDLPredicate_logical) p;
                    }
                }
                else if(p instanceof PDDLPredicate_package && ((PDDLPredicate_package) p).isLogical()) {
                    PDDLPredicate_logical returned = ((PDDLPredicate_package) p).contains(another);
                    // equal predicate not found
                    if(returned.getName().equals("")) {
                        
                    }
                    else {
                        return returned;
                    }
                }
            }
        }
        return new PDDLPredicate_logical("");
    }
    
    
    // remove duplicating predicates with another PDDLPredicate from this package
    // within an action, so arg names are the same for equal predicates
    // and : check predicate list
    // return new another
    // if precon & effect from same sub action: remove effect
    // if precon & effect from different sub actions :
    //    if precon first & effect second : remove effect
    //    if precon second & effect first : remove precon
    public PDDLPredicate remove_duplicate(PDDLPredicate another, boolean precon) {      
        if(another instanceof PDDLPredicate_logical) {   
            ArrayList<PDDLPredicate> newList = new ArrayList<>();
            PDDLPredicate new_another = another;
            for(int i = 0; i < this.predicateList.size(); i ++) {
                PDDLPredicate p = this.predicateList.get(i);
                // only one possible duplicate since duplicating predicates have been removed before
                if(p instanceof PDDLPredicate_logical) {
                    if(!((PDDLPredicate_logical) p).equals_checking_truth_value((PDDLPredicate_logical) another)) {
                        newList.add(p);
                    }
                    else {
                        // if precon & effect from same action: keep them
                        if(((PDDLPredicate_logical) p).getFirstAction() == ((PDDLPredicate_logical) another).getFirstAction()) {
                            newList.add(p);
                        }
                        // if precon second & effect first : remove precon
                        if(((PDDLPredicate_logical) p).getFirstAction() && !((PDDLPredicate_logical) another).getFirstAction()) {
                            newList.add(p);
                            new_another = new PDDLPredicate_logical("");
                        }
                        // if precon first & effect second : remove effect
                    }
                }
                else if(p instanceof PDDLPredicate_package && ((PDDLPredicate_package) p).isLogical()) {                  
                    new_another = ((PDDLPredicate_package) p).remove_duplicate(another, precon);
                    newList.add(p);
                }
                else if(p instanceof PDDLPredicate_package && !((PDDLPredicate_package) p).isLogical()){
                    newList.add(p);
                }
            }
            this.setPredicate(newList);
            return new_another;
        }
        
        else if(another instanceof PDDLPredicate_package){            
            PDDLPredicate_package new_another = (PDDLPredicate_package) another;
            ArrayList<PDDLPredicate> new_another_predicate = new ArrayList<>();
            
            for(int i = 0; i < ((PDDLPredicate_package) another).predicateList.size(); i ++) {
                PDDLPredicate p = ((PDDLPredicate_package) another).predicateList.get(i);
                PDDLPredicate returned = this.remove_duplicate(p, precon);
                
                if(returned instanceof PDDLPredicate_logical) {
                    if(!((PDDLPredicate_logical) returned).getName().equals("")) {
                        new_another_predicate.add(returned);
                    }
                }
                else if(returned instanceof PDDLPredicate_package) {
                    new_another_predicate.add(returned);
                }
                else {
                    new_another_predicate.add(returned);
                }
            }
            new_another.setPredicate(new_another_predicate);
            return new_another;
        }
        
        return another;
    }

    // check whether this package contains conflict with another simple predicate
    // 0 : same type conflict, 1 : former is subtype conflict, 2 : latter is subtype conflict, 3 : no conflict, 4 : same first_action conflict
    // 5 : conflict in condition
    @Override
    public int check_for_conflict(PDDLPredicate_logical another, boolean different_arg_names) {
        if(this.isLogical()) {
            for(PDDLPredicate p : this.getPredicateList()) {
                int conflict_value = p.check_for_conflict(another, different_arg_names);
                if(conflict_value == 0 || conflict_value == 1 || conflict_value == 2 || conflict_value == 4) {
                    return conflict_value;
                }
            }
        }
        else {
            return 3;
        }
        return 3;
    }
    
    @Override
    public boolean check_this_validity_by_name() {
        if(this.getPrefix().equals("")) {
            return false;
        }
        else {
            return true;
        }
    }
    
    // true : valid
    // false : invalid
    public boolean check_this_validity_by_predicate_list() {
        if(!this.getPredicateList().isEmpty()) {
            return !cotains_not_equal_same_arg();
        }
        return false;
    }
    
    // true : contains (not (= ?a ?a))
    // false: does not contain
    @Override
    public boolean cotains_not_equal_same_arg() {
        for(PDDLPredicate p : this.getPredicateList()) {
            if(p.cotains_not_equal_same_arg()) {
                return true;
            }
        }
        return false;
    }

    public void remove_all_negative_predicates_in_predicate_list() {
        ArrayList<PDDLPredicate> new_predicate_list = new ArrayList<>();
        for(PDDLPredicate p : this.predicateList) {
            if(p instanceof PDDLPredicate_package) {
                ((PDDLPredicate_package) p).remove_all_negative_predicates_in_predicate_list();
            }
            else {
                if(p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getTruthValue() == true) {
                    new_predicate_list.add(p);
                }
                else if(p instanceof PDDLPredicate_number) {
                    new_predicate_list.add(p);
                }
            }
        }
        this.setPredicate(new_predicate_list);
    }

    @Override
    public int get_number_of_predicate_logical() {
        int toReturn = 0;
        for(PDDLPredicate p : predicateList) {
            if(p instanceof PDDLPredicate_logical) {
                toReturn ++;
            }
            else if(p instanceof PDDLPredicate_package){
                toReturn = toReturn + ((PDDLPredicate_package) p).get_number_of_predicate_logical();
            }
        }
        return toReturn;
    }
    
}
