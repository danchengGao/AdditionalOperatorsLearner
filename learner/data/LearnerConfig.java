package data;

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
    // Passed to run-fast-downward.sh. ff-lazy is lazy_greedy([ff()]).
    public static String PLANNER = "ff-lazy";
    public static String DOMAIN;
    public static int R = 20;
    public static double DIFFERENCE = 10;
    public static int MAX_MACRO = 5;
    public static int NBH_SIZE = 10;
    public static int TIME_LIMIT = 7200000;

    private LearnerConfig() {}

    public static void readOptions(String[] args) {
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.equals("--harder-problems")) {
                HARDER_PROBLEMS = true;
            }
            else if(arg.equals("--domain")) {
                DOMAIN = value(args, i, arg);
                i++;
            }
            else if(arg.equals("--r")) {
                R = parseInt(value(args, i, arg), arg);
                i++;
            }
            else if(arg.equals("--difference")) {
                DIFFERENCE = parseDouble(value(args, i, arg), arg);
                i++;
            }
            else if(arg.equals("--max-macro")) {
                MAX_MACRO = parseInt(value(args, i, arg), arg);
                i++;
            }
            else if(arg.equals("--nbh-size")) {
                NBH_SIZE = parseInt(value(args, i, arg), arg);
                i++;
            }
            else if(arg.equals("--time-limit")) {
                TIME_LIMIT = parseInt(value(args, i, arg), arg);
                i++;
            }
            else if(arg.equals("--planner")) {
                PLANNER = parsePlanner(value(args, i, arg));
                i++;
            }
            else if(arg.equals("--operator-use")) {
                USAGE = parse(Usage.class, value(args, i, arg), arg);
                i++;
            }
            else if(arg.equals("--configuration")) {
                CONFIGURATION = parse(Configuration.class, value(args, i, arg), arg);
                i++;
            }
            else if(arg.startsWith("--")) {
                throw new IllegalArgumentException("Unknown option: " + arg);
            }
            else {
                throw new IllegalArgumentException("Unexpected argument: " + arg + ". Use --domain and the other named options.");
            }
        }
        if(DOMAIN == null) {
            throw new IllegalArgumentException("--domain is required");
        }
    }

    private static String value(String[] args, int i, String option) {
        if(i + 1 >= args.length || args[i + 1].startsWith("--")) {
            throw new IllegalArgumentException("missing value for " + option);
        }
        return args[i + 1];
    }

    private static int parseInt(String text, String option) {
        try {
            return Integer.parseInt(text);
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException(option + " needs an integer: " + text);
        }
    }

    private static double parseDouble(String text, String option) {
        try {
            return Double.parseDouble(text);
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException(option + " needs a number: " + text);
        }
    }

    public static boolean everywhereRequiresTraditional() {
        return USAGE == Usage.EVERYWHERE && CONFIGURATION != Configuration.TRADITIONAL_MEDIAN;
    }

    private static String parsePlanner(String text) {
        String normalized = text.trim().toLowerCase(Locale.ROOT);
        String[] allowed = {"ff-lazy", "ff-eager", "cea", "lmcount", "mercury"};
        for(String name : allowed) {
            if(name.equals(normalized)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Unknown value for --planner: " + text + ". Use ff-lazy, ff-eager, cea, lmcount, or mercury.");
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
