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
abstract class PDDLPredicate {
    abstract PDDLPredicate copy();
    abstract void setFirstAction(boolean value);
    abstract String toString_PDDL(String indent);
    abstract void negate();
    abstract void replace_argument_names(String original_name, String new_name);
    abstract void replace_argument(StripsArgument original_arg, StripsArgument new_arg);
    abstract boolean contains_argument(StripsArgument a);

    // 0 : same type conflict, 
    // 1 : former is subtype conflict, 
    // 2 : latter is subtype conflict, 
    // 3 : no conflict, 
    // 4 : same first_action conflict
    abstract int check_for_conflict(PDDLPredicate_logical another, boolean different_arg_names);
    abstract boolean check_this_validity_by_name();
    abstract ArrayList<StripsArgument> get_all_arguments();
    
    // true : contains (not (= ?a ?a))
    // false: does not contain
    abstract boolean cotains_not_equal_same_arg();

    abstract int get_number_of_predicate_logical();
}
