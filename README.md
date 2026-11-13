# Additional Operators Learner

This repository learns macro and other operators to add to STRIPS domains. The operators are used outside search to improve planner performance.

The learner adds operators to a STRIPS domain, then searches for a small subset that makes Fast Downward solve training problems faster. The operators are intended to help heuristic computation, successor ordering, or tie-breaking, rather than to be applied as ordinary actions during search. The search is a ParamILS-style local search over sets of operators.

Four kinds of operator are generated:

- **Traditional macros:** a macro whose precondition and effect match a two-action sequence.
- **Relaxed macros:** the same merge with delete effects removed.
- **Random operators:** operators built from randomly chosen predicates.
- **Unary operators:** operators that change a single fact, including operators aimed at mutex groups.

The default run searches the mixed pool (relaxed macros, traditional macros, random operators, and unary operators) and compares candidate sets by the median training-set search time.

## Layout

```
src/                  learner
src/test-domains/     original domains and learnt domains
```

Original domains are the PDDL files directly under `src/test-domains/`. Learnt domains use a short prefix:

| Prefix | Domain        | Prefix | Domain          |
|--------|---------------|--------|-----------------|
| `ba`   | Barman        | `p`    | Parking         |
| `bl`   | Blocks        | `r`    | Rover           |
| `de`   | Depots        | `s`    | Satellite       |
| `dr`   | Driverlog     | `so`   | Sokoban         |
| `g`    | Gold Miner    | `sp`   | Spanner         |
| `go`   | Gripper       | `t`    | TPP             |
| `m`    | Matching BW   | `z`    | Zenotravel      |

Not every domain appears in every learnt-domain directory.

| Directory | Contents |
|-----------|----------|
| `src/test-domains/default_config` | Operators learnt with the default configuration: 50 rounds of 2 hours, `r = 20`, median objective, restart probability `0.01`. These operators are used outside search. This is the overall best configuration. |
| `src/test-domains/default_config_combined` | Start from the `default_config` domains, then learn traditional macros on top of them. The new macros may be used during search. |
| `src/test-domains/best_ever` | The best operator set found for each domain across the experiments. These operators are used outside search. |
| `src/test-domains/best_ever_combined` | Start from the `best_ever` domains, then learn traditional macros on top of them. The new macros may be used during search. |

## Requirements

- Java 8 or later.
- Fast Downward, available as `./planner/downward/fast-downward.py` from the working directory. It is not included here. `src/run-fast-downward.sh <domain> <problem> <timeout> [config]` runs one planner configuration. The learner calls it with no config name, which selects `ff`. The other names are `cea`, `lmcount`, and `mercury`. Mercury is `./planner/mercury/seq-agl-mercury/mercury.py`, also not included.
- A planner build that keeps additional operators out of search. The learner names those operators with the prefix `macro-`, and the planner must exclude operators with that prefix from search while still using them in heuristic computation. Traditional macros that may be used during search, in the `*_combined` domains, use the prefix `nonr-` instead. An unmodified Fast Downward treats every operator as searchable.
- Training problems as files in `test-domains/problems`, relative to the working directory. These problems are not included. Use 20 generated instances per domain, each taking the original domain about 5–15 seconds.

Run commands from `src`, so those relative paths resolve.

## Running

```
javac -encoding UTF-8 $(find . -name '*.java')
java Main test-domains/barman.pddl 20 10 5 10 7200000
```

`Main` takes six arguments:

| Argument | Meaning |
|----------|---------|
| domain path | Original domain file. |
| `r` | Number of random operator sets tried before iterative improvement. |
| difference | Minimum mean-runtime gap used when the mean objective compares two sets. The default run uses the median objective, which does not read this value. |
| `max_macro` | Maximum number of additional operators in one set. Use 5. |
| `nbh_size` | Number of neighbours sampled during iterative first improvement. Use 10. |
| `time_limit` | Cutoff in milliseconds. `7200000` is 2 hours. |

The restart probability is fixed at `0.01`, and each perturbation makes 3 neighbourhood steps. Output for a run is written under `test-domains/macroGen/0/`, including the incumbent domain `macroGen-<index>.pddl`.

## Ablation configurations

`Generator` always calls `ParamILS_mixed_median_t`. The other configurations used in the ablation study are separate entry points in the same class. Selecting one means changing that call.

| Entry point | Operator pool | Objective |
|-------------|---------------|-----------|
| `ParamILS_median_t` | Relaxed macros | Median |
| `ParamILS_traditional_median_t` | Traditional macros | Median |
| `ParamILS_random_median_t` | Random operators | Median |
| `ParamILS_unary_median_t` | Unary operators | Median |
| `ParamILS_mixed_median_t` | Mixed pool, including traditional macros | Median |
| `ParamILS_mixed_mean_t` | Mixed pool, including traditional macros | Mean |
