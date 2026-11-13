#!/bin/bash
# Usage: ./run-fast-downward.sh <domain> <problem> <timeout> [config]
# The learner calls this with three arguments, which selects ff.
# config is one of: ff, cea, lmcount, mercury

Domain=$1
Problem=$2
Timeout=$3
Config=${4:-ff}

case "$Config" in
    ff)
        ./planner/downward/fast-downward.py --overall-time-limit "$Timeout" "$Domain" "$Problem" --search "lazy_greedy([ff()])"
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
        echo "Use ff, cea, lmcount, or mercury." >&2
        exit 1
        ;;
esac
