package parser;

import data.StripsDataManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author danchengGao
 */

public class ParserManager {
	
    private String domainPath = "";
    private StripsDataManager dataMgr;
    private PDDLReader pddlr;

    public ParserManager(String dpath) {
	    domainPath = dpath;
	    System.out.println("Parser starts...");
	    System.out.println("Parsing domain: " + domainPath);
        
        pddlr = new PDDLReader(domainPath);
        
        dataMgr = new StripsDataManager();
	    createDomainName();
        createRequirement();
        createType();
        createConstant();
        createPredicate();
        createAction();
        
        System.out.println("Parsing complete.\n");
    }
        
    private void createDomainName() {
        System.out.print("Creating domain name...");
        
        dataMgr.createDomainName(pddlr.getDomainName());
            
        System.out.println("Done");
    }
        
    private void createRequirement() {
        System.out.print("Creating requirements...");
            
    	dataMgr.createRequirement(pddlr.getRequirements());
            
        System.out.println("Done");
    }
	
    private void createType() {
        System.out.print("Creating types...");
            
        TypeParser tp = new TypeParser(pddlr.getTypes());
        TreeMap <String, String> typeMap = tp.getTypes();
        for (Map.Entry <String, String> entry : typeMap.entrySet()) {
            dataMgr.createType(entry.getKey(), entry.getValue());
        } 
            
        System.out.println("Done");
    }
        
    private void createConstant() {
        System.out.print("Creating constants...");
            
        ConstantParser cp = new ConstantParser(pddlr.getConstants()); 
        TreeMap <String, String> constantMap = cp.getConstants();
        for (Map.Entry <String, String> entry : constantMap.entrySet()) {
            if(entry.getValue() != null)
                dataMgr.createConstant(entry.getKey(), entry.getValue());
        } 
            
        System.out.println("Done");
    }
                       
    private void createPredicate() {
        System.out.print("Creating predicates...");
            
        PredicateParser pp = new PredicateParser(pddlr.getPredicates()); 
        TreeMap <String, ArrayList <String[]>> predicateMap = pp.getPredicates();        
        for (Map.Entry <String, ArrayList <String[]>> entry : predicateMap.entrySet()) {           
            if(entry.getValue() != null)
                dataMgr.createPredicate(entry.getKey(), entry.getValue());
        }
            
        System.out.println("Done");
    }
        
    private void createAction() {
        System.out.print("Creating actions...");
            
        ActionParser ap = new ActionParser(pddlr.getActions());
        TreeMap <String, ArrayList <ArrayList <String>>> actionMap = ap.getActions();
        ArrayList <String> order = ap.getActionOrder();
        for (Map.Entry <String, ArrayList <ArrayList <String>>> entry : actionMap.entrySet()) {
            dataMgr.createAction(entry.getKey(), entry.getValue());
        }
        dataMgr.reorderActions(order);
            
        System.out.println("Done");
    }

    public StripsDataManager getData() {
        return dataMgr;
    }            

}
