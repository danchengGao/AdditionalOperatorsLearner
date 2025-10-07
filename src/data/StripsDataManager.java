/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import java.util.*;

import parser.ParserManager;

/**
 *
 * @author danchengGao
 */
public class StripsDataManager {
    
    private ParserManager pm;
    
    private String domainName = "";
    private final ArrayList <StripsType> types = new ArrayList <> ();
    private final ArrayList <String> requirements;
    private final ArrayList <StripsConstant> constants = new ArrayList <> ();
    private final ArrayList <PDDLPredicate_logical> predicates = new ArrayList <> ();
    private ArrayList <PDDLPredicate_logical> functions = new ArrayList <> ();
    private final ArrayList <StripsAction> actions = new ArrayList <> ();
    private ArrayList <StripsAction> macroActions = new ArrayList <> ();
    private ArrayList <Integer> selectedMacroActionIndex = new ArrayList <> ();
    // useful macros
    private ArrayList <Integer> usefulMacroActionIndex = new ArrayList <> ();
    private PDDLPrefix allPrefix = new PDDLPrefix();
    private int mergeIndex = 0;
    
    private String originalDomainPath = "";
    
    private int total_macro_generated = 0;
    private int pruned_during_generation = 0;
    private int pruned_during_ranking = 0;

    private int createRandomCounter = 0;

    private ArrayList <StripsAction> randomActions = new ArrayList <> ();

    // macro integer in macroActions = number of times encountered during paramILS search
    private TreeMap <Integer, Integer> macro_counter = new TreeMap <> ();

    private final ArrayList <StripsAction> originalMacroActions = new ArrayList <> ();
    private final ArrayList <StripsAction> originalInSearchActions = new ArrayList <> ();

    
    public StripsDataManager(ParserManager p) {
        this.requirements = new ArrayList <> ();
        pm = p;
        System.out.println("Creating data...");
    }
    
    public void createDomainName(String name) {
    	domainName = name;
    }
    
    public void createRequirement(ArrayList <String> r) {
    	for(int i = 0; i < r.size(); i ++) {
            if(i == r.size()-1) {
                requirements.add(r.get(i).substring(0, r.get(i).length()-1));
            }
            else {
                requirements.add(r.get(i));
            }
        }
    }
    
    public void createType(String name, String parent) {       
        StripsType t = new StripsType(name);
        StripsType p = new StripsType(parent);
        // type has parent
        if(parent != null) {
            // parent already created
            if(types.contains(p)) {
                // type already created
                if(types.contains(t)) {
                    (types.get(types.indexOf(t))).setParentType(types.get(types.indexOf(p)));
                }
                // type not yet created
                else {
                    t.setParentType(types.get(types.indexOf(p)));
                    types.add(t);
                }                    
            }
            // parent not yet created
            else {
                // type already created
                if(types.contains(t)) {
                    types.add(p);
                    (types.get(types.indexOf(t))).setParentType(types.get(types.indexOf(p)));
                }  
                // type not yet created
                else {
                    types.add(p);
                    t.setParentType(types.get(types.indexOf(p)));
                    types.add(t);
                }
            }
        }
        // type does not have a parent
        else {
            // type not yet created
            if(!types.contains(t)) {
                types.add(t);
            }   
        }
    }
    
    public void createConstant(String name, String parent) {
        // check that the name is not a type
        for(StripsType t : types) {
            if(t.getName().equals(name)) {
                return;
            }
        }
        // if constant has a type
        if(!parent.equals("")) {
            StripsType p = new StripsType(parent);
            StripsConstant t = new StripsConstant(name, types.get(types.indexOf(p)));
            constants.add(t);
        }
        // if constant does not have a type
        else {
            StripsConstant t = new StripsConstant(name);
            constants.add(t);
        }
        
    }
    
    public void createPredicate(String name, ArrayList <String[]> arguments) {
        PDDLPredicate_logical p = new PDDLPredicate_logical(name);
        for (String[] ar : arguments) {        	
            StripsType t = new StripsType(ar[1]);
            // if the argument has a type
            if(!t.getName().equals("")) {
                StripsArgument argument = new StripsArgument(ar[0], types.get(types.indexOf(t)));  
                p.addArgument(argument);
            }
            // if the argument does not have a type        
            else {
                StripsArgument argument = new StripsArgument(ar[0], t);  
                p.addArgument(argument);
            }
        }
        predicates.add(p);
        
        // arguments with more than 1 possible types
        for(int i = 0; i < arguments.size(); i ++) {
            String[] ar = arguments.get(i);
            if(ar.length > 2) {
                PDDLPredicate_logical new_p = p.copy();
                ArrayList <StripsArgument> original_arguments = new_p.getArgument();
                StripsType t = new StripsType(ar[2]);
                StripsArgument argument = new StripsArgument(ar[0], types.get(types.indexOf(t)));  
                original_arguments.set(i, argument);
                new_p.setArgument(original_arguments);
                predicates.add(new_p);
            }            
        }
    }
    
