(define 
(domain Depot) 
(:requirements 
    :equality 
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
(:action Drive-de-de
    :parameters (?x - truck ?y - depot ?z - depot)
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

(:action Drive-di-di
    :parameters (?x - truck ?y - distributor ?z - distributor)
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

(:action Drive-de-di
    :parameters (?x - truck ?y - depot ?z - distributor)
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

(:action Drive-di-de
    :parameters (?x - truck ?y - distributor ?z - depot)
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

(:action Lift-pallet-de
    :parameters (?x - hoist ?y - crate ?z - pallet ?p - depot)
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

(:action Lift-pallet-di
    :parameters (?x - hoist ?y - crate ?z - pallet ?p - distributor)
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

(:action Lift-crate-de
    :parameters (?x - hoist ?y - crate ?z - crate ?p - depot)
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

(:action Lift-crate-di
    :parameters (?x - hoist ?y - crate ?z - crate ?p - distributor)
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

(:action Drop-pallet-de
    :parameters (?x - hoist ?y - crate ?z - pallet ?p - depot)
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

(:action Drop-pallet-di
    :parameters (?x - hoist ?y - crate ?z - pallet ?p - distributor)
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

(:action Drop-crate-de
    :parameters (?x - hoist ?y - crate ?z - crate ?p - depot)
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

(:action Drop-crate-di
    :parameters (?x - hoist ?y - crate ?z - crate ?p - distributor)
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

(:action Load-de
    :parameters (?x - hoist ?y - crate ?z - truck ?p - depot)
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

(:action Load-di
    :parameters (?x - hoist ?y - crate ?z - truck ?p - distributor)
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

(:action Unload-de
    :parameters (?x - hoist ?y - crate ?z - truck ?p - depot)
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

(:action Unload-di
    :parameters (?x - hoist ?y - crate ?z - truck ?p - distributor)
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

(:action macro-Drive-and-Load-1
    :parameters (?x_0 - truck ?y_0 - place ?x - hoist ?y - crate ?z - truck)
    :precondition 
    (and
        (at ?x_0 ?y_0)
        (not (= ?x_0 ?z))
        (at ?x ?y_0)
        (at ?z ?y_0)
        (lifting ?x ?y)
    )
    :effect 
    (and
        (not (at ?x_0 ?y_0))
        (at ?x_0 ?y_0)
        (not (lifting ?x ?y))
        (in ?y ?z)
        (available ?x)
    )
) 

(:action macro-2-3-834186631
    :parameters (?x - locatable ?y - place ?x - crate ?y - surface ?x - hoist ?y - crate ?x - surface)
    :precondition 
    (and
        (not (at ?x ?y))
        (on ?x ?y)
    )
    :effect 
    (and
        (not (lifting ?x ?y))
        (on ?x ?y)
        (not (clear ?x))
    )
) 

(:action nonr-Drop-crate-di-and-Drop-crate-di-1
    :parameters (?x_0 - hoist ?y_0 - crate ?p_0 - distributor ?x - hoist)
    :precondition 
    (and
        (at ?x_0 ?p_0)
        (at ?y_0 ?p_0)
        (clear ?y_0)
        (lifting ?x_0 ?y_0)
        (not (= ?x_0 ?x))
        (at ?x ?p_0)
        (lifting ?x ?y_0)
    )
    :effect 
    (and
        (available ?x_0)
        (not (lifting ?x_0 ?y_0))
        (at ?y_0 ?p_0)
        (on ?y_0 ?y_0)
        (available ?x)
        (not (lifting ?x ?y_0))
        (at ?y_0 ?p_0)
        (not (clear ?y_0))
        (clear ?y_0)
        (on ?y_0 ?y_0)
    )
) 

(:action nonr-Drive-de-di-and-Drive-de-di-2
    :parameters (?x_0 - truck ?y_0 - depot ?z_0 - distributor ?y - depot)
    :precondition 
    (and
        (at ?x_0 ?y_0)
        (not (= ?y_0 ?y))
        (at ?x_0 ?y)
    )
    :effect 
    (and
        (not (at ?x_0 ?y_0))
        (at ?x_0 ?z_0)
        (not (at ?x_0 ?y))
        (at ?x_0 ?z_0)
    )
) 

(:action nonr-Drop-pallet-di-and-Load-de-3
    :parameters (?x_0 - hoist ?y_0 - crate ?z_0 - pallet ?p_0 - distributor ?x - hoist ?y - crate ?z - truck ?p - depot)
    :precondition 
    (and
        (at ?x_0 ?p_0)
        (at ?z_0 ?p_0)
        (clear ?z_0)
        (lifting ?x_0 ?y_0)
        (not (= ?x_0 ?x))
        (not (= ?y_0 ?y))
        (at ?x ?p)
        (at ?z ?p)
        (lifting ?x ?y)
    )
    :effect 
    (and
        (available ?x_0)
        (not (lifting ?x_0 ?y_0))
        (at ?y_0 ?p_0)
        (not (clear ?z_0))
        (clear ?y_0)
        (on ?y_0 ?z_0)
        (not (lifting ?x ?y))
        (in ?y ?z)
        (available ?x)
    )
) 

(:action nonr-Drop-pallet-di-and-Lift-crate-de-0
    :parameters (?x_0 - hoist ?y_0 - crate ?z_0 - pallet ?p_0 - distributor ?p - depot)
    :precondition 
    (and
        (at ?x_0 ?p_0)
        (at ?z_0 ?p_0)
        (clear ?z_0)
        (lifting ?x_0 ?y_0)
        (at ?x_0 ?p)
        (at ?y_0 ?p)
        (on ?y_0 ?y_0)
    )
    :effect 
    (and
        (at ?y_0 ?p_0)
        (not (clear ?z_0))
        (on ?y_0 ?z_0)
        (not (at ?y_0 ?p))
        (lifting ?x_0 ?y_0)
        (not (clear ?y_0))
        (not (available ?x_0))
        (clear ?y_0)
        (not (on ?y_0 ?y_0))
    )
) 

) 
