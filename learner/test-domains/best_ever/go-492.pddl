(define 
(domain gold-miner-typed) 
(:requirements 
    :typing :equality 
) 
(:types 
    LOC 
) 
(:predicates 
    (arm-empty)
    (bomb-at ?x - LOC)
    (clear ?x - LOC)
    (connected ?x - LOC ?y - LOC)
    (gold-at ?x - LOC)
    (hard-rock-at ?x - LOC)
    (holds-bomb)
    (holds-gold)
    (holds-laser)
    (laser-at ?x - LOC)
    (robot-at ?x - LOC)
    (soft-rock-at ?x - LOC)
) 
(:action move
    :parameters (?x - LOC ?y - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (connected ?x ?y)
        (clear ?y)
    )
    :effect 
    (and
        (robot-at ?y)
        (not (robot-at ?x))
    )
) 

(:action pickup-laser
    :parameters (?x - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (laser-at ?x)
        (arm-empty)
    )
    :effect 
    (and
        (not (arm-empty))
        (holds-laser)
        (not (laser-at ?x))
    )
) 

(:action pickup-bomb
    :parameters (?x - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (bomb-at ?x)
        (arm-empty)
    )
    :effect 
    (and
        (not (arm-empty))
        (holds-bomb)
    )
) 

(:action putdown-laser
    :parameters (?x - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (holds-laser)
    )
    :effect 
    (and
        (arm-empty)
        (not (holds-laser))
        (laser-at ?x)
    )
) 

(:action detonate-bomb
    :parameters (?x - LOC ?y - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (holds-bomb)
        (connected ?x ?y)
        (soft-rock-at ?y)
    )
    :effect 
    (and
        (not (holds-bomb))
        (arm-empty)
        (clear ?y)
        (not (soft-rock-at ?y))
    )
) 

(:action fire-laser
    :parameters (?x - LOC ?y - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (holds-laser)
        (connected ?x ?y)
    )
    :effect 
    (and
        (clear ?y)
        (not (soft-rock-at ?y))
        (not (gold-at ?y))
        (not (hard-rock-at ?y))
    )
) 

(:action pick-gold
    :parameters (?x - LOC)
    :precondition 
    (and
        (robot-at ?x)
        (arm-empty)
        (gold-at ?x)
    )
    :effect 
    (and
        (not (arm-empty))
        (holds-gold)
    )
) 

(:action macro-2-3-2063392529
    :parameters (?x - LOC)
    :precondition 
    (and
        (clear ?x)
        (laser-at ?x)
    )
    :effect 
    (and
        (holds-bomb)
        (robot-at ?x)
        (not (hard-rock-at ?x))
    )
) 

) 
