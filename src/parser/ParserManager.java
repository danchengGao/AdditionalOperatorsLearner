package parser;

import data.StripsDataManager;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private ArrayList<PDDLReader> pddlrList = new ArrayList<PDDLReader>();

    public ParserManager(String dpath) {
	    domainPath = dpath;
	    System.out.println("Parser starts...");
	    System.out.println("Parsing domain: " + domainPath);
        
        readPDDLFile(domainPath);
        
        dataMgr = new StripsDataManager(this);
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
        
        dataMgr.createDomainName(pddlrList.get(0).getDomainName());
            
        System.out.println("Done");
    }
        
    private void createRequirement() {
        System.out.print("Creating requirements...");
            
    	dataMgr.createRequirement(pddlrList.get(0).getRequirements());
            
        System.out.println("Done");
    }
	
    private void createType() {
        System.out.print("Creating types...");
            
        TypeParser tp = new TypeParser(pddlrList.get(0).getTypes());
        TreeMap <String, String> typeMap = tp.getTypes();
        for (Map.Entry <String, String> entry : typeMap.entrySet()) {
            dataMgr.createType(entry.getKey(), entry.getValue());
        } 
            
        System.out.println("Done");
    }
        
    private void createConstant() {
        System.out.print("Creating constants...");
            
        ConstantParser cp = new ConstantParser(pddlrList.get(0).getConstants()); 
        TreeMap <String, String> constantMap = cp.getConstants();
        for (Map.Entry <String, String> entry : constantMap.entrySet()) {
            if(entry.getValue() != null)
                dataMgr.createConstant(entry.getKey(), entry.getValue());
        } 
            
        System.out.println("Done");
    }
                       
    private void createPredicate() {
        System.out.print("Creating predicates...");
            
        PredicateParser pp = new PredicateParser(pddlrList.get(0).getPredicates()); 
        TreeMap <String, ArrayList <String[]>> predicateMap = pp.getPredicates();        
        for (Map.Entry <String, ArrayList <String[]>> entry : predicateMap.entrySet()) {           
            if(entry.getValue() != null)
                dataMgr.createPredicate(entry.getKey(), entry.getValue());
        }
            
        System.out.println("Done");
    }
        
    private void createAction() {
        System.out.print("Creating actions...");
            
        ActionParser ap = new ActionParser(pddlrList.get(0).getActions());
        TreeMap <String, ArrayList <ArrayList <String>>> actionMap = ap.getActions();
        ArrayList <String> order = ap.getActionOrder();
        for (Map.Entry <String, ArrayList <ArrayList <String>>> entry : actionMap.entrySet()) {
            dataMgr.createAction(entry.getKey(), entry.getValue());
        }
        dataMgr.reorderActions(order);
            
        System.out.println("Done");
    }
        
    public void createMacroAction() {
        System.out.print("Creating macro-actions...");
            
        ActionParser ap = new ActionParser(pddlrList.get(1).getActions());
        TreeMap <String, ArrayList <ArrayList <String>>> actionMap = ap.getActions();
        ArrayList <String> order = ap.getActionOrder();
        for (Map.Entry <String, ArrayList <ArrayList <String>>> entry : actionMap.entrySet()) {
            dataMgr.createAction(entry.getKey(), entry.getValue());
        } 
        dataMgr.reorderMacroActions(order);
            
        System.out.println("Done");
    }
        
    public void createSelectedMacroAction() {
        System.out.print("Creating selected macro-actions...");
            
        ActionParser ap = new ActionParser(pddlrList.get(2).getActions()); //
        TreeMap <String, ArrayList <ArrayList <String>>> actionMap = ap.getActions();
        ArrayList <String> order = ap.getActionOrder();
        for (Map.Entry <String, ArrayList <ArrayList <String>>> entry : actionMap.entrySet()) {
            dataMgr.createSelectedAction(entry.getKey(), entry.getValue());
        } 
        dataMgr.reorderSelectedMacroActions(order);
            
        System.out.println("Done");
    }
        
    public void readPDDLFile(String filePath) {
        PDDLReader pddlr = new PDDLReader(filePath);
        pddlrList.add(pddlr);
    }
        
    public ArrayList<String> readEvaluationFile(String filePath) {
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
                
            ArrayList<String> toReturn = new ArrayList<>();
            String line  = br.readLine();
            String[] line1 = line.split(" ");
            toReturn.add(line1[2]);
            line  = br.readLine();
            String[] line2 = line.split(" ");
            toReturn.add(line2[2]);
                
            if (fr != null)
	        fr.close();
            if (br != null)
		br.close();
                
            return toReturn;
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();  
        } 
        catch (IOException ex) {
            ex.printStackTrace();    
        }
        return null;
    }
    
    public StripsDataManager getData() {
        return dataMgr;
    }            

}
