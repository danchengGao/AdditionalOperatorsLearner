/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author danchengGao
 */
public class StripsConstant {
    
    private String value;
    private StripsType type;
    
    public StripsConstant(String v) {
        value = v;
        type = new StripsType("");
    }
    
    public StripsConstant(String v, StripsType t) {
        value = v;
        type = t;
    }
    
    public void setValue(String v) {
        value = v;
    }
    
    public void setType(StripsType p) {
        type = p;
    }
    
    public String getValue() {
        return value;
    }
    
    public StripsType getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object o) {
        return this.value.equals(((StripsConstant)o).getValue());
    }
    
    @Override
    public String toString() {
        if(!this.type.getName().equals(""))
            return this.value + " - " + this.type.getName();
        else
            return this.value;
    }
    
}
