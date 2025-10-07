package data;

import java.util.*;

/**
 *
 * @author danchengGao
 */
public class StripsAction {
    
    private String name;
    private ArrayList <StripsArgument> parameters = new ArrayList <>();
    private PDDLPredicate precondition;
    private PDDLPredicate effect;
    private boolean isMacroAction = false;
    private boolean isOriginalMacro = false;
    private boolean isOriginalInSearchMacro = false;
    
    private double score = 0.0;
    private boolean selected = false;

    private ArrayList <PDDLPredicate_logical> binding_pairs = new ArrayList<>();
    // name, list of args
    private HashMap <String, ArrayList<String>> constituent_actions = new HashMap<>();
    private ArrayList<String> constituent_actions_names = new ArrayList<>();
    
    public StripsAction(String n, boolean isMacro) {
        name = n;
        isMacroAction = isMacro;
    }
    
    // a copy constructor
    public StripsAction(StripsAction another) {
        this.name = another.name;
        this.isMacroAction = another.isMacroAction;
        
        for(StripsArgument arg : another.parameters) {
            StripsArgument a = new StripsArgument(arg);
            this.parameters.add(a);
        }
        
        this.precondition = another.precondition.copy();        
        this.effect = another.effect.copy();

        //this.constituent_actions = another.constituent_actions;
        for(Map.Entry<String, ArrayList<String>> entry : another.constituent_actions.entrySet()) {
            ArrayList<String> new_list = new ArrayList<>();
            for(String s : entry.getValue()) {
                new_list.add(s);
            }
            this.constituent_actions.put(entry.getKey(), new_list);
        }
        this.constituent_actions_names = another.constituent_actions_names;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void addParameters(StripsArgument a){
        parameters.add(a);
    }
    
    public void setParameters(ArrayList <StripsArgument> a){
        parameters = a;
    }

    public void setGroundedParameters(ArrayList<String> paraList) {
        ArrayList <StripsArgument> originalArguments = new ArrayList <>();
        
        for(int i = 0 ; i < paraList.size(); i ++) {
            originalArguments.add(new StripsArgument(parameters.get(i)));
            parameters.get(i).setValue(paraList.get(i));
            precondition.replace_argument(originalArguments.get(i), parameters.get(i));
            effect.replace_argument(originalArguments.get(i), parameters.get(i));
        }
        
    }
    
    public void addPrecon(PDDLPredicate p) {
        if(this.precondition instanceof PDDLPredicate_logical) {
            this.setPrecondition(((PDDLPredicate_logical) (this.precondition)).merge_simple(p, true));
        }
        else if(this.precondition instanceof PDDLPredicate_package){
            this.setPrecondition(((PDDLPredicate_package) (this.precondition)).merge_simple(p, true));
        }
    }
    
    public void setPrecondition(PDDLPredicate p){
        precondition = p;
    }

    public void addEff(PDDLPredicate p) {
        if(this.effect instanceof PDDLPredicate_logical) {
            this.setEffect(((PDDLPredicate_logical) (this.effect)).merge_simple(p, false));
        }
        else if(this.precondition instanceof PDDLPredicate_package){
            this.setEffect(((PDDLPredicate_package) (this.effect)).merge_simple(p, false));
        }
    }
    
    public void setEffect(PDDLPredicate p) {
        effect = p;
    }

    public void setOriginalMacro(boolean isOriginalMacro) {
        this.isOriginalMacro = isOriginalMacro;
    }

    public void setOriginalInSearchMacro(boolean isOriginalInSearchMacro) {
        this.isOriginalInSearchMacro = isOriginalInSearchMacro;
    }
    
    public String getName() {
        return name;
    }
    
    public ArrayList<StripsArgument> getParameter() {
        return parameters;
    }
    
    public boolean getIsMacroAction() {
        return isMacroAction;
    }
     
    public PDDLPredicate getPrecondition() {
        return precondition;
    }
    
    public PDDLPredicate getEffect() {
        return effect;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setScore(double s) {
        score = s;
    }
    
    public void setFirstAction(boolean value) {
        this.precondition.setFirstAction(value);
        this.effect.setFirstAction(value);
    }
    
    public boolean getSelected() {
        return selected;
    }
    
    public void setSelected(boolean s) {
        selected = s;
    }

    public void addBindingPair(PDDLPredicate_logical p) {
        binding_pairs.add(p);
    }

    public ArrayList<PDDLPredicate_logical>getBinding_pairs() {
        return binding_pairs;
    }

    public void setConstituent_actions(String name, ArrayList<String> args) {
        constituent_actions_names.add(name);
        constituent_actions.put(name, args);
    }

    public HashMap<String, ArrayList<String>> getConstituent_actions() {
        return constituent_actions;
    }

    public ArrayList<String> getConstituent_argsByName(String name) {
        return constituent_actions.get(name);
    }

    public ArrayList<String> getConstituent_actions_names() {
        return constituent_actions_names;
    }

    public void updateTypeInPredicate() {
        for(StripsArgument one : this.parameters) {
            for(StripsArgument another: this.precondition.get_all_arguments()) {
                if(one.getName().equals(another.getName()) && !one.getType().equals(another.getType())) {
                    another.setType(one.getType());
                }
            }
            for(StripsArgument another: this.effect.get_all_arguments()) {
                if(one.getName().equals(another.getName()) && !one.getType().equals(another.getType())) {
                    another.setType(one.getType());
                }
            }
        }
    }

    public int getArgIndexByArgName(String name) {
        for(int i = 0; i < parameters.size(); i ++) {
            if(parameters.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    // equals by name only
    @Override    
    public boolean equals(Object o) {
        if(this.name.equalsIgnoreCase(((StripsAction)o).getName()))
            return true;
        else
            return false;
    }
    
    @Override
    public String toString() {
    	String toReturn = "(:action ";
        if(isMacroAction) {
            if(isOriginalMacro)
                toReturn = toReturn + "macro-" + this.name + "\n";
            else if(isOriginalInSearchMacro)
                toReturn = toReturn + "nonr-" + this.name + "\n";
            else
                toReturn = toReturn + "macro-" + this.name + "\n";
        }
        else {
            toReturn = toReturn + this.name + "\n";
        }
            	
    	toReturn = toReturn + "    :parameters (";
    	for(StripsArgument a : parameters) {
    	    toReturn = toReturn + a + " ";
    	}
        if(parameters.size() != 0) {
    	    toReturn = toReturn.substring(0, toReturn.length()-1);
        }
    	toReturn = toReturn + ")" + "\n";
    	
    	toReturn = toReturn + "    :precondition \n";
    	toReturn = toReturn + precondition.toString_PDDL("") + "\n";
    	
    	toReturn = toReturn + "    :effect \n";
    	toReturn = toReturn + effect.toString_PDDL("") + "\n";
    	
    	toReturn = toReturn + ") \n\n";
        return toReturn;
    }

    public void simplify_precon() {
        if(precondition instanceof PDDLPredicate_package) {
            this.setPrecondition(((PDDLPredicate_package) precondition).simplify(true));
        }
    }

    public void simplify_effect() {
        if(effect instanceof PDDLPredicate_package) {
            this.setEffect(((PDDLPredicate_package) effect).simplify(false));
        }
    }
    
    // replace equal arguments
    // simplify precon & effect
    // remove duplicating parameters
    public void replace_equal_arguments() {
        
        if(this.precondition instanceof PDDLPredicate_logical) {
            if( ((PDDLPredicate_logical) this.precondition).getName().equals("=")) {
                // replace all name2 with name1
                String name1 = ((PDDLPredicate_logical) this.precondition).getArgument().get(0).getName();
                String name2 = ((PDDLPredicate_logical) this.precondition).getArgument().get(1).getName();
                // replace parameters
    	        replace_equal_arguments_in_parameters(name2, name1);
                // replace precondition
    	        // precondition has only one logical predicate which is =
    	        // remove this predicate
                this.setPrecondition(new PDDLPredicate_logical(""));
                // replace effect
                replace_equal_arguments_in_effect(name2, name1);

                replace_equal_arguments_in_constituents(name2, name1);
            }
        }
        
        else if(this.precondition instanceof PDDLPredicate_package) {
            ArrayList<PDDLPredicate_logical> equal_list = ((PDDLPredicate_package) this.precondition).get_equal_logical_predicate();
            for(PDDLPredicate_logical equal_predicate : equal_list) {
                // replace all name2 with name1
                String name1 = equal_predicate.getArgument().get(0).getName();
                String name2 = equal_predicate.getArgument().get(1).getName();
                // replace parameters
    	        replace_equal_arguments_in_parameters(name2, name1);
                // replace precondition + simplify
                ((PDDLPredicate_package) this.precondition).replace_argument_names(name2, name1);
                ((PDDLPredicate_package) this.precondition).simplify(true);
                replace_equal_arguments_in_effect(name2, name1);
                replace_equal_arguments_in_constituents(name2, name1);
            }
            // remove equal predicates:
            ((PDDLPredicate_package) this.precondition).remove_equal_predicates();
        }    
    }
    
    public void replace_equal_arguments_in_parameters(String original_name, String new_name) {
        for(StripsArgument a : this.parameters) {
            if(a.getName().equals(original_name)) {
                a.setName(new_name);
            }
        }
        remove_duplicate_parameters();
    }
    
    private void replace_equal_arguments_in_effect(String original_name, String new_name) {
        if(this.effect instanceof PDDLPredicate_logical) {
            ((PDDLPredicate_logical) this.effect).replace_argument_names(original_name, new_name);
        }
        else if(this.effect instanceof PDDLPredicate_package) {
            ((PDDLPredicate_package) this.effect).replace_argument_names(original_name, new_name);
            this.setEffect(((PDDLPredicate_package) this.effect).simplify(false));
        }
    }
    
    private void replace_equal_arguments_in_constituents(String original_name, String new_name) {
        for(ArrayList<String> list : constituent_actions.values()) {
            list.replaceAll(
                    arg -> arg.equals(original_name) ? new_name : arg);
        }
    }

    public void remove_duplicate_parameters() {
        ArrayList<StripsArgument> newList = new ArrayList<>();
        for(StripsArgument a : parameters) {
            if(!newList.contains(a)) {
                newList.add(a);
            }
        }
        this.setParameters(newList);
    }
    
    // remove predicates in effect that are already in precon
    // if precon & effect from same sub action: remove effect
    // if precon & effect from different sub actions :
    //    if precon first & effect second : remove eff //keep both?(there is a chance the predicate get negated in eff?)
    //    if precon second & effect first : remove precon
    public void remove_precon_from_effect() {
        if(this.effect instanceof PDDLPredicate_package) {
            PDDLPredicate returned = ((PDDLPredicate_package) this.effect).remove_duplicate(this.precondition, false);
            this.setPrecondition(returned);
        }
        
        else if(this.effect instanceof PDDLPredicate_logical) {
            if(this.precondition instanceof PDDLPredicate_logical) {
                if( ((PDDLPredicate_logical) this.effect).equals_checking_truth_value( (PDDLPredicate_logical) this.precondition ) ) {
                    if(((PDDLPredicate_logical) this.effect).getFirstAction() == ((PDDLPredicate_logical) this.precondition).getFirstAction()) {
                        this.setEffect(new PDDLPredicate_logical(""));
                    }
                    else if(!((PDDLPredicate_logical) this.effect).getFirstAction() && ((PDDLPredicate_logical) this.precondition).getFirstAction()) {
                        this.setEffect(new PDDLPredicate_logical(""));
                    }
                    else if(((PDDLPredicate_logical) this.effect).getFirstAction() && !((PDDLPredicate_logical) this.precondition).getFirstAction()) {
                        this.setPrecondition(new PDDLPredicate_logical(""));
                    }
                }
            }
            else if(this.precondition instanceof PDDLPredicate_package) {
                PDDLPredicate_logical p = ((PDDLPredicate_package) this.precondition).contains((PDDLPredicate_logical) this.effect);
                if(!((PDDLPredicate_logical) p).getName().equals("")) {
                    if(((PDDLPredicate_logical) this.effect).getFirstAction() == p.getFirstAction()) {
                        this.setEffect(new PDDLPredicate_logical(""));
                    }
                    else if(!((PDDLPredicate_logical) this.effect).getFirstAction() && p.getFirstAction()) {
                        this.setEffect(new PDDLPredicate_logical(""));
                    }
                    else if(((PDDLPredicate_logical) this.effect).getFirstAction() && !p.getFirstAction()) {
                        // remove precon
                        ((PDDLPredicate_package) this.precondition).removePredicate(p);
                    }
                }
            }
        }

        this.simplify_precon();
        this.simplify_effect();
    }

    // return valid
    public boolean remove_precon_from_effect_new() {
        ArrayList<PDDLPredicate_logical> deleted_by_first = new ArrayList<>();
        ArrayList<PDDLPredicate_logical> added_by_first = new ArrayList<>();

        ArrayList<PDDLPredicate> new_precon_list = new ArrayList<>();
        if(this.effect instanceof PDDLPredicate_package) {
            for (PDDLPredicate p : ((PDDLPredicate_package) this.effect).getPredicateList()) {
                if (p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getFirstAction() && !((PDDLPredicate_logical) p).getTruthValue()) {
                    boolean found_opposite = false;
                    for (PDDLPredicate p_another : ((PDDLPredicate_package) this.effect).getPredicateList()) {
                        if (p_another instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p_another).getFirstAction() && ((PDDLPredicate_logical) p_another).is_opposite((PDDLPredicate_logical) p)) {
                            found_opposite = true;
                            break;
                        }
                    }
                    if (found_opposite == false) {
                        deleted_by_first.add((PDDLPredicate_logical) p);
                    }
                }
                if (p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getFirstAction() && ((PDDLPredicate_logical) p).getTruthValue()) {
                    added_by_first.add((PDDLPredicate_logical) p);
                }
            }

        }
        else {
            if(this.effect instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) this.effect).getFirstAction()) {
                if(((PDDLPredicate_logical) effect).getTruthValue()) {
                    added_by_first.add((PDDLPredicate_logical) effect);
                }
                else {
                    deleted_by_first.add((PDDLPredicate_logical) effect);
                }
            }
        }

        if(this.precondition instanceof PDDLPredicate_package) {
            for(PDDLPredicate p : ((PDDLPredicate_package) this.precondition).getPredicateList()) {
                if(p instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p).getFirstAction() && ((PDDLPredicate_logical) p).getTruthValue()) {
                    for(PDDLPredicate_logical p_in_first : deleted_by_first) {
                        if(p_in_first.is_opposite((PDDLPredicate_logical) p)) {
                            return false;
                        }
                    }
                    if(!added_by_first.contains((PDDLPredicate_logical) p)) {
                        new_precon_list.add(p);
                    }
                }
                else if(p instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p).getFirstAction() && !((PDDLPredicate_logical) p).getTruthValue()) {
                    for(PDDLPredicate_logical p_in_first : added_by_first) {
                        if(p_in_first.is_opposite((PDDLPredicate_logical) p)) {
                            return false;
                        }
                    }
                    if(!deleted_by_first.contains((PDDLPredicate_logical) p)) {
                        new_precon_list.add(p);
                    }
                }
                else {
                    new_precon_list.add(p);
                }
            }

            ((PDDLPredicate_package) this.precondition).setPredicate(new_precon_list);
        }
        else {
            PDDLPredicate p = this.precondition;
            if(p instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p).getFirstAction() && ((PDDLPredicate_logical) p).getTruthValue()) {
                for(PDDLPredicate_logical p_in_first : deleted_by_first) {
                    if(p_in_first.is_opposite((PDDLPredicate_logical) p)) {
                        return false;
                    }
                }
                if(added_by_first.contains((PDDLPredicate_logical) p)) {
                    this.precondition = new PDDLPredicate_package("");
                }
            }
            else if(p instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p).getFirstAction() && !((PDDLPredicate_logical) p).getTruthValue()) {
                for(PDDLPredicate_logical p_in_first : added_by_first) {
                    if(p_in_first.is_opposite((PDDLPredicate_logical) p)) {
                        return false;
                    }
                }
                if(deleted_by_first.contains((PDDLPredicate_logical) p)) {
                    this.precondition = new PDDLPredicate_package("");
                }
            }
        }

        if(precondition instanceof PDDLPredicate_package) {
            ArrayList<PDDLPredicate> simplified_precon = new ArrayList<>();
            for(PDDLPredicate p : ((PDDLPredicate_package) this.precondition).getPredicateList()) {
                if(!(p instanceof PDDLPredicate_logical)) {
                    simplified_precon.add(p);
                }
            }

            for(PDDLPredicate p : ((PDDLPredicate_package) this.precondition).getPredicateList()) {
                if(!(p instanceof PDDLPredicate_logical)) {
                    continue;
                }
                if(p instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p).getFirstAction()) {
                    continue;
                }
                if(p instanceof PDDLPredicate_logical && simplified_precon.contains(p)) {
                    continue;
                }
                else {
                    simplified_precon.add(p);
                }
            }

            for(PDDLPredicate p : ((PDDLPredicate_package) this.precondition).getPredicateList()) {
                if(!(p instanceof PDDLPredicate_logical)) {
                    continue;
                }
                if(p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getFirstAction()) {
                    continue;
                }
                if(p instanceof PDDLPredicate_logical && simplified_precon.contains(p)) {
                    continue;
                }
                else {
                    simplified_precon.add(p);
                }
            }
            ((PDDLPredicate_package) precondition).setPredicate(simplified_precon);
        }

        if(effect instanceof PDDLPredicate_logical) {
            return true;
        }

        ArrayList<PDDLPredicate> new_eff_list = new ArrayList<>();
        for (PDDLPredicate p : ((PDDLPredicate_package) this.effect).getPredicateList()) {
            if (p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getFirstAction() && ((PDDLPredicate_logical) p).getTruthValue()) {
                boolean deleted_later = false;
                for (PDDLPredicate p_another : ((PDDLPredicate_package) this.effect).getPredicateList()) {
                    if (p_another instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p_another).getFirstAction() && ((PDDLPredicate_logical) p_another).is_opposite((PDDLPredicate_logical) p)) {
                        deleted_later = true;
                        break;
                    }
                }
                if(deleted_later == false) {
                    new_eff_list.add(p);
                }
            }
            else if (p instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) p).getFirstAction() && !((PDDLPredicate_logical) p).getTruthValue()) {
                boolean added_later = false;
                for (PDDLPredicate p_another : ((PDDLPredicate_package) this.effect).getPredicateList()) {
                    if (p_another instanceof PDDLPredicate_logical && !((PDDLPredicate_logical) p_another).getFirstAction() && ((PDDLPredicate_logical) p_another).is_opposite((PDDLPredicate_logical) p)) {
                        added_later = true;
                        break;
                    }
                }
                if(added_later == false) {
                    new_eff_list.add(p);
                }
            }
            else {
                new_eff_list.add(p);
            }
        }
        ((PDDLPredicate_package) this.effect).setPredicate(new_eff_list);
        return true;
    }

    // check validity & refine
    public boolean check_validity() {        
        // not valid : empty precon, empty eff
        // package not valid : empty predicate list
        // logical not valid : empty name
        if(!this.getPrecondition().check_this_validity_by_name() || !this.getEffect().check_this_validity_by_name()) {
            return false;
        }        
        
        boolean precon_valid = false;
        boolean eff_valid = false;
        
        if(this.getPrecondition() instanceof PDDLPredicate_package) { 
            // not valid: (not (= ?a ?a)) in precondition
            precon_valid = ((PDDLPredicate_package) this.getPrecondition()).check_this_validity_by_predicate_list();
        }
        
        if(this.getEffect() instanceof PDDLPredicate_package) {
            // not valid: (not (= ?a ?a)) in effect
            eff_valid = ((PDDLPredicate_package) this.getEffect()).check_this_validity_by_predicate_list();
        }
        
        if(precon_valid && eff_valid && check_precon_validity_self()) {
            return true;
        }
        else {
            return false;
        }
    }
    
    // 5.4.1
    // true : valid
    // false : invalid
    public boolean check_precon_validity_self() {
        if(this.getPrecondition() instanceof PDDLPredicate_logical) {
            return true;
        }
        else if(this.getPrecondition() instanceof PDDLPredicate_package) {
            if(((PDDLPredicate_package) this.getPrecondition()).isLogical()) {
                // precondition is a "and" package
                PDDLPredicate_package pre = (PDDLPredicate_package) this.getPrecondition();
                if(pre.getPrefix().equals("and")) {
                    // new list to keep the checked predicates
                    ArrayList<PDDLPredicate> newList = new ArrayList<>();
                    // check predicate list in reverse order
                    for(int i = pre.getPredicateList().size() - 1; i >= 0; i --) {
                        boolean conflict = false;
                        PDDLPredicate one = pre.getPredicateList().get(i);
                        for(int j = 0; j < newList.size(); j ++) {                            
                            PDDLPredicate another = newList.get(j);
                            // if both simple predicates
                            if(one instanceof PDDLPredicate_logical && another instanceof PDDLPredicate_logical) {
                                // equal predicates but opposite truth value
                                if(((PDDLPredicate_logical) one).equals(((PDDLPredicate_logical) another)) && ((PDDLPredicate_logical) one).getTruthValue() != ((PDDLPredicate_logical) another).getTruthValue()) {
                                    // precondition has conflicting predicates
                                    // if same first_action value : invalid, return false                                                
                                    // if different first_action value : check effect
                                    // one : from the former, another : from the latter
                                    // check whether the latter is achieved in the effect of the former
                                    // if yes then : remove the precon, the precon should have been removed before
                                    // if no then : invalid, return false
                                    // in summary: it is not possible to have the latter precon achieved in effect as they have been removed before
                                    // therefore in any case return false
                                    return false;
                                }
                            }
                            //else if(one instanceof PDDLPredicate_package && ((PDDLPredicate_package) one).isLogical()) {
                                // TODO : not seen for now, preconditions do not have further packages
                            //}
                        }
                        // the predicate one does not conflict with any one
                        if(!conflict) {
                            // add in front of the new list
                            newList.add(0, one);
                        }
                    }
                }
                // if precondition is not a "and" package
                else {
                   // TODO : not seen for now
                }
            }
        }
        return true;
    }
    
    // 5.4.3 effect has conflicting predicates with itself: 
    // if conflict from the same action, invalid
    // if different: 
    //     and : 
    //         simple vs simple : if same action conflict, if different keeps the latter (1)
    public boolean remove_conflicting_effect_predicates_simple() {
        if(this.getEffect() instanceof PDDLPredicate_logical) {
            return true;
        }
        
        else if(this.getEffect() instanceof PDDLPredicate_package) {
            // logical package
            if(((PDDLPredicate_package) this.getEffect()).isLogical()) {
                PDDLPredicate_package eff = (PDDLPredicate_package) this.getEffect();
                
                // effect is a "and" package
                if(eff.getPrefix().equals("and")) {
                    ArrayList<PDDLPredicate> new_predicate_list = new ArrayList<>();
                    for(int i = 0; i < eff.getPredicateList().size(); i ++) {
                        
                        PDDLPredicate one = eff.getPredicateList().get(i);
                        boolean one_can_be_added = true;
                        
                        for(int j = i + 1; j < eff.getPredicateList().size(); j ++) { 
                            
                            PDDLPredicate two = eff.getPredicateList().get(j);                            
                            
                            if(one instanceof PDDLPredicate_logical && two instanceof PDDLPredicate_logical) {
                                
                                if(((PDDLPredicate_logical) one).is_opposite((PDDLPredicate_logical) two)) {
                                    
                                    if(((PDDLPredicate_logical) one).getFirstAction() && !((PDDLPredicate_logical) two).getFirstAction()) {
                                        // remove one and keep two
                                        one_can_be_added = false;
                                    }
                                    else if(((PDDLPredicate_logical) one).getFirstAction() == ((PDDLPredicate_logical) two).getFirstAction()) {
                                        //return false;
                                    }
                                    else {
                                        continue;
                                    }
                                }
                                else {
                                    continue;
                                }
                            }
                            else {
                                continue;
                            }
                        }
                        
                        if(one_can_be_added) {
                            if(!new_predicate_list.contains(one))
                                new_predicate_list.add(one);
                        }
                    }
                    eff.setPredicate(new_predicate_list);
                }                 
            }                    
        }        
        
        return true;
    }
    
    // compare actions with different arg names
    // whether they are the same
    public boolean compareActions(StripsAction another) {
        if(this.compareParameters(another.getParameter()) && this.comparePreconditions(another.getPrecondition()) && this.compareEffects(another.getEffect())) {
            return true;
        }
        return false;
    }
    
    // return true if the two parameter lists are the same
    private boolean compareParameters(ArrayList<StripsArgument> another) {
    	if(this.parameters.size() == another.size()) {
            TreeMap<String, Integer> thisMap = new TreeMap<>();
            TreeMap<String, Integer> anotherMap = new TreeMap<>();
            for(StripsArgument a : this.parameters) {
                StripsType p = a.getType();
                if(thisMap.containsKey(p.getName())) {
                    thisMap.put(p.getName(), thisMap.get(p.getName()) + 1);
                }
                else {
                    thisMap.put(p.getName(), 1);
                }
            }
            for(StripsArgument a : another) {
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
    
    private boolean comparePreconditions(PDDLPredicate another) {
        if(this.getPrecondition() instanceof PDDLPredicate_package && another instanceof PDDLPredicate_package) {
            return ((PDDLPredicate_package) this.getPrecondition()).comparePredicate_package((PDDLPredicate_package) another);
        }
        else if(this.getPrecondition() instanceof PDDLPredicate_logical && another instanceof PDDLPredicate_logical) {
            return ((PDDLPredicate_logical) this.getPrecondition()).comparePredicate_logical((PDDLPredicate_logical) another);
        }
        return false;
    }
    
    private boolean compareEffects(PDDLPredicate another) {
        if(this.getEffect() instanceof PDDLPredicate_package && another instanceof PDDLPredicate_package) {
            return ((PDDLPredicate_package) this.getEffect()).comparePredicate_package((PDDLPredicate_package) another);
        }
        else if(this.getEffect() instanceof PDDLPredicate_logical && another instanceof PDDLPredicate_logical) {
            return ((PDDLPredicate_logical) this.getEffect()).comparePredicate_logical((PDDLPredicate_logical) another);
        }
        return false;
    }

    // return boolean successful
    // unsuccessful: when precon only has 1 predicate: nothing to remove
    public boolean remove_one_predicate_from_precon(int index_to_remove) {
        if(precondition instanceof PDDLPredicate_logical || precondition instanceof PDDLPredicate_number) {
            return false;
        }
        else if(index_to_remove >= get_number_of_predicates_in_precon()) {
            return false;
        }

        for(PDDLPredicate p : ((PDDLPredicate_package) precondition).getPredicateList()) {
            if(p instanceof PDDLPredicate_logical) {
                index_to_remove --;
            }
            else if(p instanceof PDDLPredicate_number) {
                continue;
            }
            else if(p instanceof PDDLPredicate_package) {
                index_to_remove = index_to_remove - ((PDDLPredicate_package) p).get_number_of_predicate_logical();
            }

            if (index_to_remove < 0) {
                if(p instanceof PDDLPredicate_logical) {
                    ((PDDLPredicate_package) precondition).removePredicate(p);
                }
                else {
                    int index = index_to_remove + ((PDDLPredicate_package) p).get_number_of_predicate_logical();
                    ((PDDLPredicate_package) p).removePredicate_logical(index);
                }
                break;
            }
        }

        return true;
    }

    public boolean remove_predicates_from_precon(ArrayList<Integer> index_list) {
        Collections.sort(index_list);
        for(int i = 0; i < index_list.size(); i ++) {
            if(!remove_one_predicate_from_precon(index_list.get(i) - index_list.indexOf(index_list.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public boolean remove_one_predicate_from_effect(int index_to_remove) {
        if(effect instanceof PDDLPredicate_logical || effect instanceof PDDLPredicate_number) {
            return false;
        }
        else if(index_to_remove >= get_number_of_predicates_in_effect()) {
            return false;
        }

        for(PDDLPredicate p : ((PDDLPredicate_package) effect).getPredicateList()) {
            if(p instanceof PDDLPredicate_logical) {
                index_to_remove --;
            }
            else if(p instanceof PDDLPredicate_number) {
                continue;
            }
            else if(p instanceof PDDLPredicate_package) {
                index_to_remove = index_to_remove - ((PDDLPredicate_package) p).get_number_of_predicate_logical();
            }

            if (index_to_remove < 0) {
                if(p instanceof PDDLPredicate_logical) {
                    ((PDDLPredicate_package) effect).removePredicate(p);
                }
                else {
                    int index = index_to_remove + ((PDDLPredicate_package) p).get_number_of_predicate_logical();
                    ((PDDLPredicate_package) p).removePredicate_logical(index);
                }
                break;
            }
        }

        return true;
    }

    public boolean remove_predicates_from_effect(ArrayList<Integer> index_list) {
        Collections.sort(index_list);
        for(int i = 0; i < index_list.size(); i ++) {
            if(!remove_one_predicate_from_effect(index_list.get(i) - index_list.indexOf(index_list.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public void remove_all_delete_effects() {
        if(this.effect instanceof PDDLPredicate_package) {
            ((PDDLPredicate_package) this.effect).remove_all_negative_predicates_in_predicate_list();
        }
        else if(this.effect instanceof PDDLPredicate_logical && ((PDDLPredicate_logical) this.effect).getTruthValue() == false){
            this.effect = new PDDLPredicate_package("");
        }
    }

    public int get_number_of_predicates_in_precon() {
        return precondition.get_number_of_predicate_logical();
    }

    public int get_number_of_predicates_in_effect() {
        return effect.get_number_of_predicate_logical();
    }

}
