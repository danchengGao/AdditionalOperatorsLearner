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

(:action macro-break-mutex-group--818640527
    :parameters (?o - object ?x - room)
    :precondition 
    (at ?o ?x)
    :effect 
    (not (at ?o ?x))
) 

(:action macro-2-2-1826982585
    :parameters (?r - robot ?g - gripper ?x - room)
    :precondition 
    (and
        (not (free ?r ?g))
        (at-robby ?r ?x)
    )
    :effect 
    (and
        (at-robby ?r ?x)
        (at-robby ?r ?x)
    )
) 

(:action macro-2-2--390670116
    :parameters (?o - object ?x - room ?r - robot ?g - gripper)
    :precondition 
    (and
        (at ?o ?x)
        (free ?r ?g)
    )
    :effect 
    (and
        (carry ?r ?o ?g)
        (not (free ?r ?g))
    )
) 

(:action macro-3-3--879722411
    :parameters (?r - robot ?o - object ?g - gripper ?x - room)
    :precondition 
    (and
        (carry ?r ?o ?g)
        (not (free ?r ?g))
    )
    :effect 
    (and
        (not (free ?r ?g))
        (not (at ?o ?x))
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

(:action nonr-drop-and-pick-1
    :parameters (?r_0 - robot ?obj_0 - object ?room_0 - room ?g_0 - gripper ?r - robot)
    :precondition 
    (and
        (carry ?r_0 ?obj_0 ?g_0)
        (at-robby ?r_0 ?room_0)
        (not (= ?r_0 ?r))
        (at-robby ?r ?room_0)
        (free ?r ?g_0)
    )
    :effect 
    (and
        (free ?r_0 ?g_0)
        (not (carry ?r_0 ?obj_0 ?g_0))
        (carry ?r ?obj_0 ?g_0)
        (not (at ?obj_0 ?room_0))
        (not (free ?r ?g_0))
    )
) 

) 
