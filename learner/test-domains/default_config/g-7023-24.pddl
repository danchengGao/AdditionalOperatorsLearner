(define 
(domain gripper-strips) 
(:requirements 
     :strips :typing :equality 
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



) 
