import parser.ParserManager;

/**
 *
 * @author danchengGao
 */

public class Main {

    public static void main(String[] args) {
        String domainPath;
        if (args.length == 0) {
            System.out.println("No domain path provided");
        }
        else {
            domainPath = args[0];
            // start a new run
            if (args.length == 6) {
                ParserManager p = new ParserManager(domainPath);
                p.getData().setOriginalDomainPath(domainPath);
                int input_r = Integer.parseInt(args[1]);
                double input_difference = Double.parseDouble(args[2]);
                int input_max_macro = Integer.parseInt(args[3]);
                int input_nbh_size = Integer.parseInt(args[4]);
                int input_time_limit = Integer.parseInt(args[5]);                
                Generator g = new Generator(p.getData(), input_r, input_difference, input_max_macro, input_nbh_size, input_time_limit);
            }
            else {
                System.out.println("wrong number of args");
            }
        }
    }

}