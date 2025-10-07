(define 
(domain spanner) 
(:requirements 
    :typing :strips :equality 
) 
(:types 
    object 
    locatable - object 
    location - object 
    man - locatable 
    nut - locatable 
    spanner - locatable 
) 
(:predicates 
    (at ?m - locatable ?l - location)
    (carrying ?m - man ?s - spanner)
    (link ?l1 - location ?l2 - location)
    (loose ?n - nut)
    (tightened ?n - nut)
    (useable ?s - spanner)
) 
(:action walk
    :parameters (?start - location ?end - location ?m - man)
    :precondition 
    (and
        (at ?m ?start)
        (link ?start ?end)
    )
    :effect 
    (and
        (not (at ?m ?start))
        (at ?m ?end)
    )
) 

(:action pickup_spanner
    :parameters (?l - location ?s - spanner ?m - man)
    :precondition 
    (and
        (at ?m ?l)
        (at ?s ?l)
    )
    :effect 
    (and
        (not (at ?s ?l))
        (carrying ?m ?s)
    )
) 

(:action tighten_nut
    :parameters (?l - location ?s - spanner ?m - man ?n - nut)
    :precondition 
    (and
        (at ?m ?l)
        (at ?n ?l)
        (carrying ?m ?s)
        (useable ?s)
        (loose ?n)
    )
    :effect 
    (and
        (not (loose ?n))
        (not (useable ?s))
        (tightened ?n)
    )
) 

(:action macro-2-2-1566244177
    :parameters (?m - man ?s - spanner ?m - locatable ?l - location)
    :precondition 
    (and
        (carrying ?m ?s)
        (at ?m ?l)
    )
    :effect 
    (and
        (not (useable ?s))
        (not (at ?m ?l))
    )
) 

) 
