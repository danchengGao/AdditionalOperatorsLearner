package data;

import java.util.List;
import java.util.Locale;

public final class LearnerConfig {
    public enum Usage {
        NOT_IN_SEARCH,
        EVERYWHERE
    }

    public enum Configuration {
        RELAXED_MEDIAN,
        TRADITIONAL_MEDIAN,
        RANDOM_MEDIAN,
        UNARY_MEDIAN,
        MIXED_MEDIAN,
        MIXED_MEAN
    }

    // NOT_IN_SEARCH names new operators with macro-.
    // EVERYWHERE names them with everywhere- and only runs TRADITIONAL_MEDIAN.
    public static Usage USAGE = Usage.NOT_IN_SEARCH;
    // MIXED_MEDIAN includes traditional macros.
    public static Configuration CONFIGURATION = Configuration.MIXED_MEDIAN;
    // Adds test-domains/problems/harder once the incumbent solves every training problem in under 2.5 seconds.
    public static boolean HARDER_PROBLEMS = false;

    private LearnerConfig() {}

    public static void readOptions(String[] args, List<String> positional) {
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.equals("--harder-problems")) {
                HARDER_PROBLEMS = true;
            }
            else if(arg.equals("--operator-use") || arg.equals("--configuration")) {
                if(i + 1 >= args.length) {
                    throw new IllegalArgumentException("missing value for " + arg);
                }
                String value = args[++i];
                if(arg.equals("--operator-use")) {
                    USAGE = parse(Usage.class, value, arg);
                }
                else {
                    CONFIGURATION = parse(Configuration.class, value, arg);
                }
            }
            else if(arg.startsWith("--")) {
                throw new IllegalArgumentException("Unknown option: " + arg);
            }
            else {
                positional.add(arg);
            }
        }
    }

    public static boolean everywhereRequiresTraditional() {
        return USAGE == Usage.EVERYWHERE && CONFIGURATION != Configuration.TRADITIONAL_MEDIAN;
    }

    private static <T extends Enum<T>> T parse(Class<T> type, String text, String option) {
        try {
            return Enum.valueOf(type, text.trim().toUpperCase(Locale.ROOT).replace('-', '_'));
        }
        catch(IllegalArgumentException e) {
            StringBuilder names = new StringBuilder();
            for(T value : type.getEnumConstants()) {
                names.append(names.length() == 0 ? "" : ", ").append(value.name().toLowerCase(Locale.ROOT).replace('_', '-'));
            }
            throw new IllegalArgumentException("Unknown value for " + option + ": " + text + ". Use " + names + ".");
        }
    }
}
