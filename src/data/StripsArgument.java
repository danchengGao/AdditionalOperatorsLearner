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
public class StripsArgument implements Comparable<StripsArgument>{
    
    private String name;
    private StripsType type;
    // a constant
    private String groundedValue = "";
    
    private ArrayList<StripsType> extraTypes = new ArrayList<>();
    
    public StripsArgument(String n) {
        name = n;
        type = new StripsType("");
    }
    
    public StripsArgument(String n, StripsType t) {
        name = n;
        type = t;
    }
    
    // a copy constructor
    public StripsArgument(StripsArgument another) {
        this.name = another.name;
        this.type = another.type;
        this.groundedValue = another.groundedValue;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setType(StripsType t) {
        type = t;
    }
    
    public void setValue(String g) {
    	groundedValue = g;
    }
    
    public String getName() {
        return this.name;
    }
    
    public StripsType getType() {
        return this.type;
    }
    
    public String getGroundedValue() {
        return groundedValue;
    }
    
    @Override
    public boolean equals(Object o) {
        // has grounded value
        if(!groundedValue.equals("")) {
            if(this.groundedValue.equals(((StripsArgument)o).getGroundedValue()) && this.type.equals(((StripsArgument)o).getType())) {
                return true;
            }           
            else
                return false;
        }
        // no grounded value
        else {
            if(this.name.equals(((StripsArgument)o).getName()) && this.type.equals(((StripsArgument)o).getType())) {
                return true;
            }           
            else
                return false;
        }
    }
    
    @Override
    public int compareTo(StripsArgument another) {
    	return this.name.compareTo(another.getName()); 
    }
    
    @Override
    public String toString() {
        String toReturn = "";
    	if(!groundedValue.equals("")) {
            if(!type.getName().equals("")) {                
                if(extraTypes.isEmpty()) {
                    toReturn = this.groundedValue + " - " + this.type.getName() + "";
                }
                else {
                    toReturn = this.groundedValue + " - " + "(either " + this.type.getName();
                    for(StripsType t : extraTypes) {
                        toReturn = toReturn + " " + t.getName();
                    }
                    toReturn = toReturn + ")";
                }
            }
            else {
                toReturn = this.groundedValue  + "";
            }
        }
            
    	else {
            if(!type.getName().equals("")) {
                if(extraTypes.isEmpty()) {
                    toReturn = this.name + " - " + this.type.getName() + "";
                }
                else {
                    toReturn = this.name + " - " + "(either " + this.type.getName();
                    for(StripsType t : extraTypes) {
                        toReturn = toReturn + " " + t.getName();
                    }
                    toReturn = toReturn + ")";
                }
            }
            else {
                toReturn = this.name + "";
            }
        } 
        return toReturn;
    }
    
    public String toStringWithoutType() {
        String toReturn = "";
    	if(!groundedValue.equals("")) {
            toReturn = this.groundedValue  + " ";            
        }
            
    	else {
            toReturn = this.name + " ";            
        } 
        return toReturn;
    }
    
    public void addExtraType(StripsType t) {
        extraTypes.add(t);
    }
    
    public void clearExtraTypes() {
        extraTypes.clear();
    }
}
