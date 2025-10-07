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
public class StripsType {
    
    private String name;
    private StripsType parentType;
    private ArrayList<StripsType> subTypes = new ArrayList<>();
    
    public StripsType(String n) {
        name = n;
    }
    
    public void setName(String n) {
        name = n;
    }
    
    public void setParentType(StripsType p) {
        parentType = p;
        p.setSubType(this);
    }
    
    private void setSubType(StripsType p) {
        subTypes.add(p);        
    }
    
    public String getName() {
        return name;
    }
    
    public StripsType getParent() {
        return parentType;
    }
    
    public ArrayList<StripsType> getSubTypes() {
        return subTypes;
    }
    
    public boolean isSubtype(StripsType p) {
        if(this.parentType != null) {
            if(this.parentType.equals(p)) {
                return true;
            }
            else {
                return this.parentType.isSubtype(p);
            }
        }        
        return false;
    }
    
    // get subtypes at a specific level
    public ArrayList<StripsType> getSubtypes(StripsType p) {
        ArrayList<StripsType> toReturn = new ArrayList<>();
        if(this.getSubTypes().isEmpty()) {
            return toReturn;
        }
        else if(this.getSubTypes().contains(p)) {
            return this.getSubTypes();
        }
        else {
            boolean found = false;
            for(StripsType t : this.getSubTypes()) {                
                if(found == false) {
                    ArrayList<StripsType> result = t.getSubtypes(p);
                    // it contains p
                    if(!result.isEmpty()) {
                        found = true;
                        toReturn.addAll(result);
                    }
                    // it does not contain p
                    else {
                        toReturn.add(t);
                    }                    
                }
                else {
                    toReturn.add(t);
                }
            }
            if(found == true) {
                return toReturn;
            }            
            else {
                return new ArrayList<StripsType>();
            }
        }
    }

    // equals by name
    @Override
    public boolean equals(Object o) {
        if(this.name.equals(((StripsType)o).getName()))
            return true;
        else
            return false;
    }
    
    @Override
    public String toString() {
        if (this.parentType != null)
            return this.name + " - " + this.parentType.getName();
        else
            return this.name;
    }
    
}