    public void createFunction(String name, ArrayList <String[]> arguments) {
        PDDLPredicate_logical f = new PDDLPredicate_logical(name);
        for (String[] ar : arguments) {         
            StripsType t = new StripsType(ar[1]);
            // if the argument has a type
            if(!t.getName().equals("")) {
                StripsArgument argument = new StripsArgument(ar[0], types.get(types.indexOf(t)));  
                f.addArgument(argument);
            }
            // if the argument does not have a type        
            else {
                StripsArgument argument = new StripsArgument(ar[0], t);  
                f.addArgument(argument);
            }
        }
        f.setAsFunction();
        functions.add(f);
    }
    
    public void createAction(String name, ArrayList <ArrayList <String>> values) {
        StripsAction a;
        if(name.startsWith("macro-")) {
            a = new StripsAction(name.substring(6), true);
            a.setOriginalMacro(true);
            originalMacroActions.add(a);
            setActionParameters(values.get(0), a);
            PDDLPredicate pp = createPDDL_package(values.get(1), a);
            a.setPrecondition(pp);
        }
        else if(name.startsWith("nonr-")) {
            a = new StripsAction(name.substring(5), true);
            a.setOriginalInSearchMacro(true);
            originalInSearchActions.add(a);
            setActionParameters(values.get(0), a);
            PDDLPredicate pp = createPDDL_package(values.get(1), a);
            a.setPrecondition(pp);
        }
        else {
            a = new StripsAction(name, false);
            actions.add(a);
            setActionParameters(values.get(0), a);
            PDDLPredicate pp = createPDDL_package(values.get(1), a);
            a.setPrecondition(pp);
        }
    
        PDDLPredicate pp = createPDDL_package(values.get(2), a);  
        a.setEffect(pp);
        a.updateTypeInPredicate();
    }
    
    public void createSelectedAction(String name, ArrayList <ArrayList <String>> values) {
        StripsAction a;
        
        a = new StripsAction(name.substring(6), true);
        a.setSelected(true);
              
        setActionParameters(values.get(0), a);
        PDDLPredicate pp1 = createPDDL_package(values.get(1), a);  
        a.setPrecondition(pp1);
        PDDLPredicate pp2 = createPDDL_package(values.get(2), a);  
        a.setEffect(pp2);
        a.updateTypeInPredicate();
    }
    
    private void setActionParameters(ArrayList <String> p, StripsAction ac) {
    	// mark the position of last argument added
        p = removeParentheses(p);
    	int index = 0;
        for(int i = 1; i < p.size(); i ++) {
            // if parameters have types
            if(p.get(i).equals("-")) {
                StripsType t = new StripsType(p.get(i+1));                
                for(int j = index; j < i; j ++) {;
                    StripsArgument argument = new StripsArgument(p.get(j), types.get(types.indexOf(t)));
                    ac.addParameters(argument); 
                }                      
                i = i+1;
                index = i+1;
            }
            // if parameters do not have types
            else {
                if(i == p.size()-1 && index == 0) {
                    StripsType t = new StripsType("");
                    for(int j = 0; j < p.size(); j ++) {
                        StripsArgument argument = new StripsArgument(p.get(j), t);
                        ac.addParameters(argument); 
                    } 
                }
            }           
        }
    }
    
