(define 
(domain gold-miner-typed) 
(:requirements 
    :equality 
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

(:action macro-detonate-bomb-and-move-45
    :parameters (?x_0 - LOC ?y_0 - LOC)
    :precondition 
    (and
        (robot-at ?x_0)
        (holds-bomb)
        (connected ?x_0 ?y_0)
        (soft-rock-at ?y_0)
        (not (= ?x_0 ?y_0))
    )
    :effect 
    (and
        (not (holds-bomb))
        (arm-empty)
        (clear ?y_0)
        (not (soft-rock-at ?y_0))
        (robot-at ?y_0)
        (not (robot-at ?x_0))
    )
) 

(:action macro-relaxed-move-and-putdown-laser-6
    :parameters (?x_0 - LOC ?x - LOC)
    :precondition 
    (and
        (robot-at ?x_0)
        (connected ?x_0 ?x_0)
        (clear ?x_0)
        (not (= ?x_0 ?x))
        (robot-at ?x)
        (holds-laser)
    )
    :effect 
    (and
        (robot-at ?x_0)
        (arm-empty)
        (laser-at ?x)
    )
) 

(:action macro-relaxed-detonate-bomb-and-pick-gold-3
    :parameters (?x_0 - LOC ?y_0 - LOC)
    :precondition 
    (and
        (robot-at ?x_0)
        (holds-bomb)
        (connected ?x_0 ?y_0)
        (soft-rock-at ?y_0)
        (not (= ?x_0 ?y_0))
        (robot-at ?y_0)
        (gold-at ?y_0)
    )
    :effect 
    (and
        (arm-empty)
        (clear ?y_0)
        (holds-gold)
    )
) 

(:action macro-2-2-48586925
    :parameters (?x - LOC)
    :precondition 
    (and
        (not (clear ?x))
        (not (bomb-at ?x))
    )
    :effect 
    (and
        (arm-empty)
        (not (soft-rock-at ?x))
    )
) 

(:action nonr-move-and-move-55
    :parameters (?x_0 - LOC ?y_0 - LOC ?y - LOC)
    :precondition 
    (and
        (robot-at ?x_0)
        (connected ?x_0 ?y_0)
        (clear ?y_0)
        (not (= ?x_0 ?y_0))
        (not (= ?x_0 ?y))
        (not (= ?y_0 ?y))
        (connected ?y_0 ?y)
        (clear ?y)
    )
    :effect 
    (and
        (not (robot-at ?x_0))
        (robot-at ?y)
        (not (robot-at ?y_0))
    )
) 

(:action nonr-putdown-laser-and-pickup-bomb-0
    :parameters (?x_0 - LOC)
    :precondition 
    (and
        (robot-at ?x_0)
        (holds-laser)
        (bomb-at ?x_0)
    )
    :effect 
    (and
        (not (holds-laser))
        (laser-at ?x_0)
        (not (arm-empty))
        (holds-bomb)
    )
) 

) 
