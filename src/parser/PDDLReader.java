/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author danchengGao
 */
public class PDDLReader {
    
    private String domainName = "";
    private ArrayList <String> types = new ArrayList <> ();
    private ArrayList <String> constants = new ArrayList <> ();
    private ArrayList <String> predicates = new ArrayList <> ();
    private ArrayList <String> functions = new ArrayList <> ();
    private ArrayList <String> actions = new ArrayList <> ();
    private ArrayList <String> requirements = new ArrayList <> ();
    
    public PDDLReader(String path) {
        System.out.print("Reading domain...");
	    readFile(path);
        System.out.println("Done");
    }
    
    public void readFile(String domainPath) {
	    BufferedReader br = null;
	    FileReader fr = null;
		
	    try {
            fr = new FileReader(domainPath);
            br = new BufferedReader(fr);
            String line  = br.readLine();
            String temp;
            
            Pattern p = Pattern.compile("(\\bdomain \\b)(.+)(\\))");
            Matcher m = p.matcher(line);    	            
            while(!m.find()) {
            	line = br.readLine();
            	m = p.matcher(line);
            }
            domainName = m.group(2);
            
            while((line = br.readLine()) != null) { 
            	int indicator = matchTokens(line);	
            	switch (indicator) {
            	    case 0 : {            	    	
            	    	continue;
            	    }
            	    
            	    case 1 : {
            	    	outer : while(true) { 
            	    	    temp = br.readLine();
                            if(matchComment(temp) == true) {
                                continue outer;
                            }
            	    	    String[] tempArray = temp.split("\\s");            	    		
            	    	    for (String t : tempArray) {  
            	    		if (t.equals(")")) {
                        	    break outer;
                        	}
                            	if (!t.equals("")) {
                            	    types.add(t);                		
                            	}                             	                           	
            	    	    }
            	    	}
            	    	continue;
            	    }
            	    	
            	    case 2 : {                        
            	    	outer: while(true) { 
            	    	    temp = br.readLine();
                            if(matchComment(temp) == true) {
                                continue outer;
                            }
            	    	    String[] tempArray = temp.split("\\s");
            	    	    for (String t : tempArray) {                                
            	    		if (t.equals(")")) {
                                    break outer;
                        	}
                            	if (!t.equals("")) {
                            	    constants.add(t);                		
                            	}                            	
            	    	    }
            	    	}
            	    	continue;
            	    }
            	    
            	    case 3 : {
            	    	outer: while(true) { 
            	    	    temp = br.readLine();
                            if(matchComment(temp) == true) {
                                continue outer;
                            }
            	    	    String[] tempArray = temp.split("\\s");
            	    	    for (String t : tempArray) {
            	    		if (t.equals(")")) {
                        	    break outer;
                        	}
                            	if (!t.equals("")) {
                            	    predicates.add(t);                		
                            	}                            	
            	    	    }
            	    	}
            	    	continue;
            	    }
            	    
            	    case 4 : {           	        
            	    	temp = line;
            	    	while(temp != null) {
            	    	    // remove comment ;;
            	    	    Pattern p1 = Pattern.compile("([\\s]*)([;][;])(.+)");
                            Matcher m1 = p1.matcher(temp); 
                            if(m1.matches()) {
                                temp = br.readLine();
                                continue;
                            }
                            String[] tempArray = temp.split("\\s");
        	    	        for (String t : tempArray) {
                        	    if (!t.equals("")) {
                                    actions.add(t);                		
                        	    }                            	
        	    	        } 
            	    	    temp = br.readLine();	
            	    	} 
            	    	continue;
            	    }
                    
                    case 5 : {
                        String[] tempArray = line.split("\\s");
                        for (String t : tempArray) {
                            if(!t.equals("(:requirements") && !t.equals("(:requirement")) {
                                requirements.add(t); 
                            }
                            else
                                continue;
        	    	}
                        continue;
                    }
            	}
            	
            }   

            if (br != null)
		        br.close();
	        if (fr != null)
		        fr.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();             
        }
        catch(IOException ex) {
            ex.printStackTrace();                 
        }
    }
	
    private int matchTokens(String line) {
	    Pattern p1 = Pattern.compile("[(][:][a-zA-Z]+");
	
	    Matcher m1 = p1.matcher(line);
        if (m1.find()) {
	        return matchCase(m1.group());
        }
	    return 0;
		
    }
	
    private int matchCase (String m) {
	    if (m.equals("(:types")) {
	        return 1;
	    }
	    if (m.equals("(:constants")) {
	        return 2;
	    }
	    if (m.equals("(:predicates")) {
	        return 3;
	    }
	    if (m.equals("(:action")) {
	        return 4;
	    }
        if (m.equals("(:requirements") || m.equals("(:requirement")) {
	        return 5;
	    }
	    return 0;
    }
    
    private boolean matchComment(String line) {
        boolean isComment = false;        
        Pattern p = Pattern.compile("(\\s)*([;])(.)*");
        Matcher m = p.matcher(line);
	    if (m.find()) {
	        isComment = true;
        }
        return isComment;
    }
    
    public String getDomainName() {
        return domainName;        
    }
        
    public ArrayList <String> getTypes() {
        return types;        
    }
        
    public ArrayList <String> getPredicates() {
        return predicates;        
    }
	
    public ArrayList <String> getConstants() {
        return constants;        
    }
    
    public ArrayList <String> getFunctions() {
        return functions;        
    }
        
    public ArrayList <String> getActions() {
        return actions;        
    }
    
    public ArrayList <String> getRequirements() {
        return requirements;        
    }
}