    // recursive
    // Array list of string e to pddl_package
    private PDDLPredicate createPDDL_package(ArrayList <String> e, StripsAction ac) {
        e = removeParentheses(e);        
        int indicator = allPrefix.checkForPrefix(e.get(0));
        
        // not a prefix, so a predicate or a function
        if(indicator == 0) {
            boolean truthValue = true;
            if((e.get(0).equals("not"))) {
                truthValue = false;
                e.remove(0);
                e = removeParentheses(e);
            }
            PDDLPredicate_logical pr = new PDDLPredicate_logical(e.get(0));
            pr.setTruthValue(truthValue);
            boolean found = false;
            // find the predicate in the predicates list
            for(PDDLPredicate_logical temp : predicates) {
                // found in predicate list
                if(temp.getName().equals(pr.getName())) {
                    found = true;
                    ArrayList<StripsArgument> newArgList = new ArrayList<>();
                    // get the argument list of the found predicate
                    for(StripsArgument a : temp.getArgument()) {
                        StripsArgument newOne = new StripsArgument(a);
                        newArgList.add(newOne);
                    }
                    // reset the value of the argument if it has a value
                    // otherwise reset name
                    for(int i = 1; i < e.size(); i ++) {
                        // reset value
                        if(!(e.get(i).charAt(0) == '?')) {                                
                            newArgList.get(i-1).setValue(e.get(i));
                        }
                        // reset name
                        else {
                            newArgList.get(i-1).setName(e.get(i));
                        }
                    }
                    // return predicate
                    pr.setArgument(newArgList);
                    return pr;
                }
            }
            // find the predicate in the functions list
            if(!found) {               
                for(PDDLPredicate_logical temp : functions) {
                    // found in predicate list
                    if(temp.getName().equals(pr.getName())) {
                        found = true;
                        ArrayList<StripsArgument> newArgList = new ArrayList<>();
                        // get the argument list of the found predicate
                        for(StripsArgument a : temp.getArgument()) {
                            StripsArgument newOne = new StripsArgument(a);
                            newArgList.add(newOne);
                        }
                        // reset the value of the argument if it has a value
                        // otherwise reset name
                        for(int i = 1; i < e.size(); i ++) {
                            // reset value
                            if(!(e.get(i).charAt(0) == '?')) {                                
                                newArgList.get(i-1).setValue(e.get(i));
                            }
                            // reset name
                            else {
                                newArgList.get(i-1).setName(e.get(i));
                            }
                        }
                        // add predicate to effect
                        pr.setArgument(newArgList);
                        return pr;
                    }
                }
            }
            // create a new predicate
            if(!found) {
                ArrayList<StripsArgument> para = ac.getParameter();                 
                // add argument for the predicate
                // find argument type from the action's parameter list
                for(int i = 1; i < e.size(); i++) {
                    // if the argument is not a constant
                    if(e.get(1).charAt(0) == '?') {
                        StripsArgument a = new StripsArgument(e.get(i));
                        for(StripsArgument temp : para) {
                            if(temp.getName().equals(a.getName())) {
                                a.setType(temp.getType());
                                break;
                            }
                        }
                        pr.addArgument(a);
                    }
                    // if the argument is a constant
                    else {
                        StripsArgument a = new StripsArgument("?" + e.get(i));
                        a.setValue(e.get(i));
                        for(StripsConstant c : constants) {
                            if(c.getValue().equals(e.get(i))) {
                                a.setType(c.getType());
                                break;
                            }
                        }
                        pr.addArgument(a);
                    }
                }
                return pr;
            }
        }
        // is a prefix
        else {
            PDDLPredicate_package plp = new PDDLPredicate_package(e.get(0));
            // stack for parenthesis matching
            Stack <String> stack = new Stack <>();
            // logical prefix
            if(indicator == 1) {
                plp.setLogical(true);
                ArrayList <String> temp = new ArrayList <>();
                int index = 1;                
                int counter = 0;
                for(int i = index; i < e.size(); i++) { 
                    String s = e.get(i);
                    temp.add(s);                    
                    if(s.equals("(")) {
                        stack.push(s);
                        continue;
                    }
                    if(s.equals(")")) {
                        stack.pop();
                        if(stack.isEmpty()) {
                            PDDLPredicate p = createPDDL_package(temp, ac);
                            counter = counter + 1;
                            plp.addPredicate(p);                          
                            temp.clear();
                            continue;
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
            return plp;
        }
        return null;
    }

    // remove the first and last "()"
    private ArrayList <String> removeParentheses(ArrayList <String> input) {
        input.remove(0);
        while (!input.remove(input.size()-1).equals(")")) {
            
        }
        ArrayList <String> output =  input;
        return output;
    }    
    
    // assume A is a macro-action, B is simple
    // or both non-macro
    public ArrayList<StripsAction> mergeAction(StripsAction A, StripsAction B, int round) {
        StripsAction B_copy = new StripsAction(B);
        B_copy.setFirstAction(false);
        
        StripsAction merged = new StripsAction(A.getName() + "-and-" + B_copy.getName(), true);
        ArrayList <StripsArgument> mergedPara = mergeMacroPara(A, B_copy, round);
        for(StripsArgument a : mergedPara) {
            merged.addParameters(a);
    	}

        merged.setPrecondition(mergeMacroPrecon(A, B_copy, round));
        merged.setEffect(mergeMacroEffect(A, B_copy, round));

    	macroActions.addAll(createMacroInstance(merged, true));
    	return macroActions;
    }

    public ArrayList<StripsAction> mergeActionTraditional(StripsAction A, StripsAction B, int round) {
        StripsAction B_copy = new StripsAction(B);
        B_copy.setFirstAction(false);

        StripsAction merged = new StripsAction(A.getName() + "-and-" + B_copy.getName(), true);
        ArrayList <StripsArgument> mergedPara = mergeMacroPara(A, B_copy, round);
        for(StripsArgument a : mergedPara) {
            merged.addParameters(a);
        }

        merged.setPrecondition(mergeMacroPrecon(A, B_copy, round));
        merged.setEffect(mergeMacroEffect(A, B_copy, round));

        ArrayList<String> A_args_names = new ArrayList<>();
        for(StripsArgument arg : A.getParameter()) {
            A_args_names.add(arg.getName() + "_0");
        }
        ArrayList<String> B_args_names = new ArrayList<>();
        for(StripsArgument arg : B.getParameter()) {
            B_args_names.add(arg.getName());
        }
        merged.setConstituent_actions(A.getName(), A_args_names);
        if(!A.getName().equals(B.getName()))
            merged.setConstituent_actions(B.getName(), B_args_names);
        else
            merged.setConstituent_actions("duplicate-" + B.getName(), B_args_names);

        macroActions.addAll(createMacroInstance(merged, false));
        return macroActions;
    }
       
    private ArrayList <StripsArgument> mergeMacroPara(StripsAction A, StripsAction B, int round) {
    	ArrayList <StripsArgument> toReturn = new ArrayList <>();
    	ArrayList <StripsArgument> ACopy = new ArrayList <>();
    	// copy parameters of A into a new list
    	for(StripsArgument para : A.getParameter()) {
    		StripsArgument temp = new StripsArgument(para);
    		ACopy.add(temp);
    	}
    	for(StripsArgument p : ACopy) {
    		String name = p.getName();
    		p.setName(name + "_" + round);
    		toReturn.add(p);
    	}
        toReturn.addAll(B.getParameter());
    	return toReturn;
    }
    
    private PDDLPredicate mergeMacroPrecon(StripsAction A, StripsAction B, int round) {
    	if(A.getPrecondition() instanceof PDDLPredicate_logical) {
            PDDLPredicate_logical A_precon_copy = new PDDLPredicate_logical((PDDLPredicate_logical) A.getPrecondition());
            A_precon_copy.add_round_number_to_all_arguments(round);
            if(B.getPrecondition() instanceof PDDLPredicate_logical) {                
                return ((PDDLPredicate_logical) B.getPrecondition()).merge_simple(A_precon_copy, true);
            }
            else {
                return ((PDDLPredicate_package) B.getPrecondition()).merge_simple(A_precon_copy, true);
            }
        }
        else{
            PDDLPredicate_package A_precon_copy = new PDDLPredicate_package((PDDLPredicate_package) A.getPrecondition());
            A_precon_copy.add_round_number_to_all_arguments(round);
            if(B.getPrecondition() instanceof PDDLPredicate_logical) {                
                return ((PDDLPredicate_logical) B.getPrecondition()).merge_simple(A_precon_copy, true);
            }
            else {
                return ((PDDLPredicate_package) B.getPrecondition()).merge_simple(A_precon_copy, true);
            }
        }
    }
    
    private PDDLPredicate mergeMacroEffect(StripsAction A, StripsAction B, int round) {
    	if(A.getEffect() instanceof PDDLPredicate_logical) {
            PDDLPredicate_logical A_eff_copy = new PDDLPredicate_logical((PDDLPredicate_logical) A.getEffect());
            A_eff_copy.add_round_number_to_all_arguments(round);
            return A_eff_copy.merge_simple(B.getEffect(), false);
        }
        else{
            PDDLPredicate_package A_eff_copy = new PDDLPredicate_package((PDDLPredicate_package) A.getEffect());
            A_eff_copy.add_round_number_to_all_arguments(round);
            return A_eff_copy.merge_simple(B.getEffect(), false);
        }
    }
    
    // create different versions of the same macro action
    private ArrayList <StripsAction> createMacroInstance(StripsAction A, boolean relaxed) {    
        // list to return
        ArrayList <StripsAction> toReturn = new ArrayList <>();
        
        // temporal unmodified result
        ArrayList <StripsAction> tempResult = new ArrayList <>();
        
        int paraSize = A.getParameter().size();
        
        // if no parameters, then return the action itself
        if(paraSize == 0) {
            toReturn.add(A);
            return toReturn;
        }
        
        // get the list of pairs of parameters that are of the same type
        ArrayList<Integer[]> indexPair = new ArrayList<>();
        for(int i = 0; i < paraSize - 1; i ++) {
            for(int j = i + 1; j < paraSize; j ++) {
                if(A.getParameter().get(i).getType().equals(A.getParameter().get(j).getType())) {
                    Integer[] intArray = {i, j};
                    indexPair.add(intArray);                    
                }
            }
        }
        
        // if no parameters of the same type, then return the action itself
        if(indexPair.size() == 0) {
            toReturn.add(A);
            return toReturn;
        }
        
        // create an equal and a not equal predicate for each pair
        ArrayList <PDDLPredicate_logical[]> predicateList = new ArrayList <>();
        for(Integer[] index : indexPair) {
            PDDLPredicate_logical equalPredicate = new PDDLPredicate_logical("=");
            for(Integer i : index) {
                equalPredicate.addArgument(A.getParameter().get(i));
            }
            PDDLPredicate_logical notEqualPredicate = new PDDLPredicate_logical(equalPredicate);
            notEqualPredicate.setTruthValue(false);
            PDDLPredicate_logical[] p = {equalPredicate, notEqualPredicate};
            predicateList.add(p);
        }
        if(predicateList.size() > 20) {
            return toReturn;
        }
        
        // populate toReturn with the correct number of possible actions        
        double possibleActionNumber = Math.pow(2, predicateList.size());
        for(int k = 0; k < possibleActionNumber; k ++) {
            StripsAction newAction = new StripsAction(A);
            newAction.setName(A.getName() + "-" + k);
            tempResult.add(newAction);
        }
        
        // get all the possible combinations of the predicates
        PDDLPredicate_logical[] head = predicateList.remove(0);
        ArrayList<ArrayList<PDDLPredicate_logical>> combi = getAllCombinations(head, predicateList);
        
        // add each predicate combination to an action
        for(int l = 0; l < combi.size(); l ++) {
            for(PDDLPredicate_logical p : combi.get(l)) {
                PDDLPredicate_logical p_copy = new PDDLPredicate_logical(p);
                tempResult.get(l).addPrecon(p_copy);

                tempResult.get(l).addBindingPair(p);
            }
        }
        
        total_macro_generated = total_macro_generated + tempResult.size();
        
        // refine tempResult
        out: for(StripsAction a : tempResult) {
            // remove all delete effect in a for relaxed macros
            if(relaxed) {
                String newName = "relaxed-" + a.getName();
                a.setName(newName);
                a.remove_all_delete_effects();
            }

            // replace equal & simplify precon & eff
            a.replace_equal_arguments();

            // remove predicates in effect that are already in precon
            boolean effect_valid = a.remove_precon_from_effect_new();
            boolean valid = a.check_validity() && effect_valid;

            if(valid) {
                a.setFirstAction(true);
                // remove redundant macro-actions
                in: for (StripsAction ac : toReturn) {
                    boolean compare = a.compareActions(ac);
                    if(compare) {
                        pruned_during_generation = pruned_during_generation + 1;
                        continue out;
                    }
                }
                toReturn.add(a);
            }
            else {
                pruned_during_generation = pruned_during_generation + 1;
            }
        }

        return toReturn;
    }
    
    // a recursive method to get all the possible predicate combinations
    private ArrayList<ArrayList<PDDLPredicate_logical>> getAllCombinations(PDDLPredicate_logical[] p, ArrayList<PDDLPredicate_logical[]> list) {
        ArrayList<ArrayList<PDDLPredicate_logical>> toReturn = new ArrayList<>();
        if(list.size() == 0) { 
            ArrayList<PDDLPredicate_logical> list1 = new ArrayList<>();
            list1.add(p[0]);
            ArrayList<PDDLPredicate_logical> list2 = new ArrayList<>();
            list2.add(p[1]);
            toReturn.add(list1);
            toReturn.add(list2);
            return toReturn;
        }
        else {
            PDDLPredicate_logical[] head = list.remove(0);
            ArrayList<ArrayList<PDDLPredicate_logical>> temp = getAllCombinations(head, list);
            for(ArrayList<PDDLPredicate_logical> incompleteList : temp) {
                ArrayList<PDDLPredicate_logical> anotherList1 = new ArrayList<>();
                anotherList1.add(p[0]);
                anotherList1.addAll(incompleteList);
                toReturn.add(anotherList1);
                ArrayList<PDDLPredicate_logical> anotherList2 = new ArrayList<>();
                anotherList2.add(p[1]);
                anotherList2.addAll(incompleteList);
                toReturn.add(anotherList2);
            }
        }
        return toReturn;
    }
    
    public void removeDuplicateAction(int macroStartIndex) {
        System.out.print("Remove duplicate...");
    	ArrayList <StripsAction> newList = new ArrayList <>();

        // check each macro against the original actions
    	out: for(int i = macroStartIndex; i < macroActions.size(); i ++) {
    		for(StripsAction a : actions) {
    		    // if the two actions are equal
    		    if (macroActions.get(i).compareActions(a)) {
    			    continue out;
    		    }    			
    		}

    		newList.add(macroActions.get(i));
    	}
        
    	ArrayList <StripsAction> newList2 = new ArrayList <>();
    	newList2.addAll(macroActions.subList(0, macroStartIndex));
        if(newList.size() > 0) {
           newList2.add(newList.get(0)); 
        }
        // check each macro against all other macros
    	out: for(int i = 1; i < newList.size(); i ++) {
    		for(StripsAction a : newList2) {
    		    // if the two actions are equal
    		    if (newList.get(i).compareActions(a)) {
    			    continue out;
    		    } 
    		}
    		newList2.add(newList.get(i));
    	}
    	macroActions = newList2;
        System.out.println("done.");
    }

    private void createRandomAction(int precon_number, int eff_number) {
        Random r = new Random();
        String name = Integer.toString(precon_number) + "-" + Integer.toString(eff_number) + "-" + r.nextInt();
        StripsAction a = new StripsAction(name, true);
        PDDLPredicate precon = createRandomAndPredicatePackage(precon_number, true);
        PDDLPredicate eff = createRandomAndPredicatePackage(eff_number, false);
        a.setPrecondition(precon);
        a.setEffect(eff);
        a.remove_precon_from_effect();

        ArrayList <StripsArgument> arg = new ArrayList <>();
        arg.addAll(precon.get_all_arguments());
        arg.addAll(eff.get_all_arguments());
        a.setParameters(arg);
        a.remove_duplicate_parameters();

        if(a.check_validity()) {
            randomActions.add(a);
        }
    }

    public void createSomeRandomActions(int required_number) {
        while(randomActions.size() < required_number) {
            Random random = new Random();
            int p = random.nextInt(3) + 1;
            int e = random.nextInt(3) + 1;
            createRandomAction(p, e);
        }

        System.out.println("creation done");

        removeDuplicateRandomActions(0);

        createRandomCounter ++;
        if(createRandomCounter > 10) {
            createRandomCounter = 0;
            return;
        }

        if(randomActions.size() < required_number) {
            System.out.println("size after remove: " + randomActions.size());
            createSomeRandomActions(required_number);
        }
    }

    public void removeDuplicateRandomActions(int randomStartIndex) {
        ArrayList <StripsAction> newList = new ArrayList <>();
        // check each macro against the original actions
        out: for(int i = randomStartIndex; i < randomActions.size(); i ++) {
            for(StripsAction a : actions) {
                // if the two actions are equal
                if (randomActions.get(i).compareActions(a)) {
                    continue out;
                }
            }

            newList.add(randomActions.get(i));
        }

        ArrayList <StripsAction> newList2 = new ArrayList <>();
        newList2.addAll(randomActions.subList(0, randomStartIndex));

        // check each macro against all other macros
        out: for(int i = 0; i < newList.size(); i ++) {
            for(StripsAction a : newList2) {
                // if the two actions are equal
                if (newList.get(i).compareActions(a)) {
                    continue out;
                }
            }
            newList2.add(newList.get(i));
        }

        randomActions = newList2;
    }

    private PDDLPredicate createRandomAndPredicatePackage(int predicate_number, boolean precon) {
        // create random "and" package
        ArrayList<PDDLPredicate> list = new ArrayList<>();
        for(int i = 0; i < predicate_number; i ++) {
            Random random = new Random();
            int index = random.nextInt(predicates.size());
            PDDLPredicate_logical new_p = predicates.get(index).copy();
            new_p.setTruthValue(random.nextBoolean());
            list.add(new_p);
        }

        PDDLPredicate_package p = new PDDLPredicate_package("and", list);
        PDDLPredicate toReturn = p.simplify(precon);

        return toReturn;
    }

    public void createMutexGroupDestroyingActions() {
        Random r = new Random();

        for(PDDLPredicate_logical p : predicates){
            PDDLPredicate_logical negated = p.copy();
            negated.negate();

            String name_1 = "unary-" + r.nextInt();
            StripsAction a_1 = new StripsAction(name_1, true);
            a_1.setPrecondition(p.copy());
            a_1.setEffect(negated.copy());
            ArrayList <StripsArgument> arg_1 = new ArrayList <>();
            arg_1.addAll(a_1.getPrecondition().get_all_arguments());
            a_1.setParameters(arg_1);
            a_1.remove_duplicate_parameters();
            randomActions.add(a_1);

            String name_2 = "unary-" + r.nextInt();
            StripsAction a_2 = new StripsAction(name_2, true);
            a_2.setPrecondition(negated.copy());
            a_2.setEffect(p.copy());
            ArrayList <StripsArgument> arg_2 = new ArrayList <>();
            arg_2.addAll(a_2.getPrecondition().get_all_arguments());
            a_2.setParameters(arg_2);
            a_2.remove_duplicate_parameters();
            randomActions.add(a_2);
        }
    }

    public void createMixedActions(boolean includeNonRelaxed) {
        System.out.println("Creating mix...");
        for(int i = 0; i < getActions().size(); i ++) {
            for(int j = 0; j < getActions().size(); j ++) {
                mergeAction(getActions().get(i), getActions().get(j), 0);
            }
        }
        System.out.println("Relaxed done...");

        if(includeNonRelaxed) {
            for(int i = 0; i < getActions().size(); i ++) {
                for(int j = 0; j < getActions().size(); j ++) {
                    mergeActionTraditional(getActions().get(i), getActions().get(j), 0);
                }
            }
            System.out.println("Traditional done...");
        }

        removeDuplicateAction(0);
        System.out.println("Final macro action size: " + getAllMacroActions().size());

        int random_number = getAllMacroActions().size();
        createSomeRandomActions(random_number);
        System.out.println("Random size: " + getAllRandomActions().size());
        createMutexGroupDestroyingActions();
        System.out.println("Random size: " + getAllRandomActions().size());
    }
    
    // add the specified macro action to the original domain
    public void addMacroToSelected(int index) {
        selectedMacroActionIndex.add(index);
        macroActions.get(index).setSelected(true);
    }
    
    // discard all macros and only keep the specified macro
    public void discardAllMacrosExcept(int index) {
        selectedMacroActionIndex.clear();
        selectedMacroActionIndex.add(0);
        ArrayList<StripsAction> new_list = new ArrayList<> ();
        macroActions.get(index).setSelected(true);
        new_list.add(macroActions.get(index));
        macroActions.clear();
        macroActions = new_list;
        usefulMacroActionIndex.clear();
        usefulMacroActionIndex.add(0);
    }
    
    public void reorderActions(ArrayList <String> order) {
        int macro_size = order.size() - actions.size();
        for(int i = 0; i < actions.size(); i ++) {
            int originalIndex = 0;
            for(int j = 0; j < actions.size(); j ++) {
                if(actions.get(j).getName().equalsIgnoreCase(order.get(i))) {
                    originalIndex = j;
                    break;
                }
            }
            Collections.swap(actions, i, originalIndex);
        }

        if(macro_size > 0) {
            for(int k = actions.size(); k < actions.size() + originalMacroActions.size(); k ++) {
                int originalIndex = 0;
                for(int l = 0; l < originalMacroActions.size(); l ++) {
                    String name = "macro-" + originalMacroActions.get(l).getName();
                    if(name.equalsIgnoreCase(order.get(k))) {
                        originalIndex = l;
                        break;
                    }
                }
                Collections.swap(originalMacroActions, k - actions.size(), originalIndex);
            }

            int insearch_size = macro_size - originalMacroActions.size();
            if(insearch_size > 0) {
                for(int m = actions.size() + originalMacroActions.size(); m < order.size(); m ++) {
                    int originalIndex = 0;
                    for(int n = 0; n < originalInSearchActions.size(); n ++) {
                        String name = "nonr-" + originalInSearchActions.get(n).getName();
                        if(name.equalsIgnoreCase(order.get(m))) {
                            originalIndex = n;
                            break;
                        }
                    }
                    Collections.swap(originalInSearchActions, m - actions.size() - originalMacroActions.size(), originalIndex);
                }
            }
        }
    }
    
    // used during parsing
    public void reorderMacroActions(ArrayList <String> order) {        
        for(int i = 0; i < order.size(); i ++) {
            int originalIndex = 0;
            for(int j = 0; j < macroActions.size(); j ++) {
                String actionName = "macro-" + macroActions.get(j).getName();
                if(actionName.equalsIgnoreCase(order.get(i))) {
                    originalIndex = j;
                    break;
                }
            }
            Collections.swap(macroActions, i, originalIndex);
        }        
    }
       
    public void reorderSelectedMacroActions(ArrayList <String> order) {        
        for(int i = 0; i < order.size(); i ++) {
            int originalIndex = 0;
            for(int j = 0; j < selectedMacroActionIndex.size(); j ++) {
                String actionName = "macro-" + macroActions.get(selectedMacroActionIndex.get(j)).getName();
                if(actionName.equalsIgnoreCase(order.get(i))) {
                    originalIndex = j;
                    break;
                }
            }
            Collections.swap(selectedMacroActionIndex, i, originalIndex);
        }      
    }
    
    public void setScore(TreeMap<Integer, Double> s) {
        for(Map.Entry<Integer, Double> entry : s.entrySet()) {
            if(entry.getKey() != -1) {
                System.out.println("setting score: " + entry.getKey() + " " + entry.getValue());
                macroActions.get(entry.getKey()).setScore(entry.getValue());
            }
        }
    }
    
    public void pruneMacros(int macroListStartIndex) {
        for(int i = macroListStartIndex; i < macroActions.size(); i ++) {
            if(macroActions.get(i).getScore() > 3.0) {
                usefulMacroActionIndex.add(i);
            }
            else {
                pruned_during_ranking ++;
            }
        }
        
        // only keep the best 50 actions
        Collections.sort(usefulMacroActionIndex, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                Double o2_score = macroActions.get(o2).getScore();
                Double o1_score = macroActions.get(o1).getScore();
                return o2_score.compareTo(o1_score);
            }
        });
        if(usefulMacroActionIndex.size() > 50) {
            pruned_during_ranking = pruned_during_ranking + (usefulMacroActionIndex.size() - 50);
            usefulMacroActionIndex.subList(50, usefulMacroActionIndex.size()).clear();
        }
        
        System.out.println("Useful macro index: " + usefulMacroActionIndex);
    }
    
    public boolean isMacroUseful(int i) {
        if(usefulMacroActionIndex.contains(i) && !selectedMacroActionIndex.contains(i))
            return true;
        else
            return false;
    }

    public void remove_macros_except(HashSet<Integer> toKeep) {
        ArrayList <StripsAction> newList = new ArrayList<>();
        for(Integer i : toKeep) {
            newList.add(macroActions.get(i));
        }
        macroActions = newList;
    }
    
    public boolean isMacroSelected(int i) {
        return selectedMacroActionIndex.contains(i);
    }

    public void incrementMergeIndex() {
        mergeIndex = mergeIndex + 1;
    }
    
    public void resetMergeIndex() {
        mergeIndex = 0;
    }
    
    public void setOriginalDomainPath(String s) {
        originalDomainPath = s;
    }
    
    public String requirementsToString() {
    	String toReturn = "(:requirements \n    ";
        for(String r : requirements) {
            toReturn = toReturn + r + " ";
        }
        if(!requirements.contains(":equality")) {
            toReturn = toReturn + ":equality ";
        }
        toReturn = toReturn + "\n) \n";
    	return toReturn;
    }

    public void increase_macro_counter(HashSet<Integer> indexes) {
        for(Integer i : indexes) {
            if(macro_counter.containsKey(i)) {
                int original_count = macro_counter.get(i);
                macro_counter.put(i, original_count + 1);
            }
            else {
                macro_counter.put(i, 1);
            }
        }
    }
    
    public String allTypesToString() {
        if(types.size() == 0) {
            return "";
        }
    	String toReturn = "(:types \n";
    	for(StripsType t : types) {
    		toReturn = toReturn + "    " + t + " \n";
    	}
    	toReturn = toReturn + ") \n";
    	return toReturn;
    }
    
    public String allConstantsToString() {
    	String toReturn = "(:constants \n";
    	if(!constants.isEmpty()) {
    		for(StripsConstant c : constants) {
        		toReturn = toReturn + "    " + c + "\n";
        	}
        	toReturn = toReturn + ") \n";
        	return toReturn;
    	}    	
    	else {
    		return "";
    	}
    }
    
    public String allPredicatesToString() {
    	String toReturn = "(:predicates \n";
        String toReturn_temp = "";
    	for(int i = 0; i < predicates.size(); i ++) {
            PDDLPredicate_logical p = predicates.get(i);
            ArrayList<PDDLPredicate_logical> to_be_merged = new ArrayList<>();
            if(i == predicates.size() - 1) {
                toReturn_temp = toReturn_temp + "    " + p.toStringFull(to_be_merged) + "\n";
                break;
            }
            for(int j = i + 1; j < predicates.size(); j ++) {
                if(!predicates.get(j).getName().equals(p.getName())) {
                    toReturn_temp = toReturn_temp + "    " + p.toStringFull(to_be_merged) + "\n";
                    i = j - 1;
                    break;
                }
                else {
                    to_be_merged.add(predicates.get(j));
                }
            }           
    	}
        
    	toReturn = toReturn + toReturn_temp + ") \n";
    	return toReturn;
    }
    
    public String allFunctionsToString() {
        if(functions.isEmpty()) {
            return "";
        }
    	String toReturn = "(:functions \n";
    	for(PDDLPredicate_logical f : functions) {
    	    toReturn = toReturn + "    " + f.toStringFull(new ArrayList<PDDLPredicate_logical>()) + "\n";
    	}
    	toReturn = toReturn + ") \n";
    	return toReturn;
    }
    
    public String allActionsToString() {
    	String toReturn = "";
    	for(StripsAction a : actions) {
    		toReturn = toReturn + a;
    	}

    	return toReturn;
    }
    
    public String allMacrosToString() {
    	String toReturn = "";
    	for(StripsAction a : macroActions) {
    		toReturn = toReturn + a;
    	}
    	return toReturn;
    }
    
    public String allSelectedMacrosToString() {
    	String toReturn = "";
    	for(Integer i : selectedMacroActionIndex) {
            StripsAction a = macroActions.get(i);
    	    toReturn = toReturn + a;
    	}
    	return toReturn;
    }

    public String allOriginalMacrosToString() {
        String toReturn = "";
        for(StripsAction a : originalMacroActions) {
            toReturn = toReturn + a;
        }
        return toReturn;
    }

    public String allOriginalInSearchToString() {
        String toReturn = "";
        for(StripsAction a : originalInSearchActions) {
            toReturn = toReturn + a;
        }
        return toReturn;
    }
    
    public ParserManager getParserManager() {
        return pm;
    }
    
    public String getDomainName() {
        return domainName;
    }
    
    public ArrayList <String> getRequirements() {
        return requirements;
    }
    
    public ArrayList <StripsType> getTypes() {
        return types;
    }
    
    public ArrayList <StripsConstant> getConstants() {
        return constants;
    }
    
    public ArrayList <PDDLPredicate_logical> getPredicates() {
        return predicates;
    }
    
    public ArrayList <PDDLPredicate_logical> getFunctions() {
        return functions;
    }
    
    public ArrayList <StripsAction> getActions() {
        return actions;
    }
    
    public ArrayList <StripsAction> getMacroActions() {
        return macroActions;
    }
    
    
    public ArrayList <Integer> getSelectedMacroActionIndex() {
        return selectedMacroActionIndex;
    }
    
    public ArrayList <StripsAction> getAllMacroActions() {
        ArrayList <StripsAction> all = macroActions;
        return all;
    }

    public ArrayList <StripsAction> getAllRandomActions() {
        return randomActions;
    }
    
    public String getOriginalDomainPath() {
        return originalDomainPath;
    }
    
    public int getTotalMacroGenerated() {
        return total_macro_generated;
    }
    
    public int getPrunedDuringGeneration() {
        return pruned_during_generation;
    }
    
    public int getPrunedDuringRanking() {
        return pruned_during_ranking;
    }

    public TreeMap<Integer, Integer> getMacroCounter() {
        return macro_counter;
    }

    public int getOriginalMacrosSize() {
        return originalMacroActions.size();
    }

    public ArrayList <StripsAction> getOriginalMacros() {
        return originalMacroActions;
    }

    public int getOriginalInSearchSize() {
        return originalInSearchActions.size();
    }

    public ArrayList <StripsAction> getOriginalInSearch() {
        return originalInSearchActions;
    }
}
