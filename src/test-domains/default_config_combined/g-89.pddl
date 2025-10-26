(define 
(domain gripper-strips) 
(:requirements 
    :equality 
) 
(:types 
    gripper 
    object 
    robot 
    room 
) 
(:predicates 
    (at ?o - object ?x - room)
    (at-robby ?r - robot ?x - room)
    (carry ?r - robot ?o - object ?g - gripper)
    (free ?r - robot ?g - gripper)
) 
(:action move
    :parameters (?r - robot ?from - room ?to - room)
    :precondition 
    (and
        (at-robby ?r ?from)
    )
    :effect 
    (and
        (at-robby ?r ?to)
        (not (at-robby ?r ?from))
    )
) 

(:action pick
    :parameters (?r - robot ?obj - object ?room - room ?g - gripper)
    :precondition 
    (and
        (at ?obj ?room)
        (at-robby ?r ?room)
        (free ?r ?g)
    )
    :effect 
    (and
        (carry ?r ?obj ?g)
        (not (at ?obj ?room))
        (not (free ?r ?g))
    )
) 

(:action drop
    :parameters (?r - robot ?obj - object ?room - room ?g - gripper)
    :precondition 
    (and
        (carry ?r ?obj ?g)
        (at-robby ?r ?room)
    )
    :effect 
    (and
        (at ?obj ?room)
        (free ?r ?g)
        (not (carry ?r ?obj ?g))
    )
) 

(:action macro-relaxed-move-and-move-0
    :parameters (?r_0 - robot ?from_0 - room)
    :precondition 
    (and
        (at-robby ?r_0 ?from_0)
    )
    :effect 
    (and
        (at-robby ?r_0 ?from_0)
        (at-robby ?r_0 ?from_0)
    )
) 

(:action macro-2-2-123297866
    :parameters (?o - object ?x - room ?r - robot ?g - gripper)
    :precondition 
    (and
        (at ?o ?x)
        (free ?r ?g)
    )
    :effect 
    (and
        (at-robby ?r ?x)
        (carry ?r ?o ?g)
    )
) 

(:action macro-3-2--1636756609
    :parameters (?r - robot ?o - object ?g - gripper ?x - room)
    :precondition 
    (and
        (carry ?r ?o ?g)
        (free ?r ?g)
        (at-robby ?r ?x)
    )
    :effect 
    (and
        (free ?r ?g)
        (not (free ?r ?g))
    )
) 

(:action macro-2-2-1020439431
    :parameters (?r - robot ?x - room ?o - object ?g - gripper)
    :precondition 
    (and
        (not (at-robby ?r ?x))
        (carry ?r ?o ?g)
    )
    :effect 
    (and
        (at-robby ?r ?x)
        (at ?o ?x)
    )
) 

(:action nonr-move-and-drop-6
    :parameters (?r_0 - robot ?from_0 - room ?to_0 - room ?obj - object ?g - gripper)
    :precondition 
    (and
        (at-robby ?r_0 ?from_0)
        (not (= ?from_0 ?to_0))
        (carry ?r_0 ?obj ?g)
    )
    :effect 
    (and
        (at-robby ?r_0 ?to_0)
        (not (at-robby ?r_0 ?from_0))
        (at ?obj ?to_0)
        (free ?r_0 ?g)
        (not (carry ?r_0 ?obj ?g))
    )
) 

(:action nonr-move-and-pick-6
    :parameters (?r_0 - robot ?from_0 - room ?to_0 - room ?obj - object ?g - gripper)
    :precondition 
    (and
        (at-robby ?r_0 ?from_0)
        (not (= ?from_0 ?to_0))
        (at ?obj ?to_0)
        (free ?r_0 ?g)
    )
    :effect 
    (and
        (at-robby ?r_0 ?to_0)
        (not (at-robby ?r_0 ?from_0))
        (carry ?r_0 ?obj ?g)
        (not (at ?obj ?to_0))
        (not (free ?r_0 ?g))
    )
) 

) 
