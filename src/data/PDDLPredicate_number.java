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
public class PDDLPredicate_number extends PDDLPredicate {
    
    private Integer number = 0;
    private boolean first_action = true;
    
    // package with logical prefix
    public PDDLPredicate_number(String s) {
        number = Integer.valueOf(s);
    }
    
    // a copy constructor
    public PDDLPredicate_number(PDDLPredicate_number n) {
        number = n.getNumber();
    }
    
    @Override
    public PDDLPredicate copy() {
        return new PDDLPredicate_number(this);
    }
    
    public Integer getNumber() {
        return number;
    }
    
    public void add(Integer i) {
        this.number = this.number + i;    
    }
    
    public void subtract(Integer i) {
        this.number = this.number - i;    
    }
    
    public void multiply(Integer i) {
        this.number = this.number * i;    
    }
    
    public void divide(Integer i) {
        this.number = this.number / i;    
    }
    
    public void set(Integer i) {
        this.number = i;    
    }
    
    public boolean getFirstAction() {
        return first_action;
    }
    
    @Override
    public void setFirstAction(boolean value) {
        first_action = value;
    }
    
    @Override
    public boolean equals(Object o) {
        return this.getNumber().equals(((PDDLPredicate_number)o).getNumber());
    }
    
    @Override
    public String toString() {
        String toReturn = number.toString();        
        return toReturn;
    }
    
    @Override
    public String toString_PDDL(String indent) {
        String toReturn = indent + "    " + number.toString();        
        return toReturn;
    }
    
    @Override
    public void negate() {}
    
    @Override
    public void replace_argument_names(String original_name, String new_name) {}
    
    @Override
    public  void replace_argument(StripsArgument original_arg, StripsArgument new_arg){}
    
    @Override
    public boolean contains_argument(StripsArgument a) {
        return false;
    }
    
    @Override
    public int check_for_conflict(PDDLPredicate_logical another, boolean different_arg_names) {
        return 3;
    }
    
    @Override
    public boolean check_this_validity_by_name() {
        return true;
    }
    
    @Override
    public ArrayList<StripsArgument> get_all_arguments() {
        ArrayList<StripsArgument> toReturn = new ArrayList<>();
        return toReturn;
    }
    
    @Override
    public boolean cotains_not_equal_same_arg() {
        return false;
    }

    @Override
    public int get_number_of_predicate_logical() {
        return 0;
    }
}
