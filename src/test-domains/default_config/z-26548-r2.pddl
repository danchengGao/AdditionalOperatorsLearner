(define 
(domain zeno-travel) 
(:requirements 
    :typing :equality 
) 
(:types 
    object 
    aircraft - object 
    city - object 
    flevel - object 
    person - object 
) 
(:predicates 
    (at ?a - (either person aircraft) ?c - city)
    (fuel-level ?a - aircraft ?l - flevel)
    (in ?p - person ?a - aircraft)
    (next ?l1 - flevel ?l2 - flevel)
) 
(:action board
    :parameters (?p - person ?a - aircraft ?c - city)
    :precondition 
    (and
        (at ?p ?c)
        (at ?a ?c)
    )
    :effect 
    (and
        (not (at ?p ?c))
        (in ?p ?a)
    )
) 

(:action debark
    :parameters (?p - person ?a - aircraft ?c - city)
    :precondition 
    (and
        (in ?p ?a)
        (at ?a ?c)
    )
    :effect 
    (and
        (not (in ?p ?a))
        (at ?p ?c)
    )
) 

(:action fly
    :parameters (?a - aircraft ?c1 - city ?c2 - city ?l1 - flevel ?l2 - flevel)
    :precondition 
    (and
        (at ?a ?c1)
        (fuel-level ?a ?l1)
        (next ?l2 ?l1)
    )
    :effect 
    (and
        (not (at ?a ?c1))
        (at ?a ?c2)
        (not (fuel-level ?a ?l1))
        (fuel-level ?a ?l2)
    )
) 

(:action zoom
    :parameters (?a - aircraft ?c1 - city ?c2 - city ?l1 - flevel ?l2 - flevel ?l3 - flevel)
    :precondition 
    (and
        (at ?a ?c1)
        (fuel-level ?a ?l1)
        (next ?l2 ?l1)
        (next ?l3 ?l2)
    )
    :effect 
    (and
        (not (at ?a ?c1))
        (at ?a ?c2)
        (not (fuel-level ?a ?l1))
        (fuel-level ?a ?l3)
    )
) 

(:action refuel
    :parameters (?a - aircraft ?c - city ?l - flevel ?l1 - flevel)
    :precondition 
    (and
        (fuel-level ?a ?l)
        (next ?l ?l1)
        (at ?a ?c)
    )
    :effect 
    (and
        (fuel-level ?a ?l1)
        (not (fuel-level ?a ?l))
    )
) 

(:action macro-debark-and-fly-16
    :parameters (?p_0 - person ?a_0 - aircraft ?c_0 - city ?l1 - flevel ?l2 - flevel)
    :precondition 
    (and
        (in ?p_0 ?a_0)
        (at ?a_0 ?c_0)
        (not (= ?l1 ?l2))
        (fuel-level ?a_0 ?l1)
        (next ?l2 ?l1)
    )
    :effect 
    (and
        (not (in ?p_0 ?a_0))
        (at ?p_0 ?c_0)
        (not (at ?a_0 ?c_0))
        (at ?a_0 ?c_0)
        (not (fuel-level ?a_0 ?l1))
        (fuel-level ?a_0 ?l2)
    )
) 



(:action macro-relaxed-fly-and-refuel-496
    :parameters (?a_0 - aircraft ?c1_0 - city ?l1_0 - flevel ?l2_0 - flevel ?l - flevel)
    :precondition 
    (and
        (at ?a_0 ?c1_0)
        (fuel-level ?a_0 ?l1_0)
        (next ?l2_0 ?l1_0)
        (not (= ?l1_0 ?l2_0))
        (not (= ?l1_0 ?l))
        (not (= ?l2_0 ?l))
        (fuel-level ?a_0 ?l)
        (next ?l ?l)
    )
    :effect 
    (and
        (at ?a_0 ?c1_0)
        (fuel-level ?a_0 ?l2_0)
        (fuel-level ?a_0 ?l)
    )
) 



) 
