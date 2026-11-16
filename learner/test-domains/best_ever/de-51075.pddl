(define 
(domain Depot) 
(:requirements 
    :typing :equality 
) 
(:types 
    surface - locatable 
    crate - surface 
    place - object 
    depot - place 
    distributor - place 
    locatable - object 
    hoist - locatable 
    object 
    pallet - surface 
    truck - locatable 
) 
(:predicates 
    (at ?x - locatable ?y - place)
    (available ?x - hoist)
    (clear ?x - surface)
    (in ?x - crate ?y - truck)
    (lifting ?x - hoist ?y - crate)
    (on ?x - crate ?y - surface)
) 
(:action Drive
    :parameters (?x - truck ?y - place ?z - place)
    :precondition 
    (and
        (at ?x ?y)
    )
    :effect 
    (and
        (not (at ?x ?y))
        (at ?x ?z)
    )
) 

(:action Lift
    :parameters (?x - hoist ?y - crate ?z - surface ?p - place)
    :precondition 
    (and
        (at ?x ?p)
        (available ?x)
        (at ?y ?p)
        (on ?y ?z)
        (clear ?y)
    )
    :effect 
    (and
        (not (at ?y ?p))
        (lifting ?x ?y)
        (not (clear ?y))
        (not (available ?x))
        (clear ?z)
        (not (on ?y ?z))
    )
) 

(:action Drop
    :parameters (?x - hoist ?y - crate ?z - surface ?p - place)
    :precondition 
    (and
        (at ?x ?p)
        (at ?z ?p)
        (clear ?z)
        (lifting ?x ?y)
    )
    :effect 
    (and
        (available ?x)
        (not (lifting ?x ?y))
        (at ?y ?p)
        (not (clear ?z))
        (clear ?y)
        (on ?y ?z)
    )
) 

(:action Load
    :parameters (?x - hoist ?y - crate ?z - truck ?p - place)
    :precondition 
    (and
        (at ?x ?p)
        (at ?z ?p)
        (lifting ?x ?y)
    )
    :effect 
    (and
        (not (lifting ?x ?y))
        (in ?y ?z)
        (available ?x)
    )
) 

(:action Unload
    :parameters (?x - hoist ?y - crate ?z - truck ?p - place)
    :precondition 
    (and
        (at ?x ?p)
        (at ?z ?p)
        (available ?x)
        (in ?y ?z)
    )
    :effect 
    (and
        (not (in ?y ?z))
        (not (available ?x))
        (lifting ?x ?y)
    )
) 

(:action macro-Unload-and-Drop-0
    :parameters (?x_0 - hoist ?y_0 - crate ?z_0 - truck ?p_0 - place ?z - surface)
    :precondition 
    (and
        (at ?x_0 ?p_0)
        (at ?z_0 ?p_0)
        (available ?x_0)
        (in ?y_0 ?z_0)
        (at ?z ?p_0)
        (clear ?z)
    )
    :effect 
    (and
        (not (in ?y_0 ?z_0))
        (available ?x_0)
        (not (lifting ?x_0 ?y_0))
        (at ?y_0 ?p_0)
        (not (clear ?z))
        (clear ?y_0)
        (on ?y_0 ?z)
    )
) 

(:action macro-Unload-and-Load-4
    :parameters (?x_0 - hoist ?y_0 - crate ?z_0 - truck ?p_0 - place ?z - truck)
    :precondition 
    (and
        (at ?x_0 ?p_0)
        (at ?z_0 ?p_0)
        (available ?x_0)
        (in ?y_0 ?z_0)
        (not (= ?z_0 ?z))
        (at ?z ?p_0)
    )
    :effect 
    (and
        (not (in ?y_0 ?z_0))
        (not (lifting ?x_0 ?y_0))
        (in ?y_0 ?z)
        (available ?x_0)
    )
) 

(:action macro-Drop-and-Drop-5
    :parameters (?x_0 - hoist ?y_0 - crate ?z_0 - surface ?p_0 - place ?x - hoist ?z - surface)
    :precondition 
    (and
        (at ?x_0 ?p_0)
        (at ?z_0 ?p_0)
        (clear ?z_0)
        (lifting ?x_0 ?y_0)
        (not (= ?x_0 ?x))
        (not (= ?z_0 ?z))
        (at ?x ?p_0)
        (at ?z ?p_0)
        (clear ?z)
        (lifting ?x ?y_0)
    )
    :effect 
    (and
        (available ?x_0)
        (not (lifting ?x_0 ?y_0))
        (at ?y_0 ?p_0)
        (not (clear ?z_0))
        (clear ?y_0)
        (on ?y_0 ?z_0)
        (available ?x)
        (not (lifting ?x ?y_0))
        (at ?y_0 ?p_0)
        (not (clear ?z))
        (clear ?y_0)
        (on ?y_0 ?z)
    )
) 

(:action macro-Drive-and-Drop-3
    :parameters (?x_0 - truck ?y_0 - place ?z_0 - place ?x - hoist ?y - crate ?z - surface)
    :precondition 
    (and
        (at ?x_0 ?y_0)
        (not (= ?y_0 ?z_0))
        (at ?x ?z_0)
        (at ?z ?z_0)
        (clear ?z)
        (lifting ?x ?y)
    )
    :effect 
    (and
        (not (at ?x_0 ?y_0))
        (at ?x_0 ?z_0)
        (available ?x)
        (not (lifting ?x ?y))
        (at ?y ?z_0)
        (not (clear ?z))
        (clear ?y)
        (on ?y ?z)
    )
) 

) 
