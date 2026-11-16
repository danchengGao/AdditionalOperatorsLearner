import data.LearnerConfig;
import java.io.File;
import java.util.ArrayList;
import parser.ParserManager;

/**
 *
 * @author danchengGao
 */

public class Main {

    public static void main(String[] args) {
        ArrayList<String> positional = new ArrayList<>();
        try {
            LearnerConfig.readOptions(args, positional);
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if(positional.isEmpty()) {
            System.out.println("No domain path provided");
            return;
        }
        if(positional.size() != 6) {
            System.out.println("wrong number of args");
            return;
        }
        if(LearnerConfig.everywhereRequiresTraditional()) {
            System.out.println("Everywhere learning only runs TRADITIONAL_MEDIAN.");
            return;
        }

        if(LearnerConfig.HARDER_PROBLEMS && !new File("test-domains/problems/harder").isDirectory()) {
            System.out.println("--harder-problems needs test-domains/problems/harder.");
            return;
        }

        String domainPath = positional.get(0);
        System.out.println("operator use: " + LearnerConfig.USAGE + ", configuration: " + LearnerConfig.CONFIGURATION
                + ", harder problems: " + LearnerConfig.HARDER_PROBLEMS);
        ParserManager p = new ParserManager(domainPath);
        p.getData().setOriginalDomainPath(domainPath);
        int input_r = Integer.parseInt(positional.get(1));
        double input_difference = Double.parseDouble(positional.get(2));
        int input_max_macro = Integer.parseInt(positional.get(3));
        int input_nbh_size = Integer.parseInt(positional.get(4));
        int input_time_limit = Integer.parseInt(positional.get(5));
        Generator g = new Generator(p.getData(), input_r, input_difference, input_max_macro, input_nbh_size, input_time_limit);
    }

}