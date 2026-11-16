package parser;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author danchengGao
 */
public class TypeParser {
    
    private ArrayList <String> info = new ArrayList <> ();
    private TreeMap <String, String> map = new TreeMap <> ();
    
    public TypeParser(ArrayList <String> t) {
        info = t;
        parse();
    }
    
    private void parse() {
        int index = 0;
        for(int i = 0; i < info.size(); i++) {
            String key = info.get(i);            
            if(!key.equals("-")) {
                if(!map.containsKey(key))
                    map.put(key, null);
            }
            else {
                for(int j = index; j < i; j++) { 
                    map.put(info.get(j), info.get(i+1));
                }
                index = i+2;
            }            
        }
    }
    
    public TreeMap <String, String> getTypes() {
        return map;
    }
}
