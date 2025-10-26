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



(:action macro-relaxed-walk-and-pickup_spanner-7
    :parameters (?start_0 - location ?end_0 - location ?m_0 - man ?l - location ?s - spanner)
    :precondition 
    (and
        (at ?m_0 ?start_0)
        (link ?start_0 ?end_0)
        (not (= ?start_0 ?end_0))
        (not (= ?start_0 ?l))
        (not (= ?end_0 ?l))
        (at ?m_0 ?l)
        (at ?s ?l)
    )
    :effect 
    (and
        (at ?m_0 ?end_0)
        (carrying ?m_0 ?s)
    )
) 

) 
