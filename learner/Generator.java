import data.LearnerConfig;
import data.StripsDataManager;
import evaluator.RunFastDownward;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author danchengGao
 */

public class Generator {
    
    private StripsDataManager dataMgr;
    private File problemFolder = new File ("test-domains/problems");
    private ArrayList<String> problemFiles = new ArrayList<>();
    
    private String fileHeader = "";
    private String fileContent = "";
    private String dirName = "";
    
    // map: domain index = set of macro indexes
    private TreeMap<Integer, HashSet<Integer>> all_θ = new TreeMap<>();
    // largest_θ_count occupied
    private int largest_θ_count = 0;
    // map: domain index = list of neighbour domain indexes
    private TreeMap<Integer, ArrayList<Integer>> neighbourhood_collection = new TreeMap<>();
    
    private int s = 3;

    private double k_max = 30.0;
    private int θ_0 = 0;
    private int θ_inc = θ_0;
    private ArrayList<Integer> θ_inc_list = new ArrayList<>();
    private int B = 0;
    private int maxPossibleObjective = 100000000;
    
    // map: domain index = list of R
    private TreeMap<Integer, ArrayList<RunFastDownward>> global_R = new TreeMap<>();
    // ĉ_a_θ_b: calculated by objective(θ_b, a, bound) 
    // map: (θ_b, a) = mean run time
    private TreeMap<int[], Double> ĉ = new TreeMap<>(Comparator.
                    <int[]>comparingInt(key -> key[0]).thenComparingInt(key -> key[1]));
    
    private long time = System.currentTimeMillis();

    private double best_inc_time = - 1.0;

    // sensitivity evaluation
    private int r = 20;
    private double difference_in_sum_time_for_comparing = 10.0;
    private int max_macro = 5;
    private int nbh_size = 10;
    private int time_limit = 3600000; // in mili sec

    private int last_updated_hour = 0;

    private File harderProblemFolder = new File ("test-domains/problems/harder");
    private int normalProblemSize = 0;
    private double p_restart = 0.01;
   

    private enum Initialiser {
        RELAXED, TRADITIONAL, RANDOM, UNARY, MIXED
    }

    private enum ActionPool {
        MACRO, RANDOM, MIXED
    }

    private enum RestartLimit {
        MACRO_COUNT, RANDOM_COUNT, LARGEST_THETA
    }

    private enum ObjectiveKind {
        MEDIAN, MEAN
    }

    // ParamILS configurations used in the ablation study
    private static final class Ablation {
        final Initialiser initialiser;
        final ObjectiveKind objective;
        final ActionPool pool;
        final RestartLimit restart;
        final boolean traceIterations;
        // true: the neighbourhood budget counts evaluated neighbours only
        // false: it counts every neighbour picked, including oversized ones
        final boolean countOnlyEvaluated;

        private Ablation(Initialiser initialiser, ObjectiveKind objective, ActionPool pool,
                         RestartLimit restart, boolean traceIterations, boolean countOnlyEvaluated) {
            this.initialiser = initialiser;
            this.objective = objective;
            this.pool = pool;
            this.restart = restart;
            this.traceIterations = traceIterations;
            this.countOnlyEvaluated = countOnlyEvaluated;
        }

        static Ablation of(LearnerConfig.Configuration configuration) {
            switch(configuration) {
                case RELAXED_MEDIAN:
                    return new Ablation(Initialiser.RELAXED, ObjectiveKind.MEDIAN, ActionPool.MACRO, RestartLimit.MACRO_COUNT, false, false);
                case TRADITIONAL_MEDIAN:
                    return new Ablation(Initialiser.TRADITIONAL, ObjectiveKind.MEDIAN, ActionPool.MACRO, RestartLimit.MACRO_COUNT, false, false);
                case RANDOM_MEDIAN:
                    return new Ablation(Initialiser.RANDOM, ObjectiveKind.MEDIAN, ActionPool.RANDOM, RestartLimit.RANDOM_COUNT, false, true);
                case UNARY_MEDIAN:
                    return new Ablation(Initialiser.UNARY, ObjectiveKind.MEDIAN, ActionPool.RANDOM, RestartLimit.RANDOM_COUNT, false, true);
                case MIXED_MEDIAN:
                    return new Ablation(Initialiser.MIXED, ObjectiveKind.MEDIAN, ActionPool.MIXED, RestartLimit.LARGEST_THETA, true, true);
                case MIXED_MEAN:
                    return new Ablation(Initialiser.MIXED, ObjectiveKind.MEAN, ActionPool.MIXED, RestartLimit.LARGEST_THETA, true, true);
                default:
                    throw new IllegalStateException("Unknown configuration: " + configuration);
            }
        }
    }

    public Generator(StripsDataManager d, int input_r, double input_difference, int input_max_macro, int input_nbh_size, int input_time_limit) {
        System.out.println("Generator_ParamILS starts...");
        dataMgr = d;

        r = input_r;
        difference_in_sum_time_for_comparing = input_difference;
        max_macro = input_max_macro;
        nbh_size = input_nbh_size;
        time_limit = input_time_limit;

        readProblemFileName();
        System.out.println(problemFiles);

        paramILS(0, Ablation.of(LearnerConfig.CONFIGURATION));
        dataMgr.setOriginalDomainPath(dirName + "macroGen-" + θ_inc + ".pddl");
        θ_0 = θ_inc;
        writeRoundSummary(0);

        System.out.println(θ_inc);
    }
    
