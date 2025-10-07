package evaluator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author danchengGao
 */

public class RunFastDownward {
    
    private String originalDomainPath = "";
    private int problemIndex = -1;
    private String problemName = "";    
    private String dirName = "";
    private int domainIndex = -1;
    private double timeout = 0.0;     
    
    private boolean successful = false;
    
    private double search_time = 0.0;
    private double total_time = 0.0;
    private double expanded = 0.0;

    private boolean valid = false;
    private ArrayList<String> plan = new ArrayList<>();
    
    public RunFastDownward(String dir, int di, int pi, String p, double t, String originalPath) {
        originalDomainPath = originalPath;
        problemName = p;
        dirName = dir;
        domainIndex = di;
        problemIndex = pi;
        if(t <= 60) {
            timeout = t;
        }
        else {
            timeout = 60;
        }
        if(pi != 0) {
            run();
        }           
        if(!successful) {
            search_time = 10 * timeout;
            total_time = 10 * timeout;
        }
    }
    
    private void run() {
        String domainName = "";
        if(domainIndex != 0) {
            domainName = dirName + "macroGen-" + domainIndex + ".pddl";
        }
        else {
            domainName = originalDomainPath;
        }
        System.out.println("Solving: " + domainName + " " + problemName);
        
        Pattern pattern0 = Pattern.compile("(\\bExpanded \\b)([0-9]+)(\\b state\\b)([\\(][s][\\)][\\.])");
        Pattern pattern1 = Pattern.compile("(\\bSearch time: \\b)([0-9]+[\\.]*[0-9]*[e]*[-]*[0-9]*)([s])");
        Pattern pattern2 = Pattern.compile("(\\bTotal time: \\b)([0-9]+[\\.]*[0-9]*[e]*[-]*[0-9]*)([s])");
        
        String s = null;
        String command = "./test-paramILS.sh " + domainName + " " + problemName + " " + (int) timeout + "s";
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor((int) Math.ceil(timeout), TimeUnit.SECONDS);
            System.out.println("done");
            
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError_with_macro = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            // just in case total time is not found
            boolean foundTotal = false;
            boolean noSolutionFound = false;
            while((s = stdInput.readLine()) != null) {
                if(s.equals("Error: Could not read file: " + domainName) || s.equals("timeout")) {
                    return;
                }                
                
                Matcher m0 = pattern0.matcher(s);                
                
                if(m0.find()) {
                    double expandedStates = Double.parseDouble(m0.group(2));
                    String output = domainIndex + "-with macro\n" + m0.group(0) + "\n";
                    
                    while((s = stdInput.readLine()) != null) {
                        Matcher m1 = pattern1.matcher(s);
                        Matcher m2 = pattern2.matcher(s);
                        output = output + s + "\n";                        
                        
                        if(m1.find()) {
                            successful = true;
                            search_time = Double.parseDouble(m1.group(2));
                            System.out.println(m1.group(0));
                            continue;
                        } 
                
                        if(m2.find()) {
                            foundTotal = true;
                            total_time = Double.parseDouble(m2.group(2));
                            System.out.println(m2.group(0));

                            if((stdInput.readLine()).equals("Search stopped without finding a solution.")) {
                                System.out.println("No solution found.");
                                successful = false;
                            }
                            break;
                        }
                    }
                    
                    expanded = expandedStates;
                    break;
                }
            }
            
            // just in case the programme can not find the total time?
            // use search time as total time
            // this is a strange bug that happens sometimes   
            if(!foundTotal && successful) {
                total_time = search_time;
            }
            
            System.out.println("successful: " + successful);
                       
            // read any errors from the attempted command
            while ((s = stdError_with_macro.readLine()) != null) {
                System.out.println(s);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
       
    public double getSearchTime() {
        return search_time;
    }
    
    public double getTotalTime() {
        return total_time;
    }
    
    public double getExpanded() {
        return expanded;
    }
    
    public boolean isSuccessful() {
        return successful;
    }
    
    public int getDomainIndex() {
        return domainIndex;
    }
    
    public int getProblemIndex() {
        return problemIndex;
    }
    
    public double getTimeOut() {
        return timeout;
    }
    
    public ArrayList<Double> getO() {
        ArrayList<Double> o = new ArrayList<>();
        o.add(search_time);
        o.add(total_time);
        o.add(expanded);
        return o;
    }
    
    public void setTimeOut(double new_value) {
        timeout = new_value;
    }
    
    public void setO(ArrayList<Double> new_O) {
        search_time = new_O.get(0);
        total_time = new_O.get(1);
        expanded = new_O.get(2);
    }
}