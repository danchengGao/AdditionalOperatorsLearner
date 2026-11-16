#!/bin/bash
# Usage: ./run-fast-downward.sh <domain> <problem> <timeout> [config]
# The learner calls this with three arguments, which selects ff-lazy.
# config is one of: ff-lazy, ff-eager, cea, lmcount, mercury

Domain=$1
Problem=$2
Timeout=$3
Config=${4:-ff-lazy}

case "$Config" in
    ff-lazy)
        ./planner/downward/fast-downward.py --overall-time-limit "$Timeout" "$Domain" "$Problem" --search "lazy_greedy([ff()])"
        ;;
    ff-eager)
        ./planner/downward/fast-downward.py --overall-time-limit "$Timeout" "$Domain" "$Problem" --search "eager_greedy([ff()])"
        ;;
    cea)
        ./planner/downward/fast-downward.py --overall-time-limit "$Timeout" "$Domain" "$Problem" --search "lazy_greedy([cea()])"
        ;;
    lmcount)
        ./planner/downward/fast-downward.py --overall-time-limit "$Timeout" "$Domain" "$Problem" --search "lazy_greedy([lmcount(lm_rhw())])"
        ;;
    mercury)
        ./planner/mercury/seq-agl-mercury/mercury.py --overall-time-limit "$Timeout" "$Domain" "$Problem" --search "lazy_greedy(hrb,cost_type=1,reopen_closed=false)"
        ;;
    *)
        echo "Unknown config: $Config" >&2
        echo "Use ff-lazy, ff-eager, cea, lmcount, or mercury." >&2
        exit 1
        ;;
esac