    private void readProblemFileName() {
        for (File file : problemFolder.listFiles()) {
            if (!file.isDirectory()) {
                problemFiles.add(file.getName());
            }
        }
        Collections.shuffle(problemFiles);
        problemFiles.add(0, "useless_problem_name");
        normalProblemSize = problemFiles.size();
    }

    private void readHarderProblemFileName() {
        ArrayList<String> newProblemFiles = new ArrayList<>();
        for (File file : harderProblemFolder.listFiles()) {
            if (!file.isDirectory()) {
                newProblemFiles.add(file.getName());
            }
        }
        Collections.shuffle(newProblemFiles);
        problemFiles.addAll(newProblemFiles);
    }
    
    private void initialiseAllθ(int global_round, Initialiser initialiser) {
        clearRoundDirectory(global_round);
        if(global_round == 0) {
            all_θ.put(0, new HashSet<Integer>());
            largest_θ_count = createInitialPool(initialiser);
            for(int j = 0; j < largest_θ_count; j ++) {
                all_θ.put(j + 1, new HashSet<>(Collections.singletonList(j)));
            }
        }
        else {
            // TODO: implementation for multiple rounds
        }
        generateFileHeader();

        try {
            FileWriter fw = new FileWriter(dirName + "global_R.csv");
            PrintWriter out = new PrintWriter(fw);
            out.println("Domain Index θ" + "," + "Problem Index π" + "," + "Timeout κ" + "," + "Search Time o" + "," + "Total Time o");
            out.flush();
            out.close();
            fw.close();

            FileWriter fw_θ_inc = new FileWriter(dirName + "θ_inc.csv");
            PrintWriter out_θ_inc = new PrintWriter(fw_θ_inc);
            out_θ_inc.print("Domain Index θ_inc" + ",");
            out_θ_inc.print("Time" + ",");
            out_θ_inc.print("Time2" + ",");
            out_θ_inc.print("N" + ",");
            out_θ_inc.println("objective value");

            out_θ_inc.flush();
            out_θ_inc.close();
            fw_θ_inc.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        θ_inc_list.add(θ_inc);
        ArrayList<Double> results = runDomain(0, 1, k_max);
        int[] key = {θ_inc, 1};
        ĉ.put(key, results.get(1));
        writeθinc(θ_inc);
    }

    private ArrayList<Double> runDomain(int domainIndex, int problemIndex, double timeout) {
        String problemName;
        if(problemIndex < normalProblemSize)
            problemName  = problemFolder + "/" + problemFiles.get(problemIndex);
        else
            problemName  = harderProblemFolder + "/" + problemFiles.get(problemIndex);
        RunFastDownward r = new RunFastDownward(dirName, domainIndex, problemIndex, problemName, timeout, dataMgr.getOriginalDomainPath());
        
        if(global_R.containsKey(domainIndex)) {
            ArrayList<RunFastDownward> list = global_R.get(domainIndex);
            boolean added = false;
            int index_to_replace = -1;
            for(int i = 0; i < list.size(); i ++) {
                RunFastDownward old_r = list.get(i);
                if(old_r.getProblemIndex() == r.getProblemIndex()) {
                    index_to_replace = i;
                    added = true;
                    break;
                }
            }
            if(!added) {
                global_R.get(domainIndex).add(r);
            }
            else {
                global_R.get(domainIndex).set(index_to_replace, r);
            }
        }
        else {
            ArrayList<RunFastDownward> new_list = new ArrayList<>();
            new_list.add(r);
            global_R.put(domainIndex, new_list);
        }
        
        writeR(r);
        
        return r.getO();
    }
    
    private int paramILS(int global_round, Ablation config) {
        initialiseAllθ(global_round, config.initialiser);

        for(int i = 0 ; i < r ; i ++) {
            Random random_0 = new Random();
            ArrayList<Integer> keyList = new ArrayList<>(all_θ.keySet());
            int θ = keyList.get(random_0.nextInt(keyList.size()));
            writeAugmentedDomain(θ, config.pool);

            if(better(config.objective, θ, θ_0)) {
                θ_0 = θ;
            }

            if(timeLimitReached()) {
                return θ_inc;
            }
        }

        writeSummaryB4Itf(global_round, θ_0);

        int θ_ils = iterativeFirstImprovement(θ_0, global_round, config);

        int counter = 0;
        while (!timeLimitReached()) {
            if(config.traceIterations) {
                System.out.println("Not terminating yet, iterating...");
                counter ++;
            }
            int θ = θ_ils;

            // ===== Perturbation
            // for i = 1, . . . , s do θ ← random θ' ∈ Nbh(θ);
            for (int i = 1; i <= s; i ++) {
                ArrayList<Integer> neighbourhood = neighbourhood(θ, global_round, config.pool);
                Collections.shuffle(neighbourhood);
                θ = neighbourhood.get(0);
                writeAugmentedDomain(θ, config.pool);
            }

            // ===== Basic local search
            θ = iterativeFirstImprovement(θ, global_round, config);

            // ===== AcceptanceCriterion
            if (better(config.objective, θ, θ_ils)) {
                if(config.traceIterations) {
                    if(config.objective == ObjectiveKind.MEAN) {
                        System.out.println("AcceptanceCriterion better foc mean");
                    }
                    else {
                        System.out.println("AcceptanceCriterion better foc median");
                    }
                }
                θ_ils = θ;
            }

            // with probability p_restart do θ_ils ← random θ ∈ Θ;
            // random value 0.0 - 1.0
            double random_value = Math.random();
            if(random_value <= p_restart) {
                if(config.traceIterations) {
                    System.out.println("Restarted...");
                }
                Random random_2 = new Random();
                θ_ils = random_2.nextInt(restartBound(config.restart));
                writeAugmentedDomain(θ_ils, config.pool);
            }
        }

        if(config.traceIterations) {
            System.out.println("iteration counter: " + counter);
        }

        return θ_inc;
    }

    private boolean better(ObjectiveKind objective, int θ_1, int θ_2) {
        if(objective == ObjectiveKind.MEDIAN) {
            return better_foc_median(θ_1, θ_2);
        }
        return better_foc_mean(θ_1, θ_2);
    }

    private int restartBound(RestartLimit restart) {
        switch(restart) {
            case MACRO_COUNT:
                return dataMgr.getAllMacroActions().size();
            case RANDOM_COUNT:
                return dataMgr.getAllRandomActions().size();
            case LARGEST_THETA:
                return largest_θ_count;
            default:
                throw new IllegalStateException(restart.name());
        }
    }

    private boolean better_foc_median(int θ_1, int θ_2) {
        if(θ_1 == 0) {
            θ_1 = θ_0;
        }
        if(θ_2 == 0) {
            θ_2 = θ_0;
        }

        int θ_min;
        int θ_max;
        
        B = B + 1;
        if(N(θ_1) <= N(θ_2)) {
            θ_min = θ_1;
            θ_max = θ_2;
            if(N(θ_1) == N(θ_2)) {
                B = B + 1;
            }
        }
        else {
            θ_min = θ_2;
            θ_max = θ_1; 
        }

        int counter = 0;
        int N_θ_min = N(θ_min);
        int N_θ_max = N(θ_max);

        double ĉ_θ_max = getĉ(θ_max)[1];
        System.out.println("ĉ_θ_max: " + θ_max + " " + ĉ_θ_max);

        while(!(dominates_median(θ_1, θ_2, ĉ_θ_max) || dominates_median(θ_2, θ_1, ĉ_θ_max))) {
            int i = N(θ_min) + 1;
            if(i >= problemFiles.size()) {
                i = problemFiles.size() - 1;
            }
            double objective_θ_max = objective_median(θ_max, i, 999999999);  // If N (θ min ) = N (θ max ), adds a new run to R θ max .
            double objective_θ_min = objective_median(θ_min, i, ĉ_θ_max);   // Adds a new run to R θ min .

            if(N(θ_min) == N_θ_min && N(θ_max) == N_θ_max) {
                return false;
            }

            N_θ_min = N(θ_min);
            N_θ_max = N(θ_max);
        }

        if(dominates_median(θ_1, θ_2, ĉ_θ_max)) {
            // ===== Perform B bonus runs
            // if N(θ_1) + B > problem number, then use problem number
            int N_number = N(θ_1) + B;
            
            if(N_number >= problemFiles.size()) {
                N_number = problemFiles.size() - 1;
            }
            objective_median(θ_1 , N_number, 999999999); // Adds B new runs to R θ 1 .
            
            B = 0;
            return true;
        }
        else {
            return false;
        }        
    }

    private boolean better_foc_mean(int θ_1, int θ_2) {
        System.out.println("better_foc_mean  θ_1: " + θ_1 + " θ_2: " + θ_2);

        if(θ_1 == 0) {
            θ_1 = θ_0;
        }
        if(θ_2 == 0) {
            θ_2 = θ_0;
        }

        int θ_min;
        int θ_max;

        B = B + 1;
        if(N(θ_1) <= N(θ_2)) {
            θ_min = θ_1;
            θ_max = θ_2;
            if(N(θ_1) == N(θ_2)) {
                B = B + 1;
            }
        }
        else {
            θ_min = θ_2;
            θ_max = θ_1;
        }

        int counter = 0;
        int N_θ_min = N(θ_min);
        int N_θ_max = N(θ_max);
        while(!(dominates_mean(θ_1, θ_2) || dominates_mean(θ_2, θ_1))) {
            int i = N(θ_min) + 1;
            if(i >= problemFiles.size()) {
                i = problemFiles.size() - 1;
            }
            double objective_θ_max = objective_mean(θ_max, i, 999999999);  // If N (θ min ) = N (θ max ), adds a new run to R θ max .
            double objective_θ_min = objective_mean(θ_min, i, 999999999);   // Adds a new run to R θ min .

            if(N(θ_min) == N_θ_min && N(θ_max) == N_θ_max) {
                return false;
            }

            N_θ_min = N(θ_min);
            N_θ_max = N(θ_max);
        }

        // θ_prime_prime dominates θ_prime?
        if(dominates_mean(θ_1, θ_2)) {
            // ===== Perform B bonus runs
            // if N(θ_1) + B > problem number, then use problem number
            int N_number = N(θ_1) + B;
            if(N_number >= problemFiles.size()) {
                N_number = problemFiles.size() - 1;
            }
            objective_mean(θ_1 , N_number, 999999999); // Adds B new runs to R θ 1 .
            B = 0;
            return true;
        }
        else {
            return false;
        }
    }
    
    private int N(int domainIndex) {
        try {
            return global_R.get(domainIndex).size();
        }
        catch (Exception e) {
            return 0;
        }       
    }
    
    private boolean dominates_mean(int θ_1, int θ_2) {
        int N_θ_1 = N(θ_1);
        int N_θ_2 = N(θ_2);
        if(N_θ_1 < N_θ_2) {
            return false;
        }
        if(N_θ_1 == 0 || N_θ_2 == 0) {
            return false;
        }
        double ĉ_N_θ_2_θ_1 = getĉ(N_θ_2, θ_1);
        if(ĉ_N_θ_2_θ_1 == -1.0) {
            ĉ_N_θ_2_θ_1 = objective_mean(θ_1, N_θ_2, 999999999);
        }
        double ĉ_N_θ_2_θ_2 = getĉ(N_θ_2, θ_2);
        if(ĉ_N_θ_2_θ_2 == -1.0) {
            ĉ_N_θ_2_θ_2 = objective_mean(θ_2, N_θ_2, 999999999);
        }
        
        // ADD SUM DIFFERENCE REQUIREMENT
        double value_1 = ĉ_N_θ_2_θ_1 * (N_θ_2);
        double value_2 = ĉ_N_θ_2_θ_2 * (N_θ_2);
        double difference = Math.abs(value_1 - value_2);
        return (ĉ_N_θ_2_θ_1 <= ĉ_N_θ_2_θ_2) && (difference > difference_in_sum_time_for_comparing / ((problemFiles.size() - 1) / N_θ_2));
    }

    private boolean dominates_median(int θ_1, int θ_2, double bound) {
        int N_θ_1 = N(θ_1);
        int N_θ_2 = N(θ_2);
        if(N_θ_1 < N_θ_2) {
            return false;
        }
        if(N_θ_1 == 0 || N_θ_2 == 0) {
            return false;
        }

        double ĉ_N_θ_2_θ_1 = -1.0;
        if(ĉ_N_θ_2_θ_1 == -1.0) {
            ĉ_N_θ_2_θ_1 = objective_median(θ_1, N_θ_2, bound);
        }

        double ĉ_N_θ_2_θ_2 = -1.0;
        if(ĉ_N_θ_2_θ_2 == -1.0) {
            ĉ_N_θ_2_θ_2 = objective_median(θ_2, N_θ_2, bound);
        }

        // ADD SUM DIFFERENCE REQUIREMENT
        double value_1 = ĉ_N_θ_2_θ_1;
        double value_2 = ĉ_N_θ_2_θ_2;
        double difference = Math.abs(value_1 - value_2);
        return (ĉ_N_θ_2_θ_1 <= ĉ_N_θ_2_θ_2) && (difference > 1.0);
    }
    
    private double getĉ(int a, int θ_b) {
        int arr[] = {θ_b, a};
        try {
            return ĉ.get(arr);
        }
        catch(Exception e) {
            return -1.0;
        }
    }
    
    private double[] getĉ(int θ_b) {
        int largest_a = 0;
        double current_ĉ = -1.0;
        for(int a = 1; a <= problemFiles.size(); a ++) {
            int arr[] = {θ_b, a};
            try {
                current_ĉ = ĉ.get(arr);
            }
            catch(Exception e) {
                continue;
            }
            largest_a = a;
        }        
        double toReturn[] = {largest_a, current_ĉ};
        return toReturn;
    }
    
    private double objective_median(int θ, int n, double optional_bound) {
        // ===== Maintain invariant: N (θ inc ) ≥ N (θ) for any θ
        double ĉ_n_θ_inc = 0;
        if(θ != θ_inc && N(θ_inc) < n) {
            ĉ_n_θ_inc = objective_median(θ_inc , n, 999999999);  // Adds N − N (θ_inc ) runs to R θ_inc
        }
        else {
            ĉ_n_θ_inc = getĉ(n, θ_inc);
        }
        
        // ===== For aggressive capping, update bound
        // 3. if Aggressive capping then bound ← min(bound, bm · ĉ N (θ inc ))
        if(ĉ_n_θ_inc != -1.0 && optional_bound > 2 * ĉ_n_θ_inc) {
            optional_bound = 2 * ĉ_n_θ_inc;
        }
        
        // ===== Update the run results in tuple R_θ
        for(int i = 1 ; i <= n ; i ++) {
            double sum_runtime = 0.0;
            // 5 : sum runtime ← sum of runtimes in R_θ [1], . . . , R_θ [i − 1]
            int tree_itr = 0;
            for(int problemIndex = 1; problemIndex <= i-1; problemIndex ++) {
                int tree_Index = findTreeIndex(tree_itr, θ, problemIndex);
                if(tree_Index != -1) {
                    sum_runtime = sum_runtime + global_R.get(θ).get(tree_Index).getSearchTime();
                    tree_itr = tree_Index + 1;
                }                
            }
            // 6 : k’ i ← min(k max , N · bound − sum runtime)
            double k_prime_i = 0.0;
            if(k_max > n * optional_bound - sum_runtime) {
                k_prime_i = n * optional_bound - sum_runtime;
            }
            else {
                k_prime_i = k_max;
            }
            if (k_prime_i <= 0) {
                return maxPossibleObjective + (n + 1) - i;
            }
            // 7 : if N (θ) ≥ i then (θ, π i , κ i , o i ) ← R θ [i]
            RunFastDownward r_θ_i = new RunFastDownward("", -1, 0, "", 0.0, dataMgr.getOriginalDomainPath());
            if(N(θ) >= i) {               
                int tree_Index = findTreeIndex(tree_itr, θ, i);
                r_θ_i =  global_R.get(θ).get(tree_Index); // result of the previous run               
            }
            // 8 : if N (θ) ≥ i and ((k i ≥ k ‘ i and o i = “unsuccessful”) or (k i < k’ i and o i = “successful”))
            // then o' i ← o i // Previous run is longer yet unsuccessful or shorter yet successful ⇒ can re-use result
            ArrayList<Double> o_prime_i = new ArrayList<Double>();
            double k_i = r_θ_i.getTimeOut();
            if(k_i == 0.0) {
                k_i = k_max;
            }

            double k_i_for_comparison = k_i;
            double k_prime_i_for_comparison = k_prime_i;
            if (k_i_for_comparison > 60) k_i_for_comparison = 60;
            if (k_prime_i_for_comparison > 60) k_prime_i_for_comparison = 60;

            if(N(θ) >= i && (((k_i_for_comparison >= k_prime_i_for_comparison && r_θ_i.isSuccessful() == false)) || (k_i_for_comparison <= k_prime_i_for_comparison && r_θ_i.isSuccessful() == true))) {
                o_prime_i = r_θ_i.getO();
            }
            // 9 : else
            // 10 : o' i ← objective from a newly executed run of A(θ) on instance π i with seed s i and captime κ' i
            else {
                o_prime_i = runDomain(θ, i, k_prime_i);
                // 11 : R θ [i] ← (θ, π i , κ' i , o' i )
                r_θ_i.setTimeOut(k_prime_i);
                r_θ_i.setO(o_prime_i);
            }

            // 12 :  if 1/N · (sum runtime + o' i ) > bound then return maxPossibleObjective + (N + 1) − i
            if(((sum_runtime + o_prime_i.get(1)) / n) > optional_bound) {
                return maxPossibleObjective + (n + 1) - i;
            }
        }
        
        // 13 : if N = N (θ inc ) and (sum of runtimes in R θ ) < (sum of runtimes in R θ inc ) then θ inc ← θ
        double median_of_θ = medianOfRuntimes(θ);
        double median_of_θ_inc = medianOfRuntimes(θ_inc);
        boolean θ_inc_updated = false;
        if(n == N(θ_inc) && (median_of_θ < median_of_θ_inc)) {
            boolean compare_with_θ_inc = (Math.abs(median_of_θ - median_of_θ_inc)) < 0.5;
            boolean comare_with_best_inc_time = (Math.abs(median_of_θ - best_inc_time)) < 0.5;
            if((compare_with_θ_inc || comare_with_best_inc_time)) {
                // do not update incumbent
            }
            else {
                θ_inc = θ;
                if(θ_inc == 0) {
                    θ_inc = θ_0;
                }
                θ_inc_list.add(θ_inc);
                θ_inc_updated = true;
                if(n >= problemFiles.size() - 1) {
                    // update best inc time
                    best_inc_time = median_of_θ;
                }
            }
        }
        
        // 14 : return 1/N · (sum of runtimes in R θ )
        double value = median_of_θ;
        if(n == 0) {
            value = 999999999;
        }
        int[] arr_θ = {θ, n}; 
        ĉ.put(arr_θ, value);
        if(θ_inc_updated) {
           writeθinc(θ_inc);
           if(LearnerConfig.HARDER_PROBLEMS && best_inc_time < 2.5 && best_inc_time > 0 && n == (normalProblemSize - 1) && problemFiles.size() == normalProblemSize) {
               readHarderProblemFileName();
           }
        }
        return value;
    }

    private double objective_mean(int θ, int n, double optional_bound) {
        // ===== Maintain invariant: N (θ inc ) ≥ N (θ) for any θ
        double ĉ_n_θ_inc = 0;
        if(θ != θ_inc && N(θ_inc) < n) {
            ĉ_n_θ_inc = objective_mean(θ_inc , n, 999999999);  // Adds N − N (θ_inc ) runs to R θ_inc
        }
        else {
            ĉ_n_θ_inc = getĉ(n, θ_inc);
        }

        // ===== For aggressive capping, update bound
        // 3. if Aggressive capping then bound ← min(bound, bm · ĉ N (θ inc ))
        if(ĉ_n_θ_inc != -1.0 && optional_bound > 2 * ĉ_n_θ_inc) {
            optional_bound = 2 * ĉ_n_θ_inc;
        }

        // ===== Update the run results in tuple R_θ
        for(int i = 1 ; i <= n ; i ++) {
            double sum_runtime = 0.0;
            // 5 : sum runtime ← sum of runtimes in R_θ [1], . . . , R_θ [i − 1]
            int tree_itr = 0;
            for(int problemIndex = 1; problemIndex <= i-1; problemIndex ++) {
                int tree_Index = findTreeIndex(tree_itr, θ, problemIndex);
                if(tree_Index != -1) {
                    sum_runtime = sum_runtime + global_R.get(θ).get(tree_Index).getSearchTime();
                    tree_itr = tree_Index + 1;
                }
            }
            // 6 : k’ i ← min(k max , N · bound − sum runtime)
            double k_prime_i = 0.0;
            if(k_max > n * optional_bound - sum_runtime) {
                k_prime_i = n * optional_bound - sum_runtime;
            }
            else {
                k_prime_i = k_max;
            }
            if (k_prime_i <= 0) {
                return maxPossibleObjective + (n + 1) - i;
            }
            // 7 : if N (θ) ≥ i then (θ, π i , κ i , o i ) ← R θ [i]
            RunFastDownward r_θ_i = new RunFastDownward("", -1, 0, "", 0.0, dataMgr.getOriginalDomainPath());
            if(N(θ) >= i) {
                int tree_Index = findTreeIndex(tree_itr, θ, i);
                r_θ_i =  global_R.get(θ).get(tree_Index); // result of the previous run
            }
            // 8 : if N (θ) ≥ i and ((k i ≥ k ‘ i and o i = “unsuccessful”) or (k i < k’ i and o i = “successful”))
            // then o' i ← o i // Previous run is longer yet unsuccessful or shorter yet successful ⇒ can re-use result
            ArrayList<Double> o_prime_i = new ArrayList<Double>();
            double k_i = r_θ_i.getTimeOut();
            if(k_i == 0.0) {
                k_i = k_max;
            }

            double k_i_for_comparison = k_i;
            double k_prime_i_for_comparison = k_prime_i;
            if (k_i_for_comparison > 60) k_i_for_comparison = 60;
            if (k_prime_i_for_comparison > 60) k_prime_i_for_comparison = 60;

            if(N(θ) >= i && (((k_i_for_comparison >= k_prime_i_for_comparison && r_θ_i.isSuccessful() == false)) || (k_i_for_comparison <= k_prime_i_for_comparison && r_θ_i.isSuccessful() == true))) {
                o_prime_i = r_θ_i.getO();
            }
            // 9 : else
            // 10 : o' i ← objective from a newly executed run of A(θ) on instance π i with seed s i and captime κ' i
            else {
                o_prime_i = runDomain(θ, i, k_prime_i);
                // 11 : R θ [i] ← (θ, π i , κ' i , o' i )
                r_θ_i.setTimeOut(k_prime_i);
                r_θ_i.setO(o_prime_i);
            }

            // 12 :  if 1/N · (sum runtime + o' i ) > bound then return maxPossibleObjective + (N + 1) − i
            if(((sum_runtime + o_prime_i.get(1)) / n) > optional_bound) {
                return maxPossibleObjective + (n + 1) - i;
            }
        }

        // 13 : if N = N (θ inc) and (sum of runtimes in R θ) < (sum of runtimes in R θ inc) then θ inc ← θ
        double sum_of_runtimes_θ = sumOfRuntimes(θ);
        double sum_of_runtimes_θ_inc = sumOfRuntimes(θ_inc);

        boolean θ_inc_updated = false;
        if(n == N(θ_inc) && (sum_of_runtimes_θ < sum_of_runtimes_θ_inc)) {
            boolean compare_with_θ_inc = (Math.abs(sum_of_runtimes_θ - sum_of_runtimes_θ_inc)) < (difference_in_sum_time_for_comparing / ((problemFiles.size() - 1) / n));
            boolean comare_with_best_inc_time = (Math.abs(sum_of_runtimes_θ - best_inc_time)) < (difference_in_sum_time_for_comparing / ((problemFiles.size() - 1) / n));

            if((compare_with_θ_inc || comare_with_best_inc_time)) {
                // do not update incumbent
            }
            else {
                θ_inc = θ;
                if(θ_inc == 0) {
                    θ_inc = θ_0;
                }
                θ_inc_list.add(θ_inc);
                θ_inc_updated = true;
                if(n >= problemFiles.size() - 1) {
                    // update best inc time
                    best_inc_time = sum_of_runtimes_θ;
                }
            }
        }

        // 14 : return 1/N · (sum of runtimes in R θ )
        double value = sum_of_runtimes_θ / n;
        if(n == 0) {
            value = 999999999;
        }
        int[] arr_θ = {θ, n};
        ĉ.put(arr_θ, value);
        if(θ_inc_updated) {
            writeθinc(θ_inc);
            if(LearnerConfig.HARDER_PROBLEMS && value < 2.5 && value > 0 && n == (normalProblemSize - 1) && problemFiles.size() == normalProblemSize) {
                readHarderProblemFileName();
            }
        }
        return value;
    }
    
    private int findTreeIndex(int startIndex, int domainIndex, int problemIndex) {
        for(int i = startIndex; i < global_R.get(domainIndex).size(); i ++) {
            if(global_R.get(domainIndex).get(i).getProblemIndex() == problemIndex) {                
                return i;
            }
        }
        return -1;
    }
    
    private double sumOfRuntimes(int domainIndex) {
        double sum = 0.0;
        try {
            for(int i = 0 ; i < global_R.get(domainIndex).size(); i ++) {
                sum = sum + global_R.get(domainIndex).get(i).getSearchTime();
            }
            return sum; 
        }
        catch (Exception e) {
            return 0.0;
        }
    }

    private double medianOfRuntimes(int domainIndex) {
        double median = 0.0;
        try {
            ArrayList<Double> running_times = new ArrayList<Double>();
            for(int i = 0 ; i < global_R.get(domainIndex).size(); i ++) {
                running_times.add(global_R.get(domainIndex).get(i).getSearchTime());
            }
            Collections.sort(running_times);
            if (running_times.size() % 2 == 0) {
                double sumOfMiddleElements = running_times.get((running_times.size() / 2)) +
                        running_times.get(((running_times.size() / 2) - 1));
                // calculate average of middle elements
                median =  sumOfMiddleElements / 2;
            }
            else {
                // get the middle element
                median = running_times.get((running_times.size() / 2));
            }
            return median;
        }
        catch (Exception e) {
            return 0.0;
        }
    }

    // force selection of domains with fewer macro-actions
    private int iterativeFirstImprovement(int θ, int global_round, Ablation config) {
        int θ_prime = -999999999;
        out: while(θ_prime != θ) {
            θ_prime = θ;
            // termination criterion
            if(timeLimitReached() || all_θ.get(θ_prime).size() > ((max_macro * (global_round + 1) + 1))) {
                break;
            }

            ArrayList<Integer> neighbourhood = neighbourhood(θ_prime, global_round, config.pool);

            // separate neighbourhood into two
            ArrayList<Integer> neighbourhood_fewer = new ArrayList<>();
            ArrayList<Integer> neighbourhood_more = new ArrayList<>();
            Collections.shuffle(neighbourhood);
            for(Integer index : neighbourhood) {
                if(all_θ.get(index).size() > all_θ.get(θ_prime).size()) {
                    neighbourhood_more.add(index);
                }
                else {
                    neighbourhood_fewer.add(index);
                }
            }

            int more_counter = 0;
            int fewer_counter = 0;
            int effective_counter = 0;
            for(int i = 0; i < neighbourhood.size(); i ++) {
                if(timeLimitReached()){
                    break out;
                }
                Integer θ_prime_prime = -1;
                // even number select from neighbourhood_more
                if((i % 2 == 0) && (more_counter < neighbourhood_more.size())) {
                    θ_prime_prime = neighbourhood_more.get(more_counter);
                    more_counter ++;
                }
                // odd number select from neighbourhood_fewer
                else if ((i % 2 != 0) && (fewer_counter < neighbourhood_fewer.size())){
                    θ_prime_prime = neighbourhood_fewer.get(fewer_counter);
                    fewer_counter ++;
                }
                else {
                    if(more_counter < neighbourhood_more.size()) {
                        θ_prime_prime = neighbourhood_more.get(more_counter);
                        more_counter ++;
                    }
                    else {
                        θ_prime_prime = neighbourhood_fewer.get(fewer_counter);
                        fewer_counter ++;
                    }
                }

                if(all_θ.get(θ_prime_prime).size() > (max_macro * (global_round + 1))) {
                    continue;
                }

                writeAugmentedDomain(θ_prime_prime, config.pool);
                // returns true if θ_prime_prime dominates θ_prime
                if (better(config.objective, θ_prime_prime, θ_prime)) {
                    θ = θ_prime_prime;
                    break;
                }

                // selective nbh
                if(config.countOnlyEvaluated) {
                    effective_counter ++;
                    if(effective_counter >= nbh_size) {
                        break;
                    }
                }
                else if(more_counter + fewer_counter >= nbh_size) {
                    break;
                }
            }

        }
        return θ;
    }

    // just remove some macro i, rather than replacing it with another
    private ArrayList<Integer> neighbourhood(int θ, int global_round, ActionPool pool) {
        if(neighbourhood_collection.containsKey(θ)) {
            return neighbourhood_collection.get(θ);
        }

        ArrayList<Integer> toReturn = new ArrayList<>();
        int θ_counter = largest_θ_count;
        HashSet<Integer> all_macro_indexes = all_θ.get(θ);

        // rm one macro
        if(all_macro_indexes.size() > 0) {
            for(Integer i : all_macro_indexes) {
                HashSet<Integer> new_set = new HashSet<>();
                new_set.addAll(all_macro_indexes);
                new_set.remove(i);
                int returned_index = all_θ_contains(new_set);
                if(returned_index >= 0) {
                    toReturn.add(returned_index);
                }
                else {
                    θ_counter ++;
                    all_θ.put(θ_counter, new_set);
                    toReturn.add(θ_counter);
                }
            }
        }
        if(all_θ.get(θ).size() < (max_macro * (global_round + 1))) {
            // this macro + one other from macros
            for (int k = 0; k < candidateCount(pool); k++) {
                if (all_macro_indexes.contains(k)) {
                    continue;
                }
                HashSet<Integer> new_set = new HashSet<>();
                new_set.addAll(all_macro_indexes);
                new_set.add(k);
                int returned_index = all_θ_contains(new_set);
                if (returned_index >= 0) {
                    toReturn.add(returned_index);
                } else {
                    θ_counter++;
                    all_θ.put(θ_counter, new_set);
                    toReturn.add(θ_counter);
                }
            }
        }

        largest_θ_count = θ_counter;
        neighbourhood_collection.put(θ, toReturn);
        return toReturn;
    }

    private int candidateCount(ActionPool pool) {
        switch(pool) {
            case MACRO:
                return dataMgr.getAllMacroActions().size();
            case RANDOM:
                return dataMgr.getAllRandomActions().size();
            case MIXED:
                return dataMgr.getAllMacroActions().size() + dataMgr.getAllRandomActions().size();
            default:
                throw new IllegalStateException(pool.name());
        }
    }

    private int all_θ_contains(HashSet<Integer> input) {
        for(Integer i : all_θ.keySet()) {
            if(all_θ.get(i).equals(input)) {
                return i;
            }
        }
        return -1;
    }

    private boolean timeLimitReached() {
        // termination for time:
        long current_time = System.currentTimeMillis() - time;
        if((int) (current_time / (1000*60*60))  > last_updated_hour) {
            writeθinc(θ_inc);
        }
        return ((System.currentTimeMillis() - time) > time_limit) ;//|| b2;
    }

    private void clearRoundDirectory(int global_round) {
        System.out.println("Clearing directory...");
        
        dirName = "test-domains/macroGen/" + global_round + "/";// + 0 + "/";
        
        // clear directory
        File directory = new File(dirName);
        if (!directory.exists()){
            directory.mkdirs();
        }
        else {
            for(File file: directory.listFiles()) {
                file.delete();
            }
        }
    }

    // builds the operator pool and returns its size
    private int createInitialPool(Initialiser initialiser) {
        int size;
        switch(initialiser) {
            case RELAXED:
            case TRADITIONAL:
                dataMgr.mergeAllActionPairs(initialiser == Initialiser.RELAXED);
                dataMgr.removeDuplicateAction(0);
                size = dataMgr.getAllMacroActions().size();
                break;
            case RANDOM:
                dataMgr.createSomeRandomActions(200);
                size = dataMgr.getAllRandomActions().size();
                break;
            case UNARY:
                dataMgr.createMutexGroupDestroyingActions();
                size = dataMgr.getAllRandomActions().size();
                break;
            case MIXED:
                dataMgr.createMixedActions();
                return dataMgr.getAllMacroActions().size() + dataMgr.getAllRandomActions().size();
            default:
                throw new IllegalStateException(initialiser.name());
        }
        System.out.println("size: " + size);
        return size;
    }


    // header includes: domain name, requirements, types, constants, predicates, original actions, selected macro action
    // parameter states whether to include the selected macros from previous rounds
    private void generateFileHeader() {
        fileHeader = "";
        fileHeader = fileHeader + "(define \n";
        fileHeader = fileHeader + "(domain " + dataMgr.getDomainName() + ") \n";
	    fileHeader = fileHeader + dataMgr.requirementsToString();
	    fileHeader = fileHeader + dataMgr.allTypesToString();
	    fileHeader = fileHeader + dataMgr.allConstantsToString();
	    fileHeader = fileHeader + dataMgr.allPredicatesToString();
	    fileHeader = fileHeader + dataMgr.allActionsToString();
        fileHeader = fileHeader + dataMgr.allOriginalMacrosToString();
        fileHeader = fileHeader + dataMgr.allOriginalInSearchToString();
    }
    
    // add the indicated macro action to the file
    private void generateContent(int domainIndex, ActionPool pool) {
        fileContent = fileHeader;
        HashSet<Integer> all_macros = all_θ.get(domainIndex);
        for(Integer i : all_macros) {
            switch(pool) {
                case MACRO:
                    fileContent = fileContent + dataMgr.getMacroActions().get(i);
                    break;
                case RANDOM:
                    fileContent = fileContent + dataMgr.getAllRandomActions().get(i);
                    break;
                case MIXED:
                    if(i < dataMgr.getMacroActions().size())
                        fileContent = fileContent + dataMgr.getMacroActions().get(i);
                    else
                        fileContent = fileContent + dataMgr.getAllRandomActions().get(i - dataMgr.getMacroActions().size());
                    break;
                default:
                    throw new IllegalStateException(pool.name());
            }
        }
        fileContent = fileContent + ") \n";
    }

    private void writeAugmentedDomain(int domainIndex, ActionPool pool) {
        generateContent(domainIndex, pool);

        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(dirName + "macroGen-" + domainIndex + ".pddl");
            bw = new BufferedWriter(fw);
            bw.write(fileContent);
            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void writeR(RunFastDownward r) {
	    try {
            FileWriter fw = new FileWriter(dirName + "global_R.csv", true);
	        PrintWriter out = new PrintWriter(fw);
            
            out.print(r.getDomainIndex());
            out.print(",");
            
            out.print(r.getProblemIndex());
            out.print(",");
            
            out.print(r.getTimeOut());
            out.print(",");

            out.print(r.getSearchTime());
            out.print(",");
            
            out.print(r.getTotalTime());
            out.println(",");
            
            out.flush();
            out.close();
            fw.close();
        }
	    catch(IOException e) {
	        e.printStackTrace();
	    }
    }


    private void writeθinc(int θ_inc) {
        try {                     
            FileWriter fw = new FileWriter(dirName + "θ_inc.csv", true);
	        PrintWriter out = new PrintWriter(fw);
            
            out.print(θ_inc + ",");

            long current_time = System.currentTimeMillis() - time;
            out.print(current_time + ",");
            int seconds = (int) (current_time / 1000) % 60 ;
            int minutes = (int) ((current_time / (1000*60)) % 60);
            int hours   = (int) (current_time / (1000*60*60));
            last_updated_hour = hours;
            String printTime = hours + " h " + minutes + " m " + seconds + " s";
            out.print(printTime + ",");

            double[] objective_value = getĉ(θ_inc);
            out.print(objective_value[0] + ",");
            out.println(objective_value[1]);
            
            out.flush();
            out.close();
            fw.close();
        }
	    catch(IOException e) {
	        e.printStackTrace();
	    }
    }

    private void writeRoundSummary(int global_round) {
        try {
            FileWriter fw = new FileWriter("test-domains/macroGen/" + global_round + "/" + "round_time");
            BufferedWriter bw = new BufferedWriter(fw);
            long current_time = System.currentTimeMillis();
            String toWrite = "time in milliseconds = " + (current_time - time);
            bw.write(toWrite);

            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSummaryB4Itf(int global_round, int θ_0_index) {
        try {
            FileWriter fw = new FileWriter("test-domains/macroGen/" + global_round + "/" + "sum_b4_itf");
            BufferedWriter bw = new BufferedWriter(fw);
            long current_time = System.currentTimeMillis();
            String toWrite = "time in milliseconds = " + (current_time - time) + " ; θ_0_index = " + θ_0_index;
            bw.write(toWrite);

            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}
