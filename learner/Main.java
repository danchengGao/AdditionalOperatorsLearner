import data.LearnerConfig;
import java.io.File;
import parser.ParserManager;

/**
 *
 * @author danchengGao
 */

public class Main {

    public static void main(String[] args) {
        try {
            LearnerConfig.readOptions(args);
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
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

        String domainPath = LearnerConfig.DOMAIN;
        System.out.println("domain: " + domainPath
                + ", r: " + LearnerConfig.R
                + ", difference: " + LearnerConfig.DIFFERENCE
                + ", max macro: " + LearnerConfig.MAX_MACRO
                + ", neighbourhood size: " + LearnerConfig.NBH_SIZE
                + ", time limit: " + LearnerConfig.TIME_LIMIT
                + ", operator use: " + LearnerConfig.USAGE
                + ", configuration: " + LearnerConfig.CONFIGURATION
                + ", planner: " + LearnerConfig.PLANNER
                + ", harder problems: " + LearnerConfig.HARDER_PROBLEMS);
        ParserManager p = new ParserManager(domainPath);
        p.getData().setOriginalDomainPath(domainPath);
        Generator g = new Generator(p.getData(), LearnerConfig.R, LearnerConfig.DIFFERENCE, LearnerConfig.MAX_MACRO, LearnerConfig.NBH_SIZE, LearnerConfig.TIME_LIMIT);
    }

}